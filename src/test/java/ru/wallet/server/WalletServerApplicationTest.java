package ru.wallet.server;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.wallet.server.utils.ClientUtil;

@RunWith(SpringRunner.class)
@SpringBootTest
public class WalletServerApplicationTest {

    @Autowired
    private ClientUtil clientUtil;

    @Test
    public void putDepositTest() {

        WalletServiceGrpc.WalletServiceBlockingStub service = clientUtil.getWalletServiceBlockingStub();

        service.putDeposit(Deposit.newBuilder()
                .setUserId(1)
                .setAmount("100.1")
                .addCurrencyValue(Deposit.Currency.USD_VALUE)
                .build());
    }

    @Test
    public void takeDepositTest() {
        WalletServiceGrpc.WalletServiceBlockingStub service = clientUtil.getWalletServiceBlockingStub();

        service.takeDeposit(Deposit.newBuilder()
                .setUserId(1)
                .setAmount("100.1")
                .addCurrencyValue(Deposit.Currency.USD_VALUE)
                .build());
    }

    @Test
    public void getBalanceTest() {
        WalletServiceGrpc.WalletServiceBlockingStub service = clientUtil.getWalletServiceBlockingStub();

        Balance balance = service.getBalance(User.newBuilder()
                .setUserId(1)
                .build());

    }
}
