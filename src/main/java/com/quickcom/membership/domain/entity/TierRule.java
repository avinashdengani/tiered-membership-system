package com.quickcom.membership.domain.entity;

import com.quickcom.membership.domain.enums.OperatorType;
import com.quickcom.membership.domain.enums.RuleType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tier_rules")
public class TierRule extends BaseEntity{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tier_id", nullable = false)
    private MembershipTier membershipTier;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 100)
    private RuleType ruleType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private OperatorType operatorType;

    @Column(nullable = false)
    private Boolean active = true;

    @Column(nullable = false)
    private String ruleValue;
}
