package sch.frog.monkey.lang.evaluator;

import sch.frog.monkey.lang.val.Value;

public interface IBuiltinFunction {

    Value execute(Value[] arguments, EvalContext context);

}
