package com.quickcom.membership.service;

import com.quickcom.membership.domain.enums.TierType;
import com.quickcom.membership.dto.request.CreateSubscriptionRequest;
import com.quickcom.membership.dto.response.SubscriptionResponse;

import java.util.UUID;

public interface SubscriptionService {

    SubscriptionResponse createSubscription(CreateSubscriptionRequest createSubscriptionRequest);

    SubscriptionResponse getSubscription(UUID subscriptionId);

    SubscriptionResponse getSubscriptionByUserId(UUID userId);

    SubscriptionResponse cancelSubscription(UUID subscriptionId);

    SubscriptionResponse upgradeTier(UUID subscriptionId, TierType newTierType);

    SubscriptionResponse downgradeTier(UUID subscriptionId, TierType newTierType);
}
