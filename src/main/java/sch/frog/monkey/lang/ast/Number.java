package sch.frog.monkey.lang.ast;

import sch.frog.monkey.lang.token.Token;

import java.util.List;

public class Number implements IExpressionStatement {

    private final Token num;

    public Number(Token num){
        this.num = num;
    }

    @Override
    public List<ITreeNode> getChildren() {
        return null;
    }

    @Override
    public String toString() {
        return num.getLiteral();
    }
}
