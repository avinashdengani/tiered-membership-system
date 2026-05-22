package com.quickcom.membership.repository;

import com.quickcom.membership.domain.entity.MembershipTier;
import com.quickcom.membership.domain.enums.TierType;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MembershipTierRepository extends JpaRepository<MembershipTier, UUID> {

    Optional<MembershipTier> findByTierTypeAndActiveTrue(TierType tierType);
    Optional<MembershipTier> findByDefaultTierTrueAndActiveTrue();
    List<MembershipTier> findAllByActiveTrueOrderByPriorityAsc();
}