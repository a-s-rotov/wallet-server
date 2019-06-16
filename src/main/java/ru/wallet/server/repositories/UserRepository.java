package ru.wallet.server.repositories;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.wallet.server.entities.UserEntity;


@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long> {


}
