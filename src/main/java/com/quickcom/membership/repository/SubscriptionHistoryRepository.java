package com.quickcom.membership.repository;

import com.quickcom.membership.domain.entity.Subscription;
import com.quickcom.membership.domain.entity.SubscriptionHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SubscriptionHistoryRepository extends JpaRepository<SubscriptionHistory, UUID> {

    List<SubscriptionHistory> findBySubscriptionOrderByCreatedAtDesc(Subscription subscription);
}
