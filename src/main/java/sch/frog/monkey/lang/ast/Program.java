package sch.frog.monkey.lang.ast;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Program implements IAstNode {

    private final LinkedList<IStatement> statements = new LinkedList<>();

    public void addStatement(IStatement statement){
        statements.add(statement);
    }

    @Override
    public List<IAstNode> getChildren() {
        return new ArrayList<>(statements);
    }

    @Override
    public String toString() {
        return "program";
    }
}
