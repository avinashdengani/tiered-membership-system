package com.quickcom.membership.service;

import com.quickcom.membership.dto.request.CreateSubscriptionRequest;
import com.quickcom.membership.dto.response.SubscriptionResponse;

import java.util.UUID;

public interface SubscriptionService {

    SubscriptionResponse createSubscription(CreateSubscriptionRequest createSubscriptionRequest);
    SubscriptionResponse getSubscription(UUID subscriptionId);
    void cancelSubscription(UUID subscriptionId);
}
