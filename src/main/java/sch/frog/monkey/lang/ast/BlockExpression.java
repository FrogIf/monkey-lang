package sch.frog.monkey.lang.ast;

import sch.frog.monkey.lang.token.Token;

import java.util.ArrayList;
import java.util.List;

public class BlockExpression implements IExpressionStatement {

    private final Token begin;

    private final Token end;

    private final List<IStatement> statements;

    public BlockExpression(Token begin, List<IStatement> statements, Token end) {
        this.begin = begin;
        this.end = end;
        this.statements = statements;
    }

    @Override
    public List<IAstNode> getChildren() {
        return new ArrayList<>(statements);
    }

    @Override
    public String toString(){
        return "{}";
    }
}
