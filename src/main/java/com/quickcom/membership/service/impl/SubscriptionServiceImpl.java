package com.quickcom.membership.service.impl;

import com.quickcom.membership.domain.entity.*;

import com.quickcom.membership.domain.enums.PlanType;
import com.quickcom.membership.domain.enums.SubscriptionActionType;
import com.quickcom.membership.domain.enums.SubscriptionStatus;
import com.quickcom.membership.domain.enums.TierType;

import com.quickcom.membership.dto.request.CreateSubscriptionRequest;
import com.quickcom.membership.dto.response.SubscriptionResponse;

import com.quickcom.membership.exception.ExceptionMessages;
import com.quickcom.membership.exception.base.ConfigurationException;
import com.quickcom.membership.exception.base.ConflictException;
import com.quickcom.membership.exception.base.ResourceNotFoundException;
import com.quickcom.membership.mapper.SubscriptionMapper;
import com.quickcom.membership.repository.*;

import com.quickcom.membership.service.SubscriptionHistoryService;
import com.quickcom.membership.service.SubscriptionService;

import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

    private final UserRepository userRepository;
    private final MembershipPlanRepository membershipPlanRepository;
    private final MembershipTierRepository membershipTierRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final TierBenefitRepository tierBenefitRepository;
    private final SubscriptionMapper subscriptionMapper;
    private final SubscriptionHistoryService subscriptionHistoryService;
    private final TierPlanPricingRepository tierPlanPricingRepository;

    @Override
    @Transactional
    public SubscriptionResponse createSubscription(CreateSubscriptionRequest request) {

        User user = findOrCreateUser(request);

        validateNoActiveSubscription(user);

        MembershipPlan membershipPlan = getActiveMembershipPlan(request.getPlanType());
        MembershipTier membershipTier = getActiveMembershipTier(request.getTierType());
        TierPlanPricing tierPlanPricing = getTierPlanPricing(membershipTier, membershipPlan);

        Subscription subscription = buildSubscription(user, membershipPlan, membershipTier, tierPlanPricing);

        subscriptionRepository.save(subscription);

        subscriptionHistoryService.recordTierHistory(
                subscription,
                SubscriptionActionType.SUBSCRIBED,
                null,
                membershipTier.getTierType().name(),
                SubscriptionActionType.SUBSCRIBED.getDefaultReason());

        return mapSubscriptionToResponse(subscription);
    }

    @Override
    @Transactional(readOnly = true)
    public SubscriptionResponse getSubscription(UUID subscriptionId) {

        Subscription subscription = subscriptionRepository
                .findById(subscriptionId)
                .orElseThrow(() -> new ResourceNotFoundException(ExceptionMessages.SUBSCRIPTION_NOT_FOUND));

        return mapSubscriptionToResponse(subscription);
    }

    @Override
    @Transactional
    public SubscriptionResponse cancelSubscription(UUID subscriptionId) {

        Subscription subscription = subscriptionRepository
                .findById(subscriptionId)
                .orElseThrow(() -> new ResourceNotFoundException(ExceptionMessages.SUBSCRIPTION_NOT_FOUND));

        if (subscription.getStatus() == SubscriptionStatus.CANCELLED) {
            throw new ConflictException(ExceptionMessages.SUBSCRIPTION_ALREADY_CANCELLED);
        }

        if (subscription.getStatus() != SubscriptionStatus.ACTIVE) {
            throw new ResourceNotFoundException(ExceptionMessages.ACTIVE_SUBSCRIPTION_NOT_FOUND);
        }

        subscription.setStatus(SubscriptionStatus.CANCELLED);

        subscriptionRepository.save(subscription);

        subscriptionHistoryService.recordTierHistory(
                subscription,
                SubscriptionActionType.CANCELLED,
                subscription.getCurrentTier().getTierType().name(),
                null,
                SubscriptionActionType.CANCELLED.getDefaultReason());

        return mapSubscriptionToResponse(subscription);
    }

    @Override
    @Transactional
    public SubscriptionResponse upgradeTier(UUID subscriptionId, TierType newTierType) {

        Subscription subscription = subscriptionRepository
                .findByIdAndStatus(subscriptionId, SubscriptionStatus.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException(ExceptionMessages.ACTIVE_SUBSCRIPTION_NOT_FOUND));

        MembershipTier newTier = getActiveMembershipTier(newTierType);

        MembershipTier currentTier = subscription.getCurrentTier();

        if (newTier.getPriority() <= currentTier.getPriority()) {
            throw new ConflictException(ExceptionMessages.NEW_TIER_MUST_BE_HIGHER);
        }

        updateSubscriptionTier(subscription, currentTier, newTier, SubscriptionActionType.UPGRADED);

        return mapSubscriptionToResponse(subscription);
    }

    @Transactional
    @Override
    public SubscriptionResponse downgradeTier(UUID subscriptionId, TierType newTierType) {

        Subscription subscription = subscriptionRepository
                .findByIdAndStatus(subscriptionId, SubscriptionStatus.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException(ExceptionMessages.ACTIVE_SUBSCRIPTION_NOT_FOUND));

        MembershipTier newTier = getActiveMembershipTier(newTierType);

        MembershipTier currentTier = subscription.getCurrentTier();

        if (newTier.getPriority() >= currentTier.getPriority()) {
            throw new ConflictException(ExceptionMessages.NEW_TIER_MUST_BE_LOWER);
        }

        updateSubscriptionTier(subscription, currentTier, newTier, SubscriptionActionType.DOWNGRADED);

        return mapSubscriptionToResponse(subscription);
    }

    private User findOrCreateUser(CreateSubscriptionRequest request) {

        return userRepository.findByEmail(request.getEmail()).orElseGet(() -> createUser(request));
    }

    private User createUser(CreateSubscriptionRequest request) {

        User user = new User();

        user.setEmail(request.getEmail());
        user.setFullName(request.getFullName());
        user.setCohort(request.getCohort());

        return userRepository.save(user);
    }

    private void validateNoActiveSubscription(User user) {

        boolean activeSubscriptionExists =
                subscriptionRepository.existsByUserIdAndStatus(user.getId(), SubscriptionStatus.ACTIVE);

        if (activeSubscriptionExists) {
            throw new ConflictException(ExceptionMessages.DUPLICATE_ACTIVE_SUBSCRIPTION);
        }
    }

    private MembershipPlan getActiveMembershipPlan(PlanType planType) {

        return membershipPlanRepository
                .findByPlanTypeAndActiveTrue(planType)
                .orElseThrow(() -> new ResourceNotFoundException(ExceptionMessages.MEMBERSHIP_PLAN_NOT_FOUND));
    }

    private MembershipTier getActiveMembershipTier(TierType tierType) {

        return membershipTierRepository
                .findByTierTypeAndActiveTrue(tierType)
                .orElseThrow(() -> new ResourceNotFoundException(ExceptionMessages.MEMBERSHIP_TIER_NOT_FOUND));
    }

    private MembershipTier getDefaultTier() {

        return membershipTierRepository
                .findByDefaultTierTrueAndActiveTrue()
                .orElseThrow(() -> new ConfigurationException(ExceptionMessages.DEFAULT_TIER_NOT_CONFIGURED));
    }

    private Subscription buildSubscription(
            User user, MembershipPlan membershipPlan, MembershipTier membershipTier, TierPlanPricing tierPlanPricing) {

        Subscription subscription = new Subscription();

        subscription.setUser(user);
        subscription.setMembershipPlan(membershipPlan);
        subscription.setCurrentTier(membershipTier);
        subscription.setTierPlanPricing(tierPlanPricing);
        subscription.setAmountPaid(tierPlanPricing.getPrice());
        subscription.setStatus(SubscriptionStatus.ACTIVE);

        LocalDateTime now = LocalDateTime.now();

        subscription.setStartDate(now);
        subscription.setExpiryDate(now.plusDays(membershipPlan.getValidityDays()));

        return subscription;
    }

    private SubscriptionResponse mapSubscriptionToResponse(Subscription subscription) {

        List<TierBenefit> tierBenefits =
                tierBenefitRepository.findAllByTierAndBenefitActiveTrue(subscription.getCurrentTier());
        return subscriptionMapper.mapToResponse(subscription, tierBenefits);
    }

    private TierPlanPricing getTierPlanPricing(MembershipTier membershipTier, MembershipPlan membershipPlan) {

        return tierPlanPricingRepository
                .findByMembershipTierAndMembershipPlanAndActiveTrue(membershipTier, membershipPlan)
                .orElseThrow(() -> new ConfigurationException(ExceptionMessages.TIER_PLAN_PRICING_NOT_FOUND));
    }

    private void updateSubscriptionTier(
            Subscription subscription,
            MembershipTier currentTier,
            MembershipTier newTier,
            SubscriptionActionType subscriptionActionType) {

        TierPlanPricing tierPlanPricing = getTierPlanPricing(newTier, subscription.getMembershipPlan());

        subscription.setCurrentTier(newTier);
        subscription.setTierPlanPricing(tierPlanPricing);
        subscription.setAmountPaid(tierPlanPricing.getPrice());

        subscriptionRepository.save(subscription);

        subscriptionHistoryService.recordTierHistory(
                subscription,
                subscriptionActionType,
                currentTier.getTierType().name(),
                newTier.getTierType().name(),
                subscriptionActionType.getDefaultReason());
    }
}
