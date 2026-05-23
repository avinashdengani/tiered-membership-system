package com.quickcom.membership.dto.response;

import com.quickcom.membership.domain.enums.BenefitType;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class BenefitResponse {

    private UUID id;
    private BenefitType benefitType;
    private String name;
    private String configuration;
}
