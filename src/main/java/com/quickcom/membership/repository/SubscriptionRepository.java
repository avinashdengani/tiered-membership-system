package com.quickcom.membership.repository;

import com.quickcom.membership.domain.entity.Subscription;
import com.quickcom.membership.domain.enums.SubscriptionStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SubscriptionRepository
        extends JpaRepository<Subscription, UUID> {

    Optional<Subscription> findByUserIdAndStatus(UUID userId, SubscriptionStatus status);

    boolean existsByUserIdAndStatus(UUID userId, SubscriptionStatus status);
}
