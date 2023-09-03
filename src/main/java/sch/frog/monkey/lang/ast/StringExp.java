package sch.frog.monkey.lang.ast;

import sch.frog.monkey.lang.token.Token;

import java.util.List;

public class StringExp implements IExpressionStatement{

    private final Token strToken;

    public StringExp(Token strToken){
        this.strToken = strToken;
    }

    @Override
    public List<ITreeNode> getChildren() {
        return null;
    }

    @Override
    public String toString(){
        return strToken.getLiteral();
    }
}
