package com.quickcom.membership.mapper;

import com.quickcom.membership.domain.entity.Subscription;

import com.quickcom.membership.dto.response.SubscriptionResponse;

import org.springframework.stereotype.Component;

@Component
public class SubscriptionMapper {

    public SubscriptionResponse mapToResponse(Subscription subscription) {

        SubscriptionResponse response = new SubscriptionResponse();

        response.setSubscriptionId(subscription.getId());
        response.setUserEmail(subscription.getUser().getEmail());
        response.setPlanType(subscription.getMembershipPlan().getPlanType());
        response.setCurrentTier(subscription.getCurrentTier().getTierType());
        response.setStatus(subscription.getStatus());
        response.setStartDate(subscription.getStartDate());
        response.setExpiryDate(subscription.getExpiryDate());

        return response;
    }
}