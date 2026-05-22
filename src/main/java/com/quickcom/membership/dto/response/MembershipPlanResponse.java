package com.quickcom.membership.dto.response;

import com.quickcom.membership.domain.enums.PlanType;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class MembershipPlanResponse {

    private UUID id;
    private String name;
    private PlanType planType;
    private BigDecimal price;
    private Integer validityDays;
}