package com.quickcom.membership.service;

import com.quickcom.membership.domain.entity.Subscription;
import com.quickcom.membership.domain.enums.SubscriptionActionType;

public interface SubscriptionHistoryService {

    void recordTierHistory(
            Subscription subscription,
            SubscriptionActionType actionType,
            String previousTier,
            String newTier,
            String reason
    );
}
