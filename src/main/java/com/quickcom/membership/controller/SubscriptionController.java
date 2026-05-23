package com.quickcom.membership.controller;

import com.quickcom.membership.domain.enums.TierType;
import com.quickcom.membership.dto.request.CreateSubscriptionRequest;
import com.quickcom.membership.dto.response.SubscriptionResponse;
import com.quickcom.membership.dto.response.TierPlanPricingResponse;
import com.quickcom.membership.service.SubscriptionService;
import com.quickcom.membership.service.TierEvaluationService;
import com.quickcom.membership.service.TierPlanPricingService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;
    private final TierEvaluationService tierEvaluationService;
    private final TierPlanPricingService tierPlanPricingService;

    @PostMapping
    public ResponseEntity<SubscriptionResponse> createSubscription(
            @Valid @RequestBody CreateSubscriptionRequest request) {
        SubscriptionResponse response = subscriptionService.createSubscription(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{subscriptionId}")
    public ResponseEntity<SubscriptionResponse> getSubscription(@PathVariable UUID subscriptionId) {

        SubscriptionResponse response = subscriptionService.getSubscription(subscriptionId);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{subscriptionId}/cancel")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelSubscription(@PathVariable UUID subscriptionId) {

        subscriptionService.cancelSubscription(subscriptionId);
    }

    @PutMapping("/{subscriptionId}/upgrade")
    public ResponseEntity<SubscriptionResponse> upgradeTier(
            @PathVariable UUID subscriptionId, @RequestParam TierType newTierType) {
        return ResponseEntity.ok(subscriptionService.upgradeTier(subscriptionId, newTierType));
    }

    @PutMapping("/{subscriptionId}/downgrade")
    public ResponseEntity<SubscriptionResponse> downgradeTier(
            @PathVariable UUID subscriptionId, @RequestParam TierType newTierType) {
        return ResponseEntity.ok(subscriptionService.downgradeTier(subscriptionId, newTierType));
    }

    @PostMapping("/{subscriptionId}/evaluate-tier")
    public ResponseEntity<SubscriptionResponse> evaluateTier(@PathVariable UUID subscriptionId) {

        SubscriptionResponse response = tierEvaluationService.evaluateTier(subscriptionId);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/pricing")
    public ResponseEntity<List<TierPlanPricingResponse>> getAllPricing() {

        return ResponseEntity.ok(tierPlanPricingService.getAllPricing());
    }
}
