package com.misis.archapp.balance.command;

import java.math.BigDecimal;
import java.util.UUID;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

public record DebitBalanceCommand(
    @TargetAggregateIdentifier UUID userId,
    BigDecimal amount
) {
}