package com.quickcom.membership.mapper;

import com.quickcom.membership.domain.entity.Benefit;
import com.quickcom.membership.domain.entity.TierBenefit;
import com.quickcom.membership.dto.response.BenefitResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BenefitMapper {

    public List<BenefitResponse> mapTierBenefitsToResponse(List<TierBenefit> tierBenefits) {

        return tierBenefits.stream()
                .map(TierBenefit::getBenefit)
                .map(this::mapToResponse)
                .toList();
    }

    public BenefitResponse mapToResponse(Benefit benefit) {

        BenefitResponse response = new BenefitResponse();

        response.setId(benefit.getId());
        response.setBenefitType(benefit.getBenefitType());
        response.setName(benefit.getName());
        response.setConfiguration(benefit.getConfiguration());

        return response;
    }
}
