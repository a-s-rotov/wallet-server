package ru.wallet.server.services;

import ru.wallet.server.entities.AccountEntity;
import ru.wallet.server.entities.Currency;
import ru.wallet.server.entities.UserEntity;

import java.math.BigDecimal;
import java.util.List;

public interface DbWalletService {
    UserEntity createUser(Long id);

    void putDeposit(Long id, BigDecimal value, Currency currency);

    void takeDeposit(Long id, BigDecimal value, Currency currency);

    List<AccountEntity> getBalance(Long id);

    void deleteAllUsers();

    void deleteAllAccounts();

}
