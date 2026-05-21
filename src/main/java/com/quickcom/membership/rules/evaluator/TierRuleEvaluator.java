package com.quickcom.membership.rules.evaluator;

import com.quickcom.membership.domain.entity.Subscription;
import com.quickcom.membership.domain.entity.TierRule;
import com.quickcom.membership.domain.enums.RuleType;

public interface TierRuleEvaluator {
    RuleType getSupportedRuleType();
    boolean evaluate(Subscription subscription, TierRule tierRule);
}
