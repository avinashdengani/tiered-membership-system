package com.quickcom.membership.dto.response;

import com.quickcom.membership.domain.enums.PlanType;
import com.quickcom.membership.domain.enums.TierType;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class TierPlanPricingResponse {

    private PlanType planType;
    private TierType tierType;
    private BigDecimal price;
    private String currency;
}
