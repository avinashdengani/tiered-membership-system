package com.quickcom.membership.repository;

import com.quickcom.membership.domain.entity.MembershipPlan;
import com.quickcom.membership.domain.entity.MembershipTier;
import com.quickcom.membership.domain.entity.TierPlanPricing;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TierPlanPricingRepository extends JpaRepository<TierPlanPricing, UUID> {

    List<TierPlanPricing> findByActiveTrue();

    Optional<TierPlanPricing> findByMembershipTierAndMembershipPlanAndActiveTrue(
            MembershipTier membershipTier, MembershipPlan membershipPlan);
}
