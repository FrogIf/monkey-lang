package sch.frog.monkey.lang.ast;

import sch.frog.monkey.lang.token.Token;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class MapExpression implements IExpressionStatement {

    private final Map<String, IExpressionStatement> map;

    private final Token mapToken;

    public MapExpression(Token mapToken, Map<String, IExpressionStatement> map) {
        this.map = map;
        this.mapToken = mapToken;
    }

    public Map<String, IExpressionStatement> getMap(){
        return map;
    }

    @Override
    public List<IAstNode> getChildren() {
        ArrayList<IAstNode> nodes = new ArrayList<>();
        for (Map.Entry<String, IExpressionStatement> entry : map.entrySet()) {
            nodes.add(new Pair(entry.getKey(), entry.getValue()));
        }
        return nodes;
    }

    @Override
    public String toString(){
        return "map";
    }

    private static class Pair implements IAstNode{

        private String key;

        private IExpressionStatement value;

        public Pair(String key, IExpressionStatement value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public List<IAstNode> getChildren() {
            return Arrays.asList(new IAstNode() {
                @Override
                public List<IAstNode> getChildren() {
                    return null;
                }

                @Override
                public String toString() {
                    return key;
                }
            }, value);
        }

        @Override
        public String toString() {
            return ":";
        }
    }
}
