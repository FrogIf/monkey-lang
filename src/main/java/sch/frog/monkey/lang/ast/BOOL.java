package sch.frog.monkey.lang.ast;

import sch.frog.monkey.lang.token.Token;

import java.util.List;

public class BOOL implements IExpressionStatement {

    private final Token value;

    public BOOL(Token t){
        value = t;
    }

    @Override
    public List<ITreeNode> getChildren() {
        return null;
    }

    @Override
    public String toString(){
        return value.getLiteral();
    }


}
