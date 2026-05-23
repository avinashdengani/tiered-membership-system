package com.quickcom.membership.dto.request;

import com.quickcom.membership.domain.enums.PlanType;
import com.quickcom.membership.domain.enums.TierType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateSubscriptionRequest {

    @Email(message = "email must be a valid email address")
    @NotBlank(message = "email must not be blank")
    private String email;

    @NotBlank(message = "fullName must not be blank")
    private String fullName;

    private String cohort;

    @NotNull(message = "planType is required")
    private PlanType planType;

    @NotNull(message = "tierType is required")
    private TierType tierType;
}
