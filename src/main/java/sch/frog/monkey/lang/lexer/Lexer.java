package sch.frog.monkey.lang.lexer;

import sch.frog.monkey.lang.token.Token;
import sch.frog.monkey.lang.token.TokenType;

import java.util.HashMap;
import java.util.Map;

/**
 * 词法分析器
 */
public class Lexer {

    private static final Map<String, TokenType> SPECIAL_IDENTIFIER_MAP = new HashMap<>();

    private static final SpecialTokenMap SPECIAL_TOKEN_MAP = new SpecialTokenMap();

    static {
        SPECIAL_IDENTIFIER_MAP.put("let", TokenType.LET);
        SPECIAL_IDENTIFIER_MAP.put("fun", TokenType.FUNCTION);
        SPECIAL_IDENTIFIER_MAP.put("return", TokenType.RETURN);
        SPECIAL_IDENTIFIER_MAP.put("true", TokenType.BOOL);
        SPECIAL_IDENTIFIER_MAP.put("false", TokenType.BOOL);
        SPECIAL_IDENTIFIER_MAP.put("if", TokenType.IF);
        SPECIAL_IDENTIFIER_MAP.put("else", TokenType.ELSE);
        SPECIAL_IDENTIFIER_MAP.put("while", TokenType.WHILE);
        SPECIAL_IDENTIFIER_MAP.put("null", TokenType.NULL);
        SPECIAL_IDENTIFIER_MAP.put("break", TokenType.BREAK);
        SPECIAL_IDENTIFIER_MAP.put("continue", TokenType.CONTINUE);

        SPECIAL_TOKEN_MAP.put("==", TokenType.EQ);
        SPECIAL_TOKEN_MAP.put("=", TokenType.ASSIGN);
        SPECIAL_TOKEN_MAP.put("!=", TokenType.NOT_EQ);
        SPECIAL_TOKEN_MAP.put(";", TokenType.SEMICOLON);
        SPECIAL_TOKEN_MAP.put("(", TokenType.LPAREN);
        SPECIAL_TOKEN_MAP.put(")", TokenType.RPAREN);
        SPECIAL_TOKEN_MAP.put(",", TokenType.COMMA);
        SPECIAL_TOKEN_MAP.put("{", TokenType.LBRACE);
        SPECIAL_TOKEN_MAP.put("}", TokenType.RBRACE);
        SPECIAL_TOKEN_MAP.put("!", TokenType.BANG);
        SPECIAL_TOKEN_MAP.put("+", TokenType.PLUS);
        SPECIAL_TOKEN_MAP.put("-", TokenType.MINUS);
        SPECIAL_TOKEN_MAP.put("*", TokenType.ASTERISK);
        SPECIAL_TOKEN_MAP.put("/", TokenType.SLASH);
        SPECIAL_TOKEN_MAP.put("<", TokenType.LT);
        SPECIAL_TOKEN_MAP.put(">", TokenType.GT);
        SPECIAL_TOKEN_MAP.put(">=", TokenType.GTE);
        SPECIAL_TOKEN_MAP.put("<=", TokenType.LTE);
        SPECIAL_TOKEN_MAP.put("[", TokenType.LBRACKET);
        SPECIAL_TOKEN_MAP.put("]", TokenType.RBRACKET);
        SPECIAL_TOKEN_MAP.put(":", TokenType.COLON);
        SPECIAL_TOKEN_MAP.put(".", TokenType.DOT);
    }

    private final IScriptStream scriptStream;

    public Lexer(IScriptStream scriptStream) {
        this.scriptStream = scriptStream;
    }

