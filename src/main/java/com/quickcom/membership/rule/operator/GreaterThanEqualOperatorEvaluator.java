package com.quickcom.membership.rule.operator;

import com.quickcom.membership.domain.enums.OperatorType;
import org.springframework.stereotype.Component;

@Component
public class GreaterThanEqualOperatorEvaluator implements OperatorEvaluator{

    @Override
    public OperatorType getSupportedOperator() {
        return OperatorType.GREATER_THAN_EQUAL;
    }

    @Override
    public boolean evaluate(Comparable actualValue, Comparable expectedValue) {
        return actualValue.compareTo(expectedValue) >= 0;
    }
}
