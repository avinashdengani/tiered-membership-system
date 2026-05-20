package com.quickcom.membership.repository;

import com.quickcom.membership.domain.entity.MembershipTier;
import com.quickcom.membership.domain.entity.TierRule;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TierRuleRepository extends JpaRepository<TierRule, UUID> {

    List<TierRule> findAllByTierAndActiveTrue(MembershipTier tier);
}