package com.quickcom.membership.service;

import com.quickcom.membership.dto.response.MembershipPlanResponse;

import java.util.List;

public interface MembershipPlanService {
    List<MembershipPlanResponse> getAllActivePlans();
}