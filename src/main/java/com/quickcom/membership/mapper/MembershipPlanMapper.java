package com.quickcom.membership.mapper;

import com.quickcom.membership.domain.entity.MembershipPlan;
import com.quickcom.membership.dto.response.MembershipPlanResponse;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MembershipPlanMapper {

    public List<MembershipPlanResponse> mapToResponse(List<MembershipPlan> membershipPlans) {

        return membershipPlans.stream().map(this::mapToResponse).toList();
    }

    public MembershipPlanResponse mapToResponse(MembershipPlan membershipPlan) {

        MembershipPlanResponse response = new MembershipPlanResponse();

        response.setId(membershipPlan.getId());
        response.setPlanType(membershipPlan.getPlanType());
        response.setName(membershipPlan.getName());
        response.setPrice(membershipPlan.getPrice());
        response.setValidityDays(membershipPlan.getValidityDays());

        return response;
    }
}