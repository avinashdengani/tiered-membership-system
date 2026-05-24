package com.quickcom.membership.rule.evaluator;

import com.quickcom.membership.domain.entity.Subscription;
import com.quickcom.membership.domain.entity.TierRule;
import com.quickcom.membership.domain.enums.RuleType;
import com.quickcom.membership.repository.OrderRepository;
import com.quickcom.membership.rule.operator.OperatorEvaluationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class MonthlySpendRuleEvaluator implements TierRuleEvaluator {
    private final OperatorEvaluationService operatorEvaluationService;
    private final OrderRepository orderRepository;

    @Override
    public RuleType getSupportedRuleType() {
        return RuleType.MONTHLY_SPEND;
    }

    @Override
    public boolean evaluate(Subscription subscription, TierRule tierRule) {

        BigDecimal monthlySpend = orderRepository.sumOrderValueSince(
                subscription.getUser().getId(), LocalDateTime.now().minusDays(30));

        return operatorEvaluationService.evaluate(
                tierRule.getOperatorType(), monthlySpend, new BigDecimal(tierRule.getRuleValue()));
    }
}
