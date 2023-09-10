package sch.frog.monkey.lang.ast;

import sch.frog.monkey.lang.token.Token;

import java.util.Collections;
import java.util.List;

public class GroupExpression implements IExpressionStatement {

    private Token lparen;
    private final IExpressionStatement child;

    public GroupExpression(Token lparen, IExpressionStatement child) {
        this.child = child;
        this.lparen = lparen;
    }

    @Override
    public List<IAstNode> getChildren() {
        return Collections.singletonList(child);
    }

    public String toString(){
        return "group";
    }

    public IAstNode getSubExp(){
        return child;
    }
}
