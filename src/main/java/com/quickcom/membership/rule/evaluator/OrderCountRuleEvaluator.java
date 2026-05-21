package com.quickcom.membership.rule.evaluator;

import com.quickcom.membership.domain.entity.Subscription;
import com.quickcom.membership.domain.entity.TierRule;
import com.quickcom.membership.domain.enums.RuleType;
import org.springframework.stereotype.Component;

@Component
public class OrderCountRuleEvaluator implements TierRuleEvaluator{

    @Override
    public RuleType getSupportedRuleType() {
        return RuleType.ORDER_COUNT;
    }

    @Override
    public boolean evaluate(Subscription subscription, TierRule tierRule) {
        int currentOrderCount = 15;
        return (currentOrderCount >= Integer.parseInt(tierRule.getRuleValue()));
    }
}
