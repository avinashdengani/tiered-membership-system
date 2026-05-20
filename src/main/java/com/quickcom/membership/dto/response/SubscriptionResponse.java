package com.quickcom.membership.dto.response;

import com.quickcom.membership.domain.enums.PlanType;
import com.quickcom.membership.domain.enums.SubscriptionStatus;
import com.quickcom.membership.domain.enums.TierType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class SubscriptionResponse {

    private UUID subscriptionId;

    private String userEmail;

    private PlanType planType;

    private TierType currentTier;

    private SubscriptionStatus subscriptionStatus;

    private LocalDateTime startDate;

    private LocalDateTime expiryDate;
}
