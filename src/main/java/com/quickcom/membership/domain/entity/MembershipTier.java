package com.quickcom.membership.domain.entity;

import com.quickcom.membership.domain.enums.TierType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "membership_tiers")
public class MembershipTier extends BaseEntity{

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true, length = 50)
    private TierType tierType;

    @Column(nullable = false, length = 100)
    private String displayName;

    @Column(nullable = false)
    private Integer priority;

    @Column(nullable = false)
    private Boolean active = true;

    @Column
    private boolean defaultTier;
}
