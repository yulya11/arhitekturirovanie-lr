package com.misis.archapp.balance.query;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
public class BalanceView {

    @Id
    private UUID userId;
    private BigDecimal balance;

    public UUID getUserId() {
        return userId;
    }

    public BalanceView setUserId(UUID userId) {
        this.userId = userId;
        return this;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public BalanceView setBalance(BigDecimal balance) {
        this.balance = balance;
        return this;
    }
}