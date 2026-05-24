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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

        User user = findUserById(request.getUserId());

        validateNoActiveSubscription(user);

        MembershipPlan membershipPlan = getActiveMembershipPlan(request.getPlanType());
        MembershipTier membershipTier = getActiveMembershipTier(request.getTierType());
        TierPlanPricing tierPlanPricing = getActiveTierPlanPricing(membershipTier, membershipPlan);

        Subscription subscription = subscriptionRepository
                .findByUserId(user.getId())
                .map(existing -> handleExistingSubscription(existing, membershipPlan, membershipTier, tierPlanPricing))
                .orElseGet(() -> handleFreshSubscription(user, membershipPlan, membershipTier, tierPlanPricing));

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
    public SubscriptionResponse getSubscriptionByUserId(UUID userId) {

        userRepository
                .findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(ExceptionMessages.USER_NOT_FOUND));

        Subscription subscription = subscriptionRepository
                .findByUserIdAndStatus(userId, SubscriptionStatus.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException(ExceptionMessages.ACTIVE_SUBSCRIPTION_NOT_FOUND));

        resolveExpiry(subscription);

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

    @Override
    @Transactional
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

    private Subscription handleExistingSubscription(
            Subscription existing,
            MembershipPlan membershipPlan,
            MembershipTier membershipTier,
            TierPlanPricing tierPlanPricing) {

        SubscriptionActionType actionType = resolveResubscribeActionType(existing.getStatus());

        reactivateSubscription(existing, membershipPlan, membershipTier, tierPlanPricing);

        subscriptionRepository.save(existing);

        subscriptionHistoryService.recordTierHistory(
                existing, actionType, null, membershipTier.getTierType().name(), actionType.getDefaultReason());

        return existing;
    }

    private Subscription handleFreshSubscription(
            User user, MembershipPlan membershipPlan, MembershipTier membershipTier, TierPlanPricing tierPlanPricing) {

        Subscription fresh = buildSubscription(user, membershipPlan, membershipTier, tierPlanPricing);

        subscriptionRepository.save(fresh);

        subscriptionHistoryService.recordTierHistory(
                fresh,
                SubscriptionActionType.SUBSCRIBED,
                null,
                membershipTier.getTierType().name(),
                SubscriptionActionType.SUBSCRIBED.getDefaultReason());

        return fresh;
    }

    private SubscriptionActionType resolveResubscribeActionType(SubscriptionStatus previousStatus) {
        return switch (previousStatus) {
            case CANCELLED -> SubscriptionActionType.RESUBSCRIBED;
            case EXPIRED -> SubscriptionActionType.RENEWED;
            default -> throw new ConflictException(ExceptionMessages.DUPLICATE_ACTIVE_SUBSCRIPTION);
        };
    }

    private Subscription buildSubscription(
            User user, MembershipPlan membershipPlan, MembershipTier membershipTier, TierPlanPricing tierPlanPricing) {

        Subscription subscription = new Subscription();
        subscription.setUser(user);

        applySubscriptionDetails(subscription, membershipPlan, membershipTier, tierPlanPricing);

        return subscription;
    }

    private void reactivateSubscription(
            Subscription existing,
            MembershipPlan membershipPlan,
            MembershipTier membershipTier,
            TierPlanPricing tierPlanPricing) {

        applySubscriptionDetails(existing, membershipPlan, membershipTier, tierPlanPricing);
    }

    private void applySubscriptionDetails(
            Subscription subscription,
            MembershipPlan membershipPlan,
            MembershipTier membershipTier,
            TierPlanPricing tierPlanPricing) {

        subscription.setMembershipPlan(membershipPlan);
        subscription.setCurrentTier(membershipTier);
        subscription.setTierPlanPricing(tierPlanPricing);
        subscription.setAmountPaid(tierPlanPricing.getPrice());
        subscription.setStatus(SubscriptionStatus.ACTIVE);

        LocalDateTime now = LocalDateTime.now();
        subscription.setStartDate(now);
        subscription.setExpiryDate(now.plusDays(membershipPlan.getValidityDays()));
    }

    private void updateSubscriptionTier(
            Subscription subscription,
            MembershipTier currentTier,
            MembershipTier newTier,
            SubscriptionActionType actionType) {

        TierPlanPricing tierPlanPricing = getActiveTierPlanPricing(newTier, subscription.getMembershipPlan());

        subscription.setCurrentTier(newTier);
        subscription.setTierPlanPricing(tierPlanPricing);
        subscription.setAmountPaid(tierPlanPricing.getPrice());

        subscriptionRepository.save(subscription);

        subscriptionHistoryService.recordTierHistory(
                subscription,
                actionType,
                currentTier.getTierType().name(),
                newTier.getTierType().name(),
                actionType.getDefaultReason());
    }

    private void resolveExpiry(Subscription subscription) {
        if (subscription.getStatus() == SubscriptionStatus.ACTIVE
                && LocalDateTime.now().isAfter(subscription.getExpiryDate())) {

            subscription.setStatus(SubscriptionStatus.EXPIRED);

            subscriptionRepository.save(subscription);

            subscriptionHistoryService.recordTierHistory(
                    subscription,
                    SubscriptionActionType.EXPIRED,
                    subscription.getCurrentTier().getTierType().name(),
                    null,
                    SubscriptionActionType.EXPIRED.getDefaultReason());
        }
    }

    private User findUserById(UUID userId) {
        return userRepository
                .findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(ExceptionMessages.USER_NOT_FOUND));
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

    private TierPlanPricing getActiveTierPlanPricing(MembershipTier membershipTier, MembershipPlan membershipPlan) {
        return tierPlanPricingRepository
                .findByMembershipTierAndMembershipPlanAndActiveTrue(membershipTier, membershipPlan)
                .orElseThrow(() -> new ConfigurationException(ExceptionMessages.TIER_PLAN_PRICING_NOT_FOUND));
    }

    private SubscriptionResponse mapSubscriptionToResponse(Subscription subscription) {
        List<TierBenefit> tierBenefits =
                tierBenefitRepository.findAllByTierAndBenefitActiveTrue(subscription.getCurrentTier());
        return subscriptionMapper.mapToResponse(subscription, tierBenefits);
    }
}
