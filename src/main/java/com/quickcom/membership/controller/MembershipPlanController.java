package com.quickcom.membership.controller;

import com.quickcom.membership.dto.response.MembershipPlanResponse;
import com.quickcom.membership.service.MembershipPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/membership-plans")
@RequiredArgsConstructor
public class MembershipPlanController {

    private final MembershipPlanService membershipPlanService;

    @GetMapping
    public ResponseEntity<List<MembershipPlanResponse>> getAllMembershipPlans() {

        return ResponseEntity.ok(membershipPlanService.getAllActivePlans());
    }
}
