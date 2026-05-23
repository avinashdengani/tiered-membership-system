package com.quickcom.membership.service;

import com.quickcom.membership.dto.response.TierPlanPricingResponse;

import java.util.List;

public interface TierPlanPricingService {

    List<TierPlanPricingResponse> getAllPricing();
}
