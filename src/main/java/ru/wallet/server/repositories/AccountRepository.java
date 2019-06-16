package ru.wallet.server.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.wallet.server.entities.AccountEntity;
import ru.wallet.server.entities.Currency;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends CrudRepository<AccountEntity, Long> {

    @Query("SELECT u FROM AccountEntity u WHERE u.user.id = :userId and u.type = :type ")
    Optional<AccountEntity> findByUserIdAndType(@Param("userId") Long userId, @Param("type") Currency currency);

    @Query("SELECT u FROM AccountEntity u WHERE u.user.id = :userId")
    List<AccountEntity> findByUserId(@Param("userId") Long userId);

}
