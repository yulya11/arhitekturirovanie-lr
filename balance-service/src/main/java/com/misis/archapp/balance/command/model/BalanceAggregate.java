package com.misis.archapp.balance.command.model;

import com.misis.archapp.balance.command.CreateBalanceCommand;
import com.misis.archapp.balance.command.CreditBalanceCommand;
import com.misis.archapp.balance.command.DebitBalanceCommand;
import com.misis.archapp.balance.event.BalanceCreatedEvent;
import com.misis.archapp.balance.event.BalanceCreditedEvent;
import com.misis.archapp.balance.event.BalanceDebitedEvent;
import java.math.BigDecimal;
import java.util.UUID;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

@Aggregate
public class BalanceAggregate {

    @AggregateIdentifier
    private UUID userId;
    private BigDecimal balance = BigDecimal.ZERO;

    protected BalanceAggregate() {
    }

    // хендлер - конструктор, через эту команду агрегат создается
    @CommandHandler
    public BalanceAggregate(CreateBalanceCommand cmd) {
        AggregateLifecycle.apply(new BalanceCreatedEvent(cmd.userId()));
    }

    // данный хендлер работает с уже существующим агрегатом
    @CommandHandler
    public void handle(CreditBalanceCommand cmd) {
        if (cmd.amount().compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Amount must be > 0");

        AggregateLifecycle.apply(new BalanceCreditedEvent(cmd.userId(), cmd.amount()));
    }

    // данный хендлер работает с уже существующим агрегатом
    @CommandHandler
    public void handle(DebitBalanceCommand cmd) {
        if (cmd.amount().compareTo(balance) > 0)
            throw new IllegalStateException("Insufficient funds");

        AggregateLifecycle.apply(new BalanceDebitedEvent(cmd.userId(), cmd.amount()));
    }

    @EventSourcingHandler
    public void on(BalanceCreatedEvent e) {
        this.userId = e.userId();
        this.balance = BigDecimal.ZERO;
    }

    @EventSourcingHandler
    public void on(BalanceCreditedEvent e) {
        this.balance = balance.add(e.amount());
    }

    @EventSourcingHandler
    public void on(BalanceDebitedEvent e) {
        this.balance = balance.subtract(e.amount());
    }
}