package com.quickcom.membership.service;

import java.util.UUID;

public interface TierEvaluationService {
    void evaluateTier(UUID subscriptionId);
}
