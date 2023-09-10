package sch.frog.monkey.lang.ast;

import sch.frog.monkey.lang.token.Token;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class LetStatement implements IStatement {

    private final Token letToken;

    private final Identifier name;

    private final IExpressionStatement value;

    public LetStatement(Token letToken, Identifier name, IExpressionStatement value) {
        this.letToken = letToken;
        this.name = name;
        this.value = value;
    }

    @Override
    public List<IAstNode> getChildren() {
        if(value == null){
            return Collections.singletonList(name);
        }else{
            return Arrays.asList(name, value);
        }
    }

    public String getVarName(){
        return name.literal();
    }

    public IExpressionStatement getValueExp(){
        return value;
    }

    @Override
    public String toString() {
        return letToken.getLiteral();
    }
}
