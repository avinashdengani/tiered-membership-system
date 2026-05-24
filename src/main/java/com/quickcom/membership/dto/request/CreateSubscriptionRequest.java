package com.quickcom.membership.dto.request;

import com.quickcom.membership.domain.enums.PlanType;
import com.quickcom.membership.domain.enums.TierType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CreateSubscriptionRequest {

    @NotNull(message = "userId is required")
    private UUID userId;

    @NotNull(message = "planType is required")
    private PlanType planType;

    @NotNull(message = "tierType is required")
    private TierType tierType;
}