    public Token nextToken() {
        skipWhitespace();
        char ch = scriptStream.current();
        int start = scriptStream.position();
        Token token;
        if(ch == IScriptStream.EOF){
            token = new Token(scriptStream.scriptId(), start, "", TokenType.EOF);
        }else if(isLetter(ch)){
            String literal = readIdentifier();
            TokenType tokenType = SPECIAL_IDENTIFIER_MAP.get(literal);
            token = new Token(scriptStream.scriptId(), start, literal, tokenType == null ? TokenType.IDENT : tokenType);
        }else if(isDigit(ch)){
            String number = readNumber();
            token = new Token(scriptStream.scriptId(), start, number, TokenType.NUMBER);
        }else if(ch == '"'){
            String str = readString();
            token = new Token(scriptStream.scriptId(), start, str, TokenType.STRING);
        }else if(ch == '/'){
            String str = readComment();
            if(str == null){
                token = new Token(scriptStream.scriptId(), start, null, TokenType.ILLEGAL);
            }else{
                token = new Token(scriptStream.scriptId(), start, str, TokenType.COMMENT);
            }
        }else{
            SpecialTokenMap.MatchResult result = SPECIAL_TOKEN_MAP.match(scriptStream);
            token = new Token(scriptStream.scriptId(), start, result.getLiteral(), result.getTokenType());
        }
        return token;
    }

    private String readComment(){
        StringBuilder comment = new StringBuilder();
        char ch = scriptStream.current();
        comment.append(ch);
        scriptStream.next();
        ch = scriptStream.current();
        if(ch == '/'){
            comment.append(ch);
            scriptStream.next();
            while((ch = scriptStream.current()) != '\n' && ch != IScriptStream.EOF){
                comment.append(ch);
                scriptStream.next();
            }
        }else if(ch == '*'){
            comment.append(ch);
            scriptStream.next();
            while((ch = scriptStream.current()) != IScriptStream.EOF){
                if(ch == '*' && scriptStream.peek() == '/'){
                    scriptStream.next();
                    scriptStream.next();
                    comment.append("*/");
                    break;
                }
                comment.append(ch);
                scriptStream.next();
            }
        }else{
            return null;
        }
        return comment.toString();
    }

    private String readString(){
        StringBuilder result = new StringBuilder();
        char ch = scriptStream.current();
        result.append(ch);
        scriptStream.next();
        int escape = 0;
        while (true) {
            ch = scriptStream.current();
            if (ch == '"' && (escape & 1) == 0) {
                result.append('"');
                scriptStream.next();
                break;
            }else if(ch == IScriptStream.EOF){
                break;
            }else {
                result.append(ch);
                scriptStream.next();
            }
            if(ch == '\\'){
                escape++;
            }else{
                escape = 0;
            }
        }
        return result.toString();
    }

    private String readIdentifier(){
        StringBuilder sb = new StringBuilder();
        char ch = scriptStream.current();
        sb.append(ch);
        scriptStream.next();
        while(isLetter(ch = scriptStream.current()) || isDigit(ch)){
            sb.append(ch);
            scriptStream.next();
        }
        return sb.toString();
    }

    private String readNumber(){
        StringBuilder sb = new StringBuilder();
        char ch = scriptStream.current();
        sb.append(ch);
        scriptStream.next();
        boolean dot = false;
        boolean digit;
        while(true){
            ch = scriptStream.current();
            digit = isDigit(ch)
                    || (!dot && (dot = ch == '.'));  // 小数点
//            digit = isDigit(ch)
//                    || (!dot && (dot = ch == '.'))  // 小数点
//                    || (dot && !loop && (loop = ch == '_')); // 循环节
            if(!digit){ break; }
            scriptStream.next();
            sb.append(ch);
        }
        return sb.toString();
    }

    private boolean isDigit(char ch){
        return ch >= '0' && ch <= '9';
    }

    private boolean isLetter(char ch){
        return (ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z') || ch == '_';
    }

    private void skipWhitespace(){
        while(isWhitespace(scriptStream.current())){
            scriptStream.next();
        }
    }

    private boolean isWhitespace(char ch){
        return ch == ' ' || ch == '\t' || ch == '\n' || ch == '\r';
    }
}
