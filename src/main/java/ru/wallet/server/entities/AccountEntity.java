package ru.wallet.server.entities;


import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"user_id", "type"})}, indexes = {
        @Index(columnList = "user_id", name = "user_id_hidx")})
public class AccountEntity extends BaseEntity {


    @Column
    @Enumerated(EnumType.STRING)
    private Currency type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column
    private BigDecimal balance;

    public Currency getType() {
        return type;
    }

    public void setType(Currency type) {
        this.type = type;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
