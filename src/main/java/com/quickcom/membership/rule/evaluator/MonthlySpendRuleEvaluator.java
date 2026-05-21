package com.quickcom.membership.rule.evaluator;

import com.quickcom.membership.domain.entity.Subscription;
import com.quickcom.membership.domain.entity.TierRule;
import com.quickcom.membership.domain.enums.RuleType;

import java.math.BigDecimal;

public class MonthlySpendRuleEvaluator implements TierRuleEvaluator{
    @Override
    public RuleType getSupportedRuleType() {
        return RuleType.MONTHLY_SPEND;
    }

    @Override
    public boolean evaluate(Subscription subscription, TierRule tierRule) {

        BigDecimal monthlySpend = BigDecimal.valueOf(12000);

        return monthlySpend.compareTo(new BigDecimal(tierRule.getRuleValue())) >= 0;
    }
}
