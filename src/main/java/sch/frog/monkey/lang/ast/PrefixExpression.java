package sch.frog.monkey.lang.ast;

import sch.frog.monkey.lang.token.Token;

import java.util.Collections;
import java.util.List;

public class PrefixExpression implements IExpressionStatement {

    private final Token prefix;

    private final IExpressionStatement right;

    public PrefixExpression(Token prefix, IExpressionStatement right){
        this.prefix = prefix;
        this.right = right;
    }

    @Override
    public List<IAstNode> getChildren() {
        return Collections.singletonList(right);
    }

    @Override
    public String toString(){
        return prefix.getLiteral();
    }

    public String operator(){
        return prefix.getLiteral();
    }

    public IAstNode getRight(){
        return right;
    }
}
