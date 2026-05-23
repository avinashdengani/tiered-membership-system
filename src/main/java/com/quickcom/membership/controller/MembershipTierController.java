package com.quickcom.membership.controller;

import com.quickcom.membership.dto.response.MembershipTierResponse;
import com.quickcom.membership.service.MembershipTierService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/subscriptions/tiers")
@RequiredArgsConstructor
public class MembershipTierController {

    private final MembershipTierService membershipTierService;

    @GetMapping
    public ResponseEntity<List<MembershipTierResponse>> getAllMembershipTiers() {

        return ResponseEntity.ok(membershipTierService.getAllActiveTiers());
    }
}
