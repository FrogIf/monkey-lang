package sch.frog.monkey.lang.ast;

import sch.frog.monkey.lang.token.Token;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FunctionDefineExpression implements IExpressionStatement{

    private final Token funToken;

    private final List<Identifier> arguments;

    private final IExpressionStatement body;

    public FunctionDefineExpression(Token funToken, List<Identifier> arguments, IExpressionStatement body) {
        this.funToken = funToken;
        this.arguments = arguments == null ? Collections.emptyList() : arguments;
        this.body = body;
    }

    @Override
    public List<IAstNode> getChildren() {
        return Arrays.asList(new IAstNode() {
            @Override
            public List<IAstNode> getChildren() {
                return new ArrayList<>(arguments);
            }

            @Override
            public String toString(){
                return "()";
            }
        }, body);
    }

    @Override
    public String toString(){
        return funToken.getLiteral();
    }

    public List<Identifier> getArguments(){
        return arguments;
    }

    public IExpressionStatement getBody(){
        return body;
    }
}
