package com.quickcom.membership.rule.evaluator;

import com.quickcom.membership.domain.entity.Subscription;
import com.quickcom.membership.domain.entity.TierRule;
import com.quickcom.membership.domain.enums.RuleType;
import com.quickcom.membership.rule.operator.OperatorEvaluationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class MonthlySpendRuleEvaluator implements TierRuleEvaluator{
    private final OperatorEvaluationService operatorEvaluationService;

    @Override
    public RuleType getSupportedRuleType() {
        return RuleType.MONTHLY_SPEND;
    }

    @Override
    public boolean evaluate(Subscription subscription, TierRule tierRule) {

        BigDecimal monthlySpend = BigDecimal.valueOf(12000);

        return operatorEvaluationService.evaluate(
                tierRule.getOperatorType(),
                monthlySpend,
                new BigDecimal(tierRule.getRuleValue())
        );
    }
}
