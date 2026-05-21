package com.quickcom.membership.controller;

import com.quickcom.membership.dto.request.CreateSubscriptionRequest;
import com.quickcom.membership.dto.response.SubscriptionResponse;
import com.quickcom.membership.service.SubscriptionService;
import com.quickcom.membership.service.TierEvaluationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;
    private final TierEvaluationService tierEvaluationService;

    @PostMapping
    public ResponseEntity<SubscriptionResponse> createSubscription(
            @Valid @RequestBody
            CreateSubscriptionRequest request
    ) {
        SubscriptionResponse response = subscriptionService.createSubscription(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);

    }

    @PostMapping("/{subscriptionId}/evaluate-tier")
    public ResponseEntity<SubscriptionResponse> evaluateTier(
            @PathVariable UUID subscriptionId
    ) {

        SubscriptionResponse response = tierEvaluationService.evaluateTier(subscriptionId);

        return ResponseEntity.ok(tierEvaluationService.evaluateTier(subscriptionId));
    }
}
