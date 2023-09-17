package sch.frog.monkey.lang.val;

import sch.frog.monkey.lang.ast.IExpressionStatement;
import sch.frog.monkey.lang.evaluator.EvalContext;

import java.util.List;

public class FunctionDefine {

    private IExpressionStatement functionBody;

    private List<String> arguments;

    private EvalContext parent;

    public FunctionDefine(List<String> arguments, IExpressionStatement functionBody, EvalContext parent) {
        this.functionBody = functionBody;
        this.arguments = arguments;
        this.parent = parent;
    }

    public EvalContext getParentContext(){
        return parent;
    }

    public List<String> getFormatArgs(){
        return arguments;
    }

    public IExpressionStatement getBody(){
        return functionBody;
    }
}
