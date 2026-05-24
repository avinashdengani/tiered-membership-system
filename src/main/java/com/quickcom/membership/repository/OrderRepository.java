package com.quickcom.membership.repository;

import com.quickcom.membership.domain.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {

    long countByUserId(UUID userId);

    long countByUserIdAndCreatedAtBetween(UUID userId, LocalDateTime startDate, LocalDateTime endDate);

    @Query("""
        SELECT COALESCE(SUM(o.orderValue), 0)
        FROM Order o
        WHERE o.user.id = :userId
        AND o.createdAt BETWEEN :startDate AND :endDate
        """)
    BigDecimal getTotalOrderValueForCreatedAtBetween(UUID userId, LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT COALESCE(SUM(o.orderValue), 0) FROM Order o WHERE o.user.id = :userId AND o.createdAt >= :since")
    BigDecimal sumOrderValueSince(@Param("userId") UUID userId, @Param("since") LocalDateTime since);
}
