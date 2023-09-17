package sch.frog.monkey.lang.evaluator;

public class GeneralOutput implements IOutput{

    public static final GeneralOutput out = new GeneralOutput();

    @Override
    public void write(String str) {
        System.out.println(str);
    }
}