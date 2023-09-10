package sch.frog.monkey.lang.ast;

import sch.frog.monkey.lang.token.Token;
import sch.frog.monkey.lang.val.Value;

import java.util.List;

public class Null implements IExpressionStatement{

    private Token nullToken;

    public Null(Token nullToken) {
        this.nullToken = nullToken;
    }

    @Override
    public List<IAstNode> getChildren() {
        return null;
    }

    @Override
    public String toString(){
        return nullToken.getLiteral();
    }

    public Value evaluate(){
        return Value.NULL;
    }
}
