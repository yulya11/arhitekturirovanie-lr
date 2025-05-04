package com.misis.archapp.balance.query;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BalanceHistoryRepository extends JpaRepository<BalanceHistoryEntry, Long> {
    List<BalanceHistoryEntry> findAllByUserId(UUID userId);
}