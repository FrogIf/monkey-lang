package sch.frog.monkey.lang.ast;

import sch.frog.monkey.lang.token.Token;

import java.util.Arrays;
import java.util.List;

public class WhileStatement implements IStatement{
    private final Token whileToken;

    private final IExpressionStatement condition;

    private final IExpressionStatement body;

    public WhileStatement(Token whileToken, IExpressionStatement condition, IExpressionStatement body) {
        this.whileToken = whileToken;
        this.condition = condition;
        this.body = body;
    }

    @Override
    public List<IAstNode> getChildren() {
        return Arrays.asList(condition, body);
    }

    public IExpressionStatement getCondition(){
        return condition;
    }

    public IExpressionStatement getLoopBody(){
        return body;
    }

    @Override
    public String toString(){
        return whileToken.getLiteral();
    }
}
