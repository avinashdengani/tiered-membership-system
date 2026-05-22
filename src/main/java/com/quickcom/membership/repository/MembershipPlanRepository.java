package com.quickcom.membership.repository;

import com.quickcom.membership.domain.entity.MembershipPlan;
import com.quickcom.membership.domain.enums.PlanType;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MembershipPlanRepository extends JpaRepository<MembershipPlan, UUID> {

    Optional<MembershipPlan> findByPlanTypeAndActiveTrue(PlanType planType);
    List<MembershipPlan> findByActiveTrue();
}