package com.quickcom.membership.rule.operator;

import com.quickcom.membership.domain.enums.OperatorType;
import com.quickcom.membership.exception.ExceptionMessages;
import com.quickcom.membership.exception.base.ConfigurationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OperatorEvaluationService {

    private final List<OperatorEvaluator> operatorEvaluators;

    public boolean evaluate(OperatorType operatorType, Comparable actualValue, Comparable expectedValue) {
        OperatorEvaluator operatorEvaluator =
                operatorEvaluators.stream()
                                .filter(evaluator -> evaluator.getSupportedOperator() == operatorType)
                                .findFirst()
                                .orElseThrow(() -> new ConfigurationException(ExceptionMessages.UNSUPPORTED_OPERATOR_TYPE));

        return operatorEvaluator.evaluate(actualValue, expectedValue);
    }
}
