package sch.frog.monkey.lang.ast;

import sch.frog.monkey.lang.token.Token;
import sch.frog.monkey.lang.val.Value;

import java.util.List;

public final class BOOL implements IExpressionStatement {

    private final Token value;

    public BOOL(Token t){
        value = t;
    }

    @Override
    public List<IAstNode> getChildren() {
        return null;
    }

    @Override
    public String toString(){
        return value.getLiteral();
    }

    public Value evaluate(){
        return "true".equals(value.getLiteral()) ? Value.TRUE : Value.FALSE;
    }


}
