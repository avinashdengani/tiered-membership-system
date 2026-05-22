package com.quickcom.membership.service.impl;

import com.quickcom.membership.domain.entity.Subscription;
import com.quickcom.membership.domain.entity.SubscriptionHistory;
import com.quickcom.membership.domain.enums.SubscriptionActionType;
import com.quickcom.membership.repository.SubscriptionHistoryRepository;
import com.quickcom.membership.service.SubscriptionHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SubscriptionHistoryServiceImpl implements SubscriptionHistoryService {

    private final SubscriptionHistoryRepository subscriptionHistoryRepository;

    @Override
    public void recordTierHistory(Subscription subscription, SubscriptionActionType actionType, String previousTier, String newTier, String reason) {
        SubscriptionHistory history = new SubscriptionHistory();

        history.setSubscription(subscription);
        history.setActionType(actionType);
        history.setPreviousTier(previousTier);
        history.setNewTier(newTier);
        history.setReason(reason);

        subscriptionHistoryRepository.save(history);
    }
}
