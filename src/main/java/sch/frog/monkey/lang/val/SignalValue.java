package sch.frog.monkey.lang.val;

public class SignalValue {

    private final Type type;

    private final Value val;

    public SignalValue(Type type, Value val) {
        this.type = type;
        this.val = val;
    }

    public Type type() {
        return type;
    }

    public Value val() {
        return val;
    }

    public enum Type{
        RETURN,
        ERROR,
        BREAK,
        CONTINUE;
    }

}
