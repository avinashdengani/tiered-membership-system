package com.quickcom.membership.service.impl;

import com.quickcom.membership.domain.entity.MembershipPlan;
import com.quickcom.membership.domain.entity.MembershipTier;
import com.quickcom.membership.domain.entity.Subscription;
import com.quickcom.membership.domain.entity.User;

import com.quickcom.membership.domain.enums.SubscriptionStatus;
import com.quickcom.membership.domain.enums.TierType;

import com.quickcom.membership.dto.request.CreateSubscriptionRequest;
import com.quickcom.membership.dto.response.SubscriptionResponse;

import com.quickcom.membership.mapper.SubscriptionMapper;
import com.quickcom.membership.repository.MembershipPlanRepository;
import com.quickcom.membership.repository.MembershipTierRepository;
import com.quickcom.membership.repository.SubscriptionRepository;
import com.quickcom.membership.repository.UserRepository;

import com.quickcom.membership.service.SubscriptionService;

import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

    private final UserRepository userRepository;
    private final MembershipPlanRepository membershipPlanRepository;
    private final MembershipTierRepository membershipTierRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final SubscriptionMapper subscriptionMapper;

    @Override
    @Transactional
    public SubscriptionResponse createSubscription(
            CreateSubscriptionRequest request
    ) {

        User user = findOrCreateUser(request);

        validateNoActiveSubscription(user);

        MembershipPlan membershipPlan = getActiveMembershipPlan(request);

        MembershipTier defaultTier = getDefaultTier();

        Subscription subscription =
                buildSubscription(
                        user,
                        membershipPlan,
                        defaultTier
                );


        return subscriptionMapper.mapToResponse(subscriptionRepository.save(subscription));
    }

    private User findOrCreateUser(
            CreateSubscriptionRequest request
    ) {

        return userRepository.findByEmail(
                request.getEmail()
        ).orElseGet(() -> createUser(request));
    }

    private User createUser(
            CreateSubscriptionRequest request
    ) {

        User user = new User();

        user.setEmail(request.getEmail());
        user.setFullName(request.getFullName());
        user.setCohort(request.getCohort());

        return userRepository.save(user);
    }

    private void validateNoActiveSubscription(
            User user
    ) {

        boolean activeSubscriptionExists =
                subscriptionRepository.existsByUserIdAndStatus(
                        user.getId(),
                        SubscriptionStatus.ACTIVE
                );

        if (activeSubscriptionExists) {
            throw new IllegalStateException(
                    "Active subscription already exists"
            );
        }
    }

    private MembershipPlan getActiveMembershipPlan(
            CreateSubscriptionRequest request
    ) {

        return membershipPlanRepository
                .findByPlanTypeAndActiveTrue(
                        request.getPlanType()
                )
                .orElseThrow(() -> new IllegalArgumentException(
                        "Membership plan not found"
                ));
    }

    private MembershipTier getDefaultTier() {

        return membershipTierRepository
                .findByTierTypeAndActiveTrue(
                        TierType.SILVER
                )
                .orElseThrow(() -> new IllegalArgumentException(
                        "Default tier not configured"
                ));
    }

    private Subscription buildSubscription(
            User user,
            MembershipPlan membershipPlan,
            MembershipTier membershipTier
    ) {

        Subscription subscription = new Subscription();

        subscription.setUser(user);
        subscription.setMembershipPlan(membershipPlan);
        subscription.setCurrentTier(membershipTier);
        subscription.setStatus(SubscriptionStatus.ACTIVE);

        LocalDateTime now = LocalDateTime.now();

        subscription.setStartDate(now);
        subscription.setExpiryDate(
                now.plusDays(
                        membershipPlan.getValidityDays()
                )
        );

        return subscription;
    }

}