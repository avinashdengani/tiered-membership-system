package com.quickcom.membership.dto.response;

import com.quickcom.membership.domain.enums.SubscriptionActionType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class SubscriptionHistoryResponse {

    private SubscriptionActionType actionType;
    private String previousTier;
    private String newTier;
    private String reason;
    private LocalDateTime changedAt;
}
