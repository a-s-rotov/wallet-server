package ru.wallet.server.entities;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
public class UserEntity extends BaseEntity {

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = CascadeType.ALL)
    private List<AccountEntity> accountEntities;

    public List<AccountEntity> getAccountEntities() {
        return accountEntities;
    }

    public void setAccountEntities(List<AccountEntity> accountEntities) {
        this.accountEntities = accountEntities;
    }
}
