package sch.frog.monkey.lang.ast;

import sch.frog.monkey.lang.token.Token;
import sch.frog.monkey.lang.val.Value;

import java.util.List;

public final class StringExp implements IExpressionStatement{

    private final Token strToken;

    public StringExp(Token strToken){
        this.strToken = strToken;
    }

    @Override
    public List<IAstNode> getChildren() {
        return null;
    }

    @Override
    public String toString(){
        return strToken.getLiteral();
    }

    public Value evaluate(){
        return new Value(strToken.getLiteral());
    }
}
