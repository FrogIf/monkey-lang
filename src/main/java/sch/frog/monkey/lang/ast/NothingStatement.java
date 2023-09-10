package sch.frog.monkey.lang.ast;

import sch.frog.monkey.lang.token.Token;

import java.util.List;

public class NothingStatement implements IStatement{

    private final Token comment;

    public NothingStatement(Token comment) {
        this.comment = comment;
    }

    @Override
    public List<IAstNode> getChildren() {
        return null;
    }
}
