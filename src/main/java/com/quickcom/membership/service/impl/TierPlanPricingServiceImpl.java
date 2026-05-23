package com.quickcom.membership.service.impl;

import com.quickcom.membership.domain.entity.TierPlanPricing;
import com.quickcom.membership.dto.response.TierPlanPricingResponse;
import com.quickcom.membership.mapper.TierPlanPricingMapper;
import com.quickcom.membership.repository.TierPlanPricingRepository;
import com.quickcom.membership.service.TierPlanPricingService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TierPlanPricingServiceImpl implements TierPlanPricingService {

    private final TierPlanPricingRepository tierPlanPricingRepository;
    private final TierPlanPricingMapper tierPlanPricingMapper;

    @Override
    @Transactional(readOnly = true)
    public List<TierPlanPricingResponse> getAllPricing() {

        List<TierPlanPricing> pricingList = tierPlanPricingRepository.findByActiveTrue();

        return tierPlanPricingMapper.mapToResponse(pricingList);
    }
}
