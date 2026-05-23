package com.quickcom.membership.service.impl;

import com.quickcom.membership.domain.entity.MembershipTier;
import com.quickcom.membership.domain.entity.Subscription;
import com.quickcom.membership.domain.entity.TierRule;
import com.quickcom.membership.domain.enums.SubscriptionActionType;
import com.quickcom.membership.dto.response.SubscriptionResponse;
import com.quickcom.membership.mapper.SubscriptionMapper;
import com.quickcom.membership.repository.MembershipTierRepository;
import com.quickcom.membership.repository.SubscriptionRepository;
import com.quickcom.membership.repository.TierRuleRepository;
import com.quickcom.membership.rule.evaluator.TierRuleEvaluator;
import com.quickcom.membership.service.SubscriptionHistoryService;
import com.quickcom.membership.service.TierEvaluationService;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TierEvaluationServiceImpl implements TierEvaluationService {

    private final SubscriptionRepository subscriptionRepository;
    private final TierRuleRepository tierRuleRepository;
    private final List<TierRuleEvaluator> tierRuleEvaluators;
    private final SubscriptionMapper subscriptionMapper;
    private final SubscriptionHistoryService subscriptionHistoryService;
    private final MembershipTierRepository membershipTierRepository;

    @Override
    @Transactional
    public SubscriptionResponse evaluateTier(UUID subscriptionId) {

        Subscription subscription = getSubscription(subscriptionId);
        MembershipTier currentTier = subscription.getCurrentTier();

        List<TierRule> activeTierRules = getActiveTierRules();

        Map<MembershipTier, List<TierRule>> tierRulesMap = groupRulesByTier(activeTierRules);

        MembershipTier evaluatedTier = determineEligibleTier(subscription, tierRulesMap);

        if (currentTier.getId().equals(evaluatedTier.getId())) {
            return subscriptionMapper.mapToResponse(subscription);
        }

        updateSubscriptionTier(subscription, currentTier, evaluatedTier);

        return subscriptionMapper.mapToResponse(subscription);
    }

    private Subscription getSubscription(UUID subscriptionId) {

        return subscriptionRepository.findById(
                subscriptionId
        ).orElseThrow(() ->
                new IllegalArgumentException(
                        "Subscription not found"
                )
        );
    }

    private List<TierRule> getActiveTierRules() {

        return tierRuleRepository.findByActiveTrue();
    }

    private Map<MembershipTier, List<TierRule>> groupRulesByTier(List<TierRule> tierRules) {

        return tierRules.stream()
                .collect(Collectors.groupingBy(
                        TierRule::getMembershipTier
                ));
    }

    private MembershipTier determineEligibleTier(Subscription subscription, Map<MembershipTier, List<TierRule>> tierRulesMap) {

        MembershipTier eligibleTier = getDefaultTier();

        for (Map.Entry<MembershipTier, List<TierRule>> entry
                : tierRulesMap.entrySet()) {

            MembershipTier membershipTier = entry.getKey();

            List<TierRule> tierRules = entry.getValue();

            if (areAllRulesEligible(subscription, tierRules)
                    && membershipTier.getPriority() >
                    eligibleTier.getPriority()) {

                eligibleTier = membershipTier;
            }
        }

        return eligibleTier;
    }

    private boolean areAllRulesEligible(Subscription subscription, List<TierRule> tierRules) {

        return tierRules.stream()
                .allMatch(rule ->
                        evaluateRule(subscription, rule)
                );
    }

    private boolean evaluateRule(Subscription subscription, TierRule rule) {

        TierRuleEvaluator tierRuleEvaluator = getRuleEvaluator(rule);
        return tierRuleEvaluator.evaluate(subscription, rule);
    }

    private TierRuleEvaluator getRuleEvaluator(TierRule rule) {

        return tierRuleEvaluators.stream()
                .filter(evaluator ->
                        evaluator.getSupportedRuleType() == rule.getRuleType()
                )
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException("Unsupported rule type")
                );
    }

    private void updateSubscriptionTier(Subscription subscription, MembershipTier currentTier, MembershipTier evaluatedTier) {

        subscription.setCurrentTier(evaluatedTier);

        SubscriptionActionType subscriptionActionType = determineActionType(currentTier, evaluatedTier);
        subscriptionRepository.save(subscription);

        subscriptionHistoryService.recordTierHistory(
                subscription,
                subscriptionActionType,
                currentTier.getTierType().name(),
                evaluatedTier.getTierType().name(),
                subscriptionActionType.getDefaultReason()
        );
    }

    private SubscriptionActionType determineActionType(MembershipTier currentTier, MembershipTier evaluatedTier) {

        return evaluatedTier.getPriority() >
                currentTier.getPriority()
                ? SubscriptionActionType.UPGRADED
                : SubscriptionActionType.DOWNGRADED;
    }

    private MembershipTier getDefaultTier() {

        return membershipTierRepository
                .findByDefaultTierTrueAndActiveTrue()
                .orElseThrow(() -> new IllegalArgumentException(
                        "Default tier not configured"
                ));
    }
}