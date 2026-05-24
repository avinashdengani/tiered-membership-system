package com.quickcom.membership.service;

import com.quickcom.membership.domain.entity.Subscription;
import com.quickcom.membership.domain.enums.SubscriptionActionType;
import com.quickcom.membership.dto.response.SubscriptionHistoryResponse;

import java.util.List;
import java.util.UUID;

public interface SubscriptionHistoryService {

    void recordTierHistory(
            Subscription subscription,
            SubscriptionActionType actionType,
            String previousTier,
            String newTier,
            String reason);

    List<SubscriptionHistoryResponse> getHistory(UUID subscriptionId);
}
