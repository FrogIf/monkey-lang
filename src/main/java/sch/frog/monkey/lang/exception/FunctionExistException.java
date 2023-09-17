package sch.frog.monkey.lang.exception;

public class FunctionExistException extends Exception{

    public FunctionExistException(String name) {
        super("function " + name + " has exist");
    }
}
