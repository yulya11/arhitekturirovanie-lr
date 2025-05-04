package com.misis.archapp.balance.query;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BalanceViewRepository extends JpaRepository<BalanceView, UUID> {
}
