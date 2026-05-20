package com.quickcom.membership.dto.request;

import com.quickcom.membership.domain.enums.PlanType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateSubscriptionRequest {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String fullName;

    private String cohort;

    @NotNull
    private PlanType planType;
}
