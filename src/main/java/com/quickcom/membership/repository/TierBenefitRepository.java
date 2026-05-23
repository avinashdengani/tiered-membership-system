package com.quickcom.membership.repository;

import com.quickcom.membership.domain.entity.TierBenefit;
import com.quickcom.membership.domain.entity.MembershipTier;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TierBenefitRepository extends JpaRepository<TierBenefit, UUID> {

    List<TierBenefit> findAllByTier(MembershipTier tier);
    List<TierBenefit> findAllByTierAndBenefitActiveTrue(MembershipTier tier);
    List<TierBenefit> findAllByTierInAndBenefitActiveTrue(List<MembershipTier> tiers);
}
