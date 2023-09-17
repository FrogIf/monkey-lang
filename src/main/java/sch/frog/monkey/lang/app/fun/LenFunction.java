package sch.frog.monkey.lang.app.fun;

import io.github.frogif.calculator.number.impl.RationalNumber;
import sch.frog.monkey.lang.evaluator.EvalContext;
import sch.frog.monkey.lang.evaluator.Evaluator;
import sch.frog.monkey.lang.evaluator.IBuiltinFunction;
import sch.frog.monkey.lang.val.Value;
import sch.frog.monkey.lang.val.ValueType;

public class LenFunction implements IBuiltinFunction {
    @Override
    public Value execute(Value[] arguments, EvalContext context) {
        if(arguments.length != 1){
            return Evaluator.error("wrong number of arguments, expect 1, but " + arguments.length);
        }
        Value val = arguments[0];
        if(val.getType() == ValueType.STRING){
            return new Value(RationalNumber.valueOf(String.valueOf(val.cast(String.class).length())));
        }else if(val.getType() == ValueType.ARRAY){new Value(RationalNumber.valueOf(String.valueOf(val.cast(Value[].class).length)));
            return new Value(RationalNumber.valueOf(String.valueOf(val.cast(Value[].class).length)));
        }else{
            return Evaluator.error("'len' not supported for " + val.getType().name());
        }
    }
}
