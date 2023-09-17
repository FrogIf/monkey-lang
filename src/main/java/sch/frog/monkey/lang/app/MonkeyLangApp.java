package sch.frog.monkey.lang.app;

import sch.frog.monkey.lang.app.fun.LenFunction;
import sch.frog.monkey.lang.app.fun.PrintFunction;
import sch.frog.monkey.lang.evaluator.Evaluator;
import sch.frog.monkey.lang.exception.FunctionExistException;

public class MonkeyLangApp {

    public static void init(){
        try {
            Evaluator.addBuiltinFunction("print", new PrintFunction());
            Evaluator.addBuiltinFunction("len", new LenFunction());
        } catch (FunctionExistException e) {
            throw new Error(e);
        }
    }

}
