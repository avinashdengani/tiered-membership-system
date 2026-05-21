package com.quickcom.membership.rule.operator;

import com.quickcom.membership.domain.enums.OperatorType;

public class LessThanOperatorEvaluator implements OperatorEvaluator{

    @Override
    public OperatorType getSupportedOperator() {
        return OperatorType.LESS_THAN;
    }

    @Override
    public boolean evaluate(Comparable actualValue, Comparable expectedValue) {
        return actualValue.compareTo(expectedValue) < 0;
    }
}
