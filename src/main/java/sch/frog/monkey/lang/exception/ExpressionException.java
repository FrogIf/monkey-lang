package sch.frog.monkey.lang.exception;

public class ExpressionException extends Exception {

    private final int pos;

    public ExpressionException(String message, int pos) {
        super(message);
        this.pos = pos;
    }

    public int getPos() {
        return pos;
    }
}
