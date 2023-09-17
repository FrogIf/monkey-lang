package sch.frog.monkey.lang.ast;

import sch.frog.monkey.lang.token.Token;
import sch.frog.monkey.lang.val.Value;

import java.util.List;

public final class StringExp implements IExpressionStatement{

    private final Token strToken;

    private final String str;

    public StringExp(Token strToken){
        this.strToken = strToken;
        String literal = strToken.getLiteral();
        str = literal.substring(1, literal.length() - 1);
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
        return new Value(str);
    }
}
