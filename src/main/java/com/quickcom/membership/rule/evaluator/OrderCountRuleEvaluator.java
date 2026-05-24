package com.quickcom.membership.rule.evaluator;

import com.quickcom.membership.domain.entity.Subscription;
import com.quickcom.membership.domain.entity.TierRule;
import com.quickcom.membership.domain.enums.RuleType;
import com.quickcom.membership.repository.OrderRepository;
import com.quickcom.membership.rule.operator.OperatorEvaluationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderCountRuleEvaluator implements TierRuleEvaluator {

    private final OperatorEvaluationService operatorEvaluationService;
    private final OrderRepository orderRepository;

    @Override
    public RuleType getSupportedRuleType() {
        return RuleType.ORDER_COUNT;
    }

    @Override
    public boolean evaluate(Subscription subscription, TierRule tierRule) {
        long currentOrderCount =
                orderRepository.countByUserId(subscription.getUser().getId());

        return operatorEvaluationService.evaluate(
                tierRule.getOperatorType(), currentOrderCount, Long.parseLong(tierRule.getRuleValue()));
    }
}
