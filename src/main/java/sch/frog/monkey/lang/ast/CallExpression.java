package sch.frog.monkey.lang.ast;

import sch.frog.monkey.lang.token.Token;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CallExpression extends InfixExpression{

    private final List<IExpressionStatement> arguments;

    public CallExpression(IExpressionStatement left, Token infix, List<IExpressionStatement> arguments) {
        super(left, infix, null);
        this.arguments = arguments == null ? Collections.emptyList() : arguments;
    }

    @Override
    public List<IAstNode> getChildren() {
        return Arrays.asList(left, new IAstNode() {
            @Override
            public List<IAstNode> getChildren() {
                return new ArrayList<>(arguments);
            }

            @Override
            public String toString(){
                return "args";
            }
        });
    }

    public List<IExpressionStatement> getRealArgExps(){
        return arguments;
    }

    @Override
    public String toString() {
        return "call";
    }
}
