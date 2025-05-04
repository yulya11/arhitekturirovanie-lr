package com.misis.archapp.balance.api;

import com.misis.archapp.balance.query.BalanceHistoryEntry;
import com.misis.archapp.balance.query.BalanceHistoryRepository;
import com.misis.archapp.balance.query.BalanceView;
import com.misis.archapp.balance.query.BalanceViewRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/balances")
public class BalanceQueryController {
    private final BalanceViewRepository viewRepo;
    private final BalanceHistoryRepository historyRepo;

    @Autowired
    public BalanceQueryController(
        BalanceViewRepository viewRepo,
        BalanceHistoryRepository historyRepo
    ) {
        this.viewRepo = viewRepo;
        this.historyRepo = historyRepo;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<BalanceView> getBalance(@PathVariable UUID userId) {
        return viewRepo.findById(userId)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{userId}/history")
    public List<BalanceHistoryEntry> getHistory(@PathVariable UUID userId) {
        return historyRepo.findAllByUserId(userId);
    }
}