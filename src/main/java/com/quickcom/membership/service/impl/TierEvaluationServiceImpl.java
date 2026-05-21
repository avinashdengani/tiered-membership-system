package com.quickcom.membership.service.impl;

import com.quickcom.membership.domain.entity.MembershipTier;
import com.quickcom.membership.domain.entity.Subscription;
import com.quickcom.membership.domain.entity.TierRule;
import com.quickcom.membership.repository.SubscriptionRepository;
import com.quickcom.membership.repository.TierRuleRepository;
import com.quickcom.membership.rule.evaluator.TierRuleEvaluator;
import com.quickcom.membership.service.TierEvaluationService;
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

    @Override
    public void evaluateTier(UUID subscriptionId) {

        Subscription subscription = getSubscription(subscriptionId);

        List<TierRule> activeTierRules = getActiveTierRules();

        Map<MembershipTier, List<TierRule>> tierRulesMap = groupRulesByTier(activeTierRules);

        MembershipTier highestEligibleTier =  determineHighestEligibleTier(subscription, tierRulesMap);

        updateSubscriptionTier(subscription, highestEligibleTier);
    }

    private Subscription getSubscription(
            UUID subscriptionId
    ) {

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

    private Map<MembershipTier, List<TierRule>>
    groupRulesByTier(
            List<TierRule> tierRules
    ) {

        return tierRules.stream()
                .collect(Collectors.groupingBy(
                        TierRule::getMembershipTier
                ));
    }

    private MembershipTier determineHighestEligibleTier(
            Subscription subscription,
            Map<MembershipTier, List<TierRule>> tierRulesMap
    ) {

        MembershipTier highestEligibleTier =
                subscription.getCurrentTier();

        for (Map.Entry<MembershipTier, List<TierRule>> entry
                : tierRulesMap.entrySet()) {

            MembershipTier membershipTier =
                    entry.getKey();

            List<TierRule> tierRules =
                    entry.getValue();

            boolean eligible =
                    areAllRulesEligible(
                            subscription,
                            tierRules
                    );

            if (eligible &&
                    membershipTier.getPriority() >
                            highestEligibleTier.getPriority()) {

                highestEligibleTier = membershipTier;
            }
        }

        return highestEligibleTier;
    }

    private boolean areAllRulesEligible(
            Subscription subscription,
            List<TierRule> tierRules
    ) {

        return tierRules.stream()
                .allMatch(rule ->
                        evaluateRule(subscription, rule)
                );
    }

    private boolean evaluateRule(Subscription subscription, TierRule rule) {

        TierRuleEvaluator tierRuleEvaluator =
                getRuleEvaluator(rule);

        return tierRuleEvaluator.evaluate(subscription, rule);
    }

    private TierRuleEvaluator getRuleEvaluator(
            TierRule rule
    ) {

        return tierRuleEvaluators.stream()
                .filter(evaluator ->
                        evaluator.getSupportedRuleType() == rule.getRuleType()
                )
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Unsupported rule type"
                        )
                );
    }

    private void updateSubscriptionTier(
            Subscription subscription,
            MembershipTier membershipTier
    ) {

        subscription.setCurrentTier(membershipTier);
        subscriptionRepository.save(subscription);
    }
}