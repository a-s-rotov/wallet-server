package ru.wallet.server;

import com.google.protobuf.Empty;
import io.grpc.StatusRuntimeException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.wallet.server.entities.Currency;
import ru.wallet.server.services.DbWalletService;
import ru.wallet.server.utils.ClientUtil;


@RunWith(SpringRunner.class)
@SpringBootTest
public class WalletServerApplicationTest {

    @Autowired
    private ClientUtil clientUtil;

    private static final Logger logger =
            LoggerFactory.getLogger(WalletServerApplicationTest.class);
    @Autowired
    private DbWalletService dbWalletService;

    @Test
    public void putDepositTest() {

        WalletServiceGrpc.WalletServiceBlockingStub service = clientUtil.getWalletServiceBlockingStub();

        User user = User.newBuilder().setUserId(1l).build();


        service.putDeposit(Deposit.newBuilder()
                .setUser(user)
                .setAmount("100.10")
                .setCurrency(Currency.GPB.name())
                .build());
        BalanceMap balance = service.getBalance(user);

        Assert.assertEquals(balance.getBalanceMap().get(Currency.GPB.name()), "100.10");
    }

    @Test
    public void takeDepositTest() {
        WalletServiceGrpc.WalletServiceBlockingStub service = clientUtil.getWalletServiceBlockingStub();
        User user = User.newBuilder().setUserId(1l).build();

        service.takeDeposit(Deposit.newBuilder()
                .setUser(user)
                .setAmount("100")
                .setCurrency(Currency.GPB.name())
                .build());

        BalanceMap balance = service.getBalance(user);

        Assert.assertEquals(balance.getBalanceMap().get(Currency.GPB.name()), "0.10");
    }

    @Test
    public void getBalanceTest() {
        WalletServiceGrpc.WalletServiceBlockingStub service = clientUtil.getWalletServiceBlockingStub();

        BalanceMap balance = service.getBalance(User.newBuilder()
                .setUserId(1)
                .build());

        Assert.assertNotNull(balance.getBalanceMap());
    }

    private void checkBalance(BalanceMap balanceMap, String usd, String gpb, String eur) {
        Assert.assertEquals(balanceMap.getBalanceMap().get(Currency.USD.name()), usd);
        Assert.assertEquals(balanceMap.getBalanceMap().get(Currency.GPB.name()), gpb);
        Assert.assertEquals(balanceMap.getBalanceMap().get(Currency.EUR.name()), eur);
    }

    @Test
    public void integrationChainTest() {

        WalletServiceGrpc.WalletServiceBlockingStub service = clientUtil.getWalletServiceBlockingStub();

        User user = service.createUser(Empty.newBuilder().build());

        try {
            service.takeDeposit(Deposit.newBuilder()
                    .setUser(user)
                    .setAmount("200")
                    .setCurrency(Currency.USD.name())
                    .build());
        } catch (StatusRuntimeException e) {
            logger.info("insufficient_funds");
        }

        service.putDeposit(Deposit.newBuilder()
                .setUser(user)
                .setAmount("100")
                .setCurrency(Currency.USD.name())
                .build());

        checkBalance(service.getBalance(user), "100.00", "0.00", "0.00");

        try {
            service.takeDeposit(Deposit.newBuilder()
                    .setUser(user)
                    .setAmount("200")
                    .setCurrency(Currency.USD.name())
                    .build());
        } catch (StatusRuntimeException e) {
            logger.info("insufficient_funds");
        }

        service.putDeposit(Deposit.newBuilder()
                .setUser(user)
                .setAmount("100")
                .setCurrency(Currency.EUR.name())
                .build());

        checkBalance(service.getBalance(user), "100.00", "0.00", "100.00");


        try {
            service.takeDeposit(Deposit.newBuilder()
                    .setUser(user)
                    .setAmount("200")
                    .setCurrency(Currency.USD.name())
                    .build());
        } catch (StatusRuntimeException e) {
            logger.info("insufficient_funds");
        }


        service.putDeposit(Deposit.newBuilder()
                .setUser(user)
                .setAmount("100")
                .setCurrency(Currency.USD.name())
                .build());

        checkBalance(service.getBalance(user), "200.00", "0.00", "100.00");

        service.takeDeposit(Deposit.newBuilder()
                .setUser(user)
                .setAmount("200")
                .setCurrency(Currency.USD.name())
                .build());

        checkBalance(service.getBalance(user), "0.00", "0.00", "100.00");


    }
}
