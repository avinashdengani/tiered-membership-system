package com.quickcom.membership.rule.evaluator;

import com.quickcom.membership.domain.entity.Subscription;
import com.quickcom.membership.domain.entity.TierRule;
import com.quickcom.membership.domain.enums.RuleType;
import com.quickcom.membership.rule.operator.OperatorEvaluationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderCountRuleEvaluator implements TierRuleEvaluator{

    private final OperatorEvaluationService operatorEvaluationService;

    @Override
    public RuleType getSupportedRuleType() {
        return RuleType.ORDER_COUNT;
    }

    @Override
    public boolean evaluate(Subscription subscription, TierRule tierRule) {
        int currentOrderCount = 15;

        return operatorEvaluationService.evaluate(
                tierRule.getOperatorType(),
                currentOrderCount,
                Integer.parseInt(
                        tierRule.getRuleValue()
                )
        );
    }
}
