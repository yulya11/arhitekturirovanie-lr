package com.misis.archapp.balance.event;

import java.util.UUID;

public record BalanceCreatedEvent(UUID userId) {
}