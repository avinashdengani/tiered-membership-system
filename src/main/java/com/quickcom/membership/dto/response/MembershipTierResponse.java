package com.quickcom.membership.dto.response;

import com.quickcom.membership.domain.enums.TierType;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class MembershipTierResponse {

    private UUID id;
    private TierType tierType;
    private String displayName;
    private Integer priority;
    private boolean defaultTier;
    private List<BenefitResponse> benefits;
}
