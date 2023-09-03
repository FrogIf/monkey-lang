package sch.frog.monkey.lang.ast;

import sch.frog.monkey.lang.token.Token;

import java.util.Arrays;
import java.util.List;

public class InfixExpression implements IExpressionStatement{

    protected final Token infix;

    protected final IExpressionStatement left;

    protected final IExpressionStatement right;

    public InfixExpression(IExpressionStatement left, Token infix, IExpressionStatement right) {
        this.infix = infix;
        this.left = left;
        this.right = right;
    }

    @Override
    public List<ITreeNode> getChildren() {
        return Arrays.asList(left, right);
    }

    @Override
    public String toString(){
        return infix.getLiteral();
    }
}
