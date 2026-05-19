package com.quickcom.membership.domain.entity;

import com.quickcom.membership.domain.enums.SubscriptionActionType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "subscription_history")
public class SubscriptionHistory extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscription_id", nullable = false)
    private Subscription subscription;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 100)
    private SubscriptionActionType actionType;

    @Column(length = 50)
    private String previousTier;

    @Column(length = 50)
    private String newTier;

    @Column(length = 500)
    private String reason;
}
