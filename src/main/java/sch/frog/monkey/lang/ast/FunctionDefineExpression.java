package sch.frog.monkey.lang.ast;

import sch.frog.monkey.lang.token.Token;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FunctionDefineExpression implements IExpressionStatement{

    private final Token funToken;

    private final List<Identifier> arguments;

    private final IExpressionStatement body;

    public FunctionDefineExpression(Token funToken, List<Identifier> arguments, IExpressionStatement body) {
        this.funToken = funToken;
        this.arguments = arguments;
        this.body = body;
    }

    @Override
    public List<ITreeNode> getChildren() {
        return Arrays.asList(new ITreeNode() {
            @Override
            public List<ITreeNode> getChildren() {
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
}
