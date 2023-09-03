package sch.frog.monkey.lang.ast;

import sch.frog.monkey.lang.token.Token;

import java.util.List;

public class Identifier implements IExpressionStatement {

    private final Token token;

    public Identifier(Token token) {
        this.token = token;
    }

    public String literal(){
        return token.getLiteral();
    }

    @Override
    public String toString() {
        return token.getLiteral();
    }

    @Override
    public List<ITreeNode> getChildren() {
        return null;
    }
}
