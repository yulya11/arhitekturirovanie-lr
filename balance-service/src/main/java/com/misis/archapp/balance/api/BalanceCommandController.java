package com.misis.archapp.balance.api;

import com.misis.archapp.balance.command.CreateBalanceCommand;
import com.misis.archapp.balance.command.CreditBalanceCommand;
import com.misis.archapp.balance.command.DebitBalanceCommand;
import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/balances")
public class BalanceCommandController {
    private final CommandGateway gateway;

    @Autowired
    public BalanceCommandController(
        CommandGateway gateway
    ) {
        this.gateway = gateway;
    }

    @PostMapping("/{userId}/create")
    public CompletableFuture<String> create(@PathVariable UUID userId) {
        return gateway.send(new CreateBalanceCommand(userId));
    }

    @PostMapping("/{userId}/credit")
    public CompletableFuture<Void> credit(@PathVariable UUID userId, @RequestBody BigDecimal amount) {
        return gateway.send(new CreditBalanceCommand(userId, amount));
    }

    @PostMapping("/{userId}/debit")
    public CompletableFuture<Void> debit(@PathVariable UUID userId, @RequestBody BigDecimal amount) {
        return gateway.send(new DebitBalanceCommand(userId, amount));
    }
}