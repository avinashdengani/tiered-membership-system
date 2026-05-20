package com.quickcom.membership.repository;

import com.quickcom.membership.domain.entity.Benefit;
import com.quickcom.membership.domain.enums.BenefitType;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BenefitRepository
        extends JpaRepository<Benefit, UUID> {

    Optional<Benefit> findByBenefitTypeAndActiveTrue(
            BenefitType benefitType
    );

    List<Benefit> findAllByActiveTrue();
}