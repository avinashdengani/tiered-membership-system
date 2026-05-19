package com.quickcom.membership.domain.entity;

import com.quickcom.membership.domain.enums.PlanType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "membership_plans")
public class MembershipPlan extends BaseEntity{

    @Column(nullable = false, length = 100)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private PlanType planType;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(nullable = false)
    private Integer validityDays;

    @Column(nullable = false)
    private Boolean active = true;
}
