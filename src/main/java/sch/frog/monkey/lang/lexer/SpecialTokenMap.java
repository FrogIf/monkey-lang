package sch.frog.monkey.lang.lexer;

import sch.frog.monkey.lang.token.TokenType;

import java.util.ArrayList;

public class SpecialTokenMap {

    private final Node root = new Node();

    public void put(String key, TokenType value){
        root.add(key, value);
    }

    public MatchResult match(IScriptStream scriptStream){
        MatchResult result = root.startMatch(scriptStream);
        if(result.tokenType == null){ result.tokenType = TokenType.ILLEGAL; }
        return result;
    }

    private static class Node{
        private char ch;

        private TokenType tokenType;

        private final ArrayList<Node> children = new ArrayList<>();

        private void add(String key, TokenType data){
            int index = 0;
            ArrayList<Node> cursorChildren = children;
            Node targetNode = null;
            int len = key.length();
            while(true){
                char ch = key.charAt(index);
                for (Node child : cursorChildren) {
                    if(child.ch == ch){
                        targetNode = child;
                        break;
                    }
                }
                if(targetNode == null){
                    targetNode = new Node();
                    targetNode.ch = ch;
                    cursorChildren.add(targetNode);
                }
                cursorChildren = targetNode.children;
                index++;
                if(index == len){
                    targetNode.tokenType = data;
                    break;
                }
                targetNode = null;
            }
        }

        public MatchResult startMatch(IScriptStream scriptStream){
            MatchResult result = new MatchResult();
            char ch = scriptStream.current();
            result.sb.append(ch);
            Node cursor = null;
            for (Node n : children) {
                if(n.ch == ch){
                    cursor = n;
                    break;
                }
            }
            if(cursor == null){
                return result;
            }
            cursor.match(scriptStream, result);
            if(result.tokenType == null){
                result.tokenType = cursor.tokenType;
            }
            return result;
        }

        private void match(IScriptStream scriptStream, MatchResult result){
            char c = scriptStream.peek();
            if(c == IScriptStream.EOF){ return; }
            Node cursor = null;
            for (Node n : children) {
                if(n.ch == c){
                    cursor = n;
                    break;
                }
            }
            if(cursor != null){
                result.sb.append(c);
                scriptStream.next();
                cursor.match(scriptStream, result);
                if(result.tokenType == null){
                    result.tokenType = cursor.tokenType;
                }
            }
        }
    }

    public static class MatchResult {
        private final StringBuilder sb = new StringBuilder();
        private TokenType tokenType;

        public String getLiteral() {
            return sb.toString();
        }

        public TokenType getTokenType() {
            return tokenType;
        }
    }
}
