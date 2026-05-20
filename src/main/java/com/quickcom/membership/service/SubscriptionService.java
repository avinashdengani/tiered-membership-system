package com.quickcom.membership.service;

import com.quickcom.membership.dto.request.CreateSubscriptionRequest;
import com.quickcom.membership.dto.response.SubscriptionResponse;

public interface SubscriptionService {

    SubscriptionResponse createSubscription(CreateSubscriptionRequest createSubscriptionRequest);
}
