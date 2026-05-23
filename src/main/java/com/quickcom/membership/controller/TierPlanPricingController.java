package com.quickcom.membership.controller;

import com.quickcom.membership.dto.response.MembershipTierResponse;
import com.quickcom.membership.dto.response.TierPlanPricingResponse;
import com.quickcom.membership.service.MembershipTierService;
import com.quickcom.membership.service.TierPlanPricingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/subscriptions/pricing")
@RequiredArgsConstructor
public class TierPlanPricingController {

    private final TierPlanPricingService tierPlanPricingService;

    @GetMapping
    public ResponseEntity<List<TierPlanPricingResponse>> getAllPricing() {

        return ResponseEntity.ok(tierPlanPricingService.getAllPricing());
    }
}
