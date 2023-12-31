package sch.frog.monkey.lang.ast;

import sch.frog.monkey.lang.token.Token;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IfElseStatement implements IStatement {

    private final Token ifToken;

    private final ConditionExpPair ifPair;

    private final List<ConditionExpPair> elifList;


    private final IExpressionStatement elseExp;

    public IfElseStatement(Token ifToken, ConditionExpPair ifPair, List<ConditionExpPair> elifList, IExpressionStatement elseExp) {
        this.ifToken = ifToken;
        this.ifPair = ifPair;
        this.elseExp = elseExp;
        this.elifList = elifList;
    }

    @Override
    public List<IAstNode> getChildren() {
        List<IAstNode> nodes = new ArrayList<>();
        nodes.add(ifPair);
        if(elifList != null && !elifList.isEmpty()){
            nodes.addAll(elifList);
        }
        if(elseExp != null){
            nodes.add(elseExp);
        }
        return nodes;
    }

    @Override
    public String toString(){
        return ifToken.getLiteral();
    }

    public List<ConditionExpPair> getConditionExpPair(){
        List<ConditionExpPair> nodes = new ArrayList<>();
        nodes.add(ifPair);
        if(elifList != null && !elifList.isEmpty()){
            nodes.addAll(elifList);
        }
        return nodes;
    }

    public IExpressionStatement getElseExp(){
        return this.elseExp;
    }

    public static class ConditionExpPair implements IExpressionStatement {
        private IExpressionStatement condition;
        private IExpressionStatement expression;

        public ConditionExpPair(IExpressionStatement condition, IExpressionStatement expression) {
            this.condition = condition;
            this.expression = expression;
        }

        @Override
        public List<IAstNode> getChildren() {
            return Arrays.asList(condition, expression);
        }

        public IExpressionStatement getCondition(){
            return condition;
        }

        public IExpressionStatement getExpression(){
            return expression;
        }

        @Override
        public String toString(){
            return "condition-exp";
        }
    }
}
