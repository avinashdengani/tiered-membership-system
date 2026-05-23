package com.quickcom.membership.dto.response;

import com.quickcom.membership.domain.enums.SubscriptionStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class SubscriptionResponse {

    private UUID subscriptionId;
    private String userEmail;
    private SubscriptionStatus status;
    private LocalDateTime startDate;
    private LocalDateTime expiryDate;
    private List<BenefitResponse> benefits;
    private TierPlanPricingResponse pricing;
}
