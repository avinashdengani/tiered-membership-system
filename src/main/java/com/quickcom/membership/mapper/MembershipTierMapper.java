package com.quickcom.membership.mapper;

import com.quickcom.membership.domain.entity.MembershipTier;
import com.quickcom.membership.domain.entity.TierBenefit;
import com.quickcom.membership.dto.response.MembershipTierResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class MembershipTierMapper {

    private final BenefitMapper benefitMapper;

    public List<MembershipTierResponse> mapToResponse(
            List<MembershipTier> membershipTiers, Map<UUID, List<TierBenefit>> benefitsByTierId) {

        return membershipTiers.stream()
                .map(tier -> mapToResponse(tier, benefitsByTierId.getOrDefault(tier.getId(), List.of())))
                .toList();
    }

    public MembershipTierResponse mapToResponse(MembershipTier membershipTier, List<TierBenefit> tierBenefits) {

        MembershipTierResponse response = new MembershipTierResponse();

        response.setId(membershipTier.getId());
        response.setTierType(membershipTier.getTierType());
        response.setDisplayName(membershipTier.getDisplayName());
        response.setDefaultTier(membershipTier.isDefaultTier());
        response.setPriority(membershipTier.getPriority());
        response.setBenefits(benefitMapper.mapTierBenefitsToResponse(tierBenefits));

        return response;
    }
}
