package com.misis.archapp.balance.query.projection;

import com.misis.archapp.balance.event.BalanceCreatedEvent;
import com.misis.archapp.balance.event.BalanceCreditedEvent;
import com.misis.archapp.balance.event.BalanceDebitedEvent;
import com.misis.archapp.balance.query.BalanceHistoryEntry;
import com.misis.archapp.balance.query.BalanceHistoryRepository;
import com.misis.archapp.balance.query.BalanceView;
import com.misis.archapp.balance.query.BalanceViewRepository;
import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.Instant;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BalanceProjection {

    private final BalanceViewRepository balanceViewRepository;
    private final BalanceHistoryRepository balanceHistoryRepository;

    @Autowired
    public BalanceProjection(
        BalanceViewRepository balanceViewRepository,
        BalanceHistoryRepository balanceHistoryRepository
    ) {
        this.balanceViewRepository = balanceViewRepository;
        this.balanceHistoryRepository = balanceHistoryRepository;
    }

    @EventHandler
    public void on(BalanceCreatedEvent e) {
        BalanceView view = new BalanceView().setUserId(e.userId()).setBalance(BigDecimal.ZERO);
        balanceViewRepository.save(view);

        // вносит запись о событии в историю
        BalanceHistoryEntry historyEntry =
            new BalanceHistoryEntry()
                .setUserId(e.userId())
                .setType("CREATE")
                .setAmount(BigDecimal.ZERO)
                .setTimestamp(Instant.now());
        balanceHistoryRepository.save(historyEntry);
    }

    @EventHandler
    public void on(BalanceCreditedEvent e) {
        // меняет состояние read-модели баланса пользователя - добавляет деньги
        BalanceView view = balanceViewRepository.findById(e.userId()).orElseThrow(EntityNotFoundException::new);
        view.setBalance(view.getBalance().add(e.amount()));
        balanceViewRepository.save(view);

        // вносит запись о событии в историю
        BalanceHistoryEntry historyEntry =
            new BalanceHistoryEntry()
                .setUserId(e.userId())
                .setType("CREDIT")
                .setAmount(e.amount())
                .setTimestamp(Instant.now());
        balanceHistoryRepository.save(historyEntry);
    }

    @EventHandler
    public void on(BalanceDebitedEvent e) {
        // меняет состояние read-модели баланса пользователя - забирает деньги
        BalanceView view = balanceViewRepository.findById(e.userId()).orElseThrow(EntityNotFoundException::new);
        view.setBalance(view.getBalance().subtract(e.amount()));
        balanceViewRepository.save(view);

        // вносит запись о событии в историю
        BalanceHistoryEntry historyEntry =
            new BalanceHistoryEntry()
                .setUserId(e.userId())
                .setType("DEBIT")
                .setAmount(e.amount())
                .setTimestamp(Instant.now());
        balanceHistoryRepository.save(historyEntry);
    }

}