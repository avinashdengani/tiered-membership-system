package com.quickcom.membership.rule.operator;

import com.quickcom.membership.domain.enums.OperatorType;

public interface OperatorEvaluator {

    OperatorType getSupportedOperator();
    boolean evaluate(Comparable actualValue, Comparable expectedValue);
}