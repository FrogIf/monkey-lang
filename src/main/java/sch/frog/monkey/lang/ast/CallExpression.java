package sch.frog.monkey.lang.ast;

import sch.frog.monkey.lang.token.Token;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CallExpression extends InfixExpression{

    private final List<IExpressionStatement> arguments;

    public CallExpression(IExpressionStatement left, Token infix, List<IExpressionStatement> arguments) {
        super(left, infix, null);
        this.arguments = arguments;
    }

    @Override
    public List<ITreeNode> getChildren() {
        return Arrays.asList(left, new ITreeNode() {
            @Override
            public List<ITreeNode> getChildren() {
                return new ArrayList<>(arguments);
            }

            @Override
            public String toString(){
                return "args";
            }
        });
    }

    @Override
    public String toString() {
        return "call";
    }
}
