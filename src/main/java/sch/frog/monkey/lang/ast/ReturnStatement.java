package sch.frog.monkey.lang.ast;

import sch.frog.monkey.lang.token.Token;

import java.util.Arrays;
import java.util.List;

public class ReturnStatement implements IStatement {

    private Token returnToken;

    private IExpressionStatement returnValue;

    public ReturnStatement(Token returnToken, IExpressionStatement returnValue) {
        this.returnToken = returnToken;
        this.returnValue = returnValue;
        if(this.returnToken == null){
            throw new IllegalArgumentException("return token must not null");
        }
        if(this.returnValue == null){
            throw new IllegalArgumentException("return value expression must not null");
        }
    }

    @Override
    public List<IAstNode> getChildren() {
        return Arrays.asList(returnValue);
    }

    public IExpressionStatement returnExp(){
        return returnValue;
    }

    public String toString(){
        return returnToken.getLiteral();
    }
}
