package com.quickcom.membership.mapper;

import com.quickcom.membership.domain.entity.Subscription;
import com.quickcom.membership.domain.entity.TierBenefit;
import com.quickcom.membership.dto.response.SubscriptionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SubscriptionMapper {

    private final BenefitMapper benefitMapper;
    private final TierPlanPricingMapper tierPlanPricingMapper;

    public SubscriptionResponse mapToResponse(Subscription subscription, List<TierBenefit> tierBenefits) {

        SubscriptionResponse response = new SubscriptionResponse();

        response.setSubscriptionId(subscription.getId());
        response.setUserId(subscription.getUser().getId());
        response.setUserEmail(subscription.getUser().getEmail());
        response.setStatus(subscription.getStatus());
        response.setStartDate(subscription.getStartDate());
        response.setExpiryDate(subscription.getExpiryDate());
        response.setBenefits(benefitMapper.mapTierBenefitsToResponse(tierBenefits));
        response.setPricing(tierPlanPricingMapper.mapToResponse(subscription.getTierPlanPricing()));
        return response;
    }
}
