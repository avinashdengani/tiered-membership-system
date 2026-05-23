package com.quickcom.membership.service.impl;

import com.quickcom.membership.domain.entity.MembershipTier;
import com.quickcom.membership.domain.entity.TierBenefit;
import com.quickcom.membership.dto.response.MembershipTierResponse;
import com.quickcom.membership.mapper.MembershipTierMapper;
import com.quickcom.membership.repository.MembershipTierRepository;
import com.quickcom.membership.repository.TierBenefitRepository;
import com.quickcom.membership.service.MembershipTierService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MembershipTierServiceImpl implements MembershipTierService {
    private final  MembershipTierMapper membershipTierMapper;
    private final MembershipTierRepository membershipTierRepository;
    private final TierBenefitRepository tierBenefitRepository;

    @Override
    @Transactional(readOnly = true)
    public List<MembershipTierResponse> getAllActiveTiers() {

        List<MembershipTier> activeTiers = membershipTierRepository.findAllByActiveTrueOrderByPriorityAsc();
        List<TierBenefit> tierBenefits = tierBenefitRepository.findAllByTierInAndBenefitActiveTrue(activeTiers);

        Map<UUID, List<TierBenefit>> benefitsByTierId =
                tierBenefits.stream()
                        .collect(Collectors.groupingBy(tierBenefit -> tierBenefit.getTier().getId()));

        return membershipTierMapper.mapToResponse(activeTiers, benefitsByTierId);
    }
}
