package com.quickcom.membership.rule.evaluator;

import com.quickcom.membership.domain.entity.Subscription;
import com.quickcom.membership.domain.entity.TierRule;
import com.quickcom.membership.domain.enums.RuleType;
import com.quickcom.membership.rule.operator.OperatorEvaluationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CohortRuleEvaluator implements TierRuleEvaluator{
    private final OperatorEvaluationService operatorEvaluationService;

    @Override
    public RuleType getSupportedRuleType() {
        return RuleType.COHORT;
    }

    @Override
    public boolean evaluate(Subscription subscription, TierRule tierRule) {

        String cohort = subscription.getUser().getCohort();

        if(cohort == null) {
            return false;
        }

        return operatorEvaluationService.evaluate(
                tierRule.getOperatorType(),
                cohort,
                tierRule.getRuleValue()
        );
    }
}
