package sch.frog.monkey.lang.ast;

import sch.frog.monkey.lang.token.Token;

import java.util.List;

public class SignalStatement implements IStatement{

    private final Token signalToken;

    public SignalStatement(Token signalToken) {
        this.signalToken = signalToken;
    }

    @Override
    public List<IAstNode> getChildren() {
        return null;
    }

    @Override
    public String toString(){
        return signalToken.getLiteral();
    }

    public String literal(){
        return signalToken.getLiteral();
    }
}
