package sch.frog.monkey.lang.ast;

import sch.frog.monkey.lang.token.Token;

public class MapIndexExpression extends InfixExpression{

    public MapIndexExpression(IExpressionStatement left, Token infix, IExpressionStatement right) {
        super(left, infix, right);
    }

    @Override
    public String toString(){
        return "mapIndex";
    }

}
