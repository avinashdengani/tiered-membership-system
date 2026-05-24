package com.quickcom.membership.service.impl;

import com.quickcom.membership.domain.entity.Subscription;
import com.quickcom.membership.domain.entity.SubscriptionHistory;
import com.quickcom.membership.domain.enums.SubscriptionActionType;
import com.quickcom.membership.dto.response.SubscriptionHistoryResponse;
import com.quickcom.membership.exception.ExceptionMessages;
import com.quickcom.membership.exception.base.ResourceNotFoundException;
import com.quickcom.membership.mapper.SubscriptionHistoryMapper;
import com.quickcom.membership.repository.SubscriptionHistoryRepository;
import com.quickcom.membership.repository.SubscriptionRepository;
import com.quickcom.membership.service.SubscriptionHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SubscriptionHistoryServiceImpl implements SubscriptionHistoryService {

    private final SubscriptionHistoryRepository subscriptionHistoryRepository;
    private final SubscriptionHistoryMapper subscriptionHistoryMapper;
    private final SubscriptionRepository subscriptionRepository;

    @Override
    @Transactional
    public void recordTierHistory(
            Subscription subscription,
            SubscriptionActionType actionType,
            String previousTier,
            String newTier,
            String reason) {
        SubscriptionHistory history = new SubscriptionHistory();

        history.setSubscription(subscription);
        history.setActionType(actionType);
        history.setPreviousTier(previousTier);
        history.setNewTier(newTier);
        history.setReason(reason);

        subscriptionHistoryRepository.save(history);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SubscriptionHistoryResponse> getHistory(UUID subscriptionId) {

        Subscription subscription = subscriptionRepository
                .findById(subscriptionId)
                .orElseThrow(() -> new ResourceNotFoundException(ExceptionMessages.SUBSCRIPTION_NOT_FOUND));

        List<SubscriptionHistory> history =
                subscriptionHistoryRepository.findBySubscriptionOrderByCreatedAtDesc(subscription);

        return subscriptionHistoryMapper.mapToResponse(history);
    }
}
