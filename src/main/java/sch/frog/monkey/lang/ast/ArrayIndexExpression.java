package sch.frog.monkey.lang.ast;

import sch.frog.monkey.lang.token.Token;

public class ArrayIndexExpression extends InfixExpression{

    public ArrayIndexExpression(IExpressionStatement left, Token infix, IExpressionStatement index) {
        super(left, infix, index);
    }

    @Override
    public String toString() {
        return "arrayIndex";
    }
}
