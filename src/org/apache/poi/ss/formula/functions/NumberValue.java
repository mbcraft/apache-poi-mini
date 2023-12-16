package org.apache.poi.ss.formula.functions;

import org.apache.poi.ss.formula.eval.*;

public class NumberValue extends Fixed1ArgFunction {

    @Override
    public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0) {
        try {
            double result = OperandResolver.coerceValueToDouble(arg0);

            return new NumberEval(result);
        } catch (EvaluationException e) {
            return ErrorEval.VALUE_INVALID;
        }
    }
}
