package com.quickcom.membership.domain.enums;

import lombok.Getter;

@Getter
public enum SubscriptionActionType {

    SUBSCRIBED("Initial tier assignment"),

    UPGRADED("Tier upgraded after evaluation"),

    DOWNGRADED("Tier downgraded after evaluation"),

    CANCELLED("Subscription cancelled"),

    RENEWED("Subscription renewed"),

    EXPIRED("Subscription expired");

    private final String defaultReason;

    SubscriptionActionType(String defaultReason) {
        this.defaultReason = defaultReason;
    }

}