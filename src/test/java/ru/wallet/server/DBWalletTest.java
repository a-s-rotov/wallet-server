package ru.wallet.server;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.wallet.server.entities.AccountEntity;
import ru.wallet.server.entities.Currency;
import ru.wallet.server.entities.UserEntity;
import ru.wallet.server.repositories.AccountRepository;
import ru.wallet.server.repositories.UserRepository;
import ru.wallet.server.services.DbWalletService;

import java.math.BigDecimal;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DBWalletTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private DbWalletService dbWalletService;


    @Before
    public void init() {
        dbWalletService.deleteAllAccounts();
        dbWalletService.deleteAllUsers();


    }

    @Test
    public void createUserTest() {

        UserEntity user = dbWalletService.createUser(null);

        Assert.assertTrue(userRepository.existsById(user.getId()));
    }

    @Test
    public void putDepositTest() {
        UserEntity user = dbWalletService.createUser(null);

        dbWalletService.putDeposit(user.getId(), new BigDecimal(300), Currency.GPB);
        AccountEntity account = accountRepository.findByUserIdAndType(user.getId(), Currency.GPB)
                .orElseThrow(() -> new NullPointerException("Account not found."));
        Assert.assertTrue(account.getBalance().compareTo(new BigDecimal(300)) == 0);
    }

    @Test
    public void takeDepositTest() {
        UserEntity user = dbWalletService.createUser(null);

        dbWalletService.putDeposit(user.getId(), new BigDecimal(300), Currency.GPB);

        dbWalletService.takeDeposit(user.getId(), new BigDecimal(200), Currency.GPB);


        AccountEntity account = accountRepository.findByUserIdAndType(user.getId(), Currency.GPB)
                .orElseThrow(() -> new NullPointerException("Account not found."));
        Assert.assertTrue(account.getBalance().compareTo(new BigDecimal(100)) == 0);
    }

}
