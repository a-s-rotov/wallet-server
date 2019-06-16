package ru.wallet.server.services;

import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.wallet.server.entities.AccountEntity;
import ru.wallet.server.entities.Currency;
import ru.wallet.server.entities.UserEntity;
import ru.wallet.server.exception.InvalidDataException;
import ru.wallet.server.repositories.AccountRepository;
import ru.wallet.server.repositories.UserRepository;

import java.math.BigDecimal;
import java.util.List;

@Service
public class DbWalletServiceImpl implements DbWalletService {
    private static final Logger logger =
            LoggerFactory.getLogger(DbWalletServiceImpl.class);
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AccountRepository accountRepository;

    private UserEntity createUserEntity(Long id) {
        UserEntity user = new UserEntity();
        user.setId(id);

        AccountEntity accountEur = new AccountEntity();
        accountEur.setType(Currency.EUR);
        accountEur.setBalance(new BigDecimal(0));
        accountEur.setUser(user);

        AccountEntity accountUsd = new AccountEntity();
        accountUsd.setType(Currency.USD);
        accountUsd.setBalance(new BigDecimal(0));
        accountUsd.setUser(user);

        AccountEntity accountGpb = new AccountEntity();
        accountGpb.setType(Currency.GPB);
        accountGpb.setBalance(new BigDecimal(0));
        accountGpb.setUser(user);


        user.setAccountEntities(Lists.newArrayList(accountEur, accountGpb, accountUsd));
        return user;
    }

    @Transactional
    public UserEntity createUser(Long id) {

        if (id == null) {
            UserEntity user = createUserEntity(null);
            user = userRepository.save(user);
            return user;
        }

        UserEntity userEntity = userRepository.findById(id).orElseGet(() -> {
            UserEntity user = createUserEntity(id);
            user = userRepository.save(user);
            return user;
        });
        logger.debug("User {} created", userEntity.getId());

        return userEntity;
    }

    @Transactional
    public void putDeposit(Long id, BigDecimal value, Currency currency) {
        Long userId = id;
        if (!userRepository.existsById(id)) {
            userId = createUser(id).getId();
        }

        accountRepository.findByUserIdAndType(userId, currency)
                .ifPresent(account -> {
                    BigDecimal currentBalance = account.getBalance();
                    account.setBalance(currentBalance.add(value));
                    accountRepository.save(account);
                });
        logger.debug("Added {}{} to account for user {}", value.toString(), currency.name(), userId);

    }

    @Transactional
    public void takeDeposit(Long id, BigDecimal value, Currency currency) {

        if (userRepository.existsById(id)) {

            accountRepository.findByUserIdAndType(id, currency).ifPresent(account -> {
                BigDecimal balance = account.getBalance();
                BigDecimal newValue = balance.subtract(value);
                if (newValue.compareTo(new BigDecimal(0)) <= -1) {
                    throw new InvalidDataException("Wrong sum");

                }
                account.setBalance(newValue);
                accountRepository.save(account);
            });

        }
        logger.debug("Took {}{} to account for user {}", value.toString(), currency.name(), id);

    }

    @Transactional
    public List<AccountEntity> getBalance(Long id) {
        if (!userRepository.existsById(id)) {
            throw new InvalidDataException("User not exist");
        }
        Iterable<AccountEntity> all = accountRepository.findByUserId(id);
        logger.debug("Balance for User {}", id);

        return (List<AccountEntity>) all;

    }

    @Transactional
    public void deleteAllUsers() {
        logger.debug("Deleted all users");

        userRepository.deleteAll();

    }

    @Transactional
    public void deleteAllAccounts() {
        logger.debug("Deleted all accounts");
        accountRepository.deleteAll();
    }


}
