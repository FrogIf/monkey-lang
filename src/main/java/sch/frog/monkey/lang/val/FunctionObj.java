package sch.frog.monkey.lang.val;

import sch.frog.monkey.lang.evaluator.IBuiltinFunction;

public class FunctionObj {

    private IBuiltinFunction builtinFunction;

    private FunctionDefine functionDefine;

    private final Type type;

    public FunctionObj(IBuiltinFunction builtinFunction) {
        this.builtinFunction = builtinFunction;
        this.type = Type.BUILTIN;
    }

    public FunctionObj(FunctionDefine functionDefine) {
        this.functionDefine = functionDefine;
        this.type = Type.CUSTOM;
    }

    public IBuiltinFunction getBuiltinFunction() {
        return builtinFunction;
    }

    public FunctionDefine getFunctionDefine() {
        return functionDefine;
    }

    public Type type() {
        return type;
    }

    public enum Type{
        BUILTIN,
        CUSTOM;
    }

}
