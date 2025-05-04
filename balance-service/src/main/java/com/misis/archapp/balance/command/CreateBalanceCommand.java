package com.misis.archapp.balance.command;

import java.util.UUID;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

public record CreateBalanceCommand(@TargetAggregateIdentifier UUID userId) {
}