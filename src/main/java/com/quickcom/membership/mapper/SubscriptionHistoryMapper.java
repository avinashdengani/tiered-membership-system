package com.quickcom.membership.mapper;

import com.quickcom.membership.domain.entity.SubscriptionHistory;
import com.quickcom.membership.dto.response.SubscriptionHistoryResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SubscriptionHistoryMapper {

    public List<SubscriptionHistoryResponse> mapToResponse(List<SubscriptionHistory> histories) {
        return histories.stream().map(this::mapToResponse).toList();
    }

    public SubscriptionHistoryResponse mapToResponse(SubscriptionHistory history) {

        SubscriptionHistoryResponse response = new SubscriptionHistoryResponse();

        response.setActionType(history.getActionType());
        response.setPreviousTier(history.getPreviousTier());
        response.setNewTier(history.getNewTier());
        response.setReason(history.getReason());
        response.setChangedAt(history.getCreatedAt());

        return response;
    }
}
