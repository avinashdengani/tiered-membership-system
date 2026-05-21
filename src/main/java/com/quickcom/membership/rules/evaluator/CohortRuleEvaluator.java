package com.quickcom.membership.rules.evaluator;

import com.quickcom.membership.domain.entity.Subscription;
import com.quickcom.membership.domain.entity.TierRule;
import com.quickcom.membership.domain.enums.RuleType;

public class CohortRuleEvaluator implements TierRuleEvaluator{
    @Override
    public RuleType getSupportedRuleType() {
        return RuleType.COHORT;
    }

    @Override
    public boolean evaluate(Subscription subscription, TierRule tierRule) {
        String cohort =
                subscription.getUser()
                        .getCohort();
        //TODO: ADD RULE VALUE
        return cohort != null && cohort.equalsIgnoreCase("");
    }
}
