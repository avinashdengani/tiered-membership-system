package com.quickcom.membership.service;

import com.quickcom.membership.dto.response.SubscriptionResponse;

import java.util.UUID;

public interface TierEvaluationService {
    SubscriptionResponse evaluateTier(UUID subscriptionId);
}
