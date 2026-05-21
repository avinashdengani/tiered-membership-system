package com.quickcom.membership.rule.operator;

import com.quickcom.membership.domain.enums.OperatorType;
import org.springframework.stereotype.Component;

@Component
public class EqualsOperatorEvaluator implements OperatorEvaluator{
    @Override
    public OperatorType getSupportedOperator() {
        return OperatorType.EQUALS;
    }

    @Override
    public boolean evaluate(Comparable actualValue, Comparable expectedValue) {
        return actualValue.compareTo(expectedValue) == 0;
    }
}
