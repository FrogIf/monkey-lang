package sch.frog.monkey.lang.ast;

import sch.frog.monkey.lang.token.Token;

import java.util.ArrayList;
import java.util.List;

public class ArrayExpression implements IExpressionStatement{

    private Token arrToken;

    private final List<IExpressionStatement> elements;

    public ArrayExpression(Token arrToken, List<IExpressionStatement> elements) {
        this.elements = elements;
    }

    @Override
    public List<IAstNode> getChildren() {
        return new ArrayList<>(elements);
    }

    public List<IExpressionStatement> getElements(){
        return elements;
    }

    @Override
    public String toString(){
        return "array";
    }
}
