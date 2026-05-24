package com.quickcom.membership.controller;

import com.quickcom.membership.dto.response.SubscriptionResponse;
import com.quickcom.membership.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final SubscriptionService subscriptionService;

    @GetMapping("/{userId}/subscription")
    public ResponseEntity<SubscriptionResponse> getSubscriptionByUser(@PathVariable UUID userId) {
        return ResponseEntity.ok(subscriptionService.getSubscriptionByUserId(userId));
    }
}
