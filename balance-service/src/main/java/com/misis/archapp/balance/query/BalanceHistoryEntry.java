package com.misis.archapp.balance.query;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
public class BalanceHistoryEntry {

    @Id
    @GeneratedValue
    private Long id;
    private UUID userId;
    private String type;
    private BigDecimal amount;
    private Instant timestamp;

    public Long getId() {
        return id;
    }

    public UUID getUserId() {
        return userId;
    }

    public BalanceHistoryEntry setUserId(UUID userId) {
        this.userId = userId;
        return this;
    }

    public String getType() {
        return type;
    }

    public BalanceHistoryEntry setType(String type) {
        this.type = type;
        return this;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public BalanceHistoryEntry setAmount(BigDecimal amount) {
        this.amount = amount;
        return this;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public BalanceHistoryEntry setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
        return this;
    }
}