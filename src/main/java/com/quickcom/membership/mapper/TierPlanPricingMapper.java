package com.quickcom.membership.mapper;

import com.quickcom.membership.domain.entity.TierPlanPricing;
import com.quickcom.membership.dto.response.TierPlanPricingResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TierPlanPricingMapper {

    public TierPlanPricingResponse mapToResponse(TierPlanPricing tierPlanPricing) {

        TierPlanPricingResponse response = new TierPlanPricingResponse();

        response.setTierType(tierPlanPricing.getMembershipTier().getTierType());
        response.setPlanType(tierPlanPricing.getMembershipPlan().getPlanType());
        response.setPrice(tierPlanPricing.getPrice());
        response.setCurrency(tierPlanPricing.getCurrency());

        return response;
    }

    public List<TierPlanPricingResponse> mapToResponseList(List<TierPlanPricing> tierPlanPricingList) {

        return tierPlanPricingList.stream().map(this::mapToResponse).toList();
    }
}
