package com.quickcom.membership.domain.entity;

import com.quickcom.membership.domain.enums.BenefitType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "benefits")
public class Benefit extends BaseEntity{

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true, length = 100)
    private BenefitType benefitType;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(nullable = false, columnDefinition = "jsonb")
    private String configuration;

    @Column(nullable = false)
    private Boolean active = true;
}
