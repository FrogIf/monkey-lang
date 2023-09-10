package sch.frog.monkey.lang.ast;

import io.github.frogif.calculator.number.impl.RationalNumber;
import sch.frog.monkey.lang.token.Token;
import sch.frog.monkey.lang.val.Value;

import java.util.List;

public final class Number implements IExpressionStatement {

    private final Token num;

    public Number(Token num){
        this.num = num;
    }

    @Override
    public List<IAstNode> getChildren() {
        return null;
    }

    @Override
    public String toString() {
        return num.getLiteral();
    }

    public Value evaluate(){
        return new Value(RationalNumber.valueOf(num.getLiteral()));
    }
}
