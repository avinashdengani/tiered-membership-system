package com.quickcom.membership.dto.response;

import com.quickcom.membership.domain.enums.SubscriptionStatus;
import com.quickcom.membership.domain.enums.TierType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class SubscriptionResponse {

    private UUID subscriptionId;
    private UUID userId;
    private String userEmail;
    private TierType currentTier;
    private String tierDisplayName;
    private SubscriptionStatus status;
    private LocalDateTime startDate;
    private LocalDateTime expiryDate;
    private long daysUntilExpiry;
    private boolean expired;
    private List<BenefitResponse> benefits;
    private TierPlanPricingResponse pricing;
}
