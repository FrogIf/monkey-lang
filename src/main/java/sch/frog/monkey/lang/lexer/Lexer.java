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
        }else{
            SpecialTokenMap.MatchResult result = SPECIAL_TOKEN_MAP.match(scriptStream);
            token = new Token(scriptStream.scriptId(), start, result.getLiteral(), result.getTokenType());
        }
        scriptStream.next();
        return token;
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
        while(isLetter(ch = scriptStream.peek()) || isDigit(ch)){
            scriptStream.next();
            sb.append(ch);
        }
        return sb.toString();
    }

    private String readNumber(){
        StringBuilder sb = new StringBuilder();
        char ch = scriptStream.current();
        sb.append(ch);
        scriptStream.next();
        boolean dot = false;
        boolean loop = false;
        boolean digit;
        while(true){
            ch = scriptStream.peek();
            digit = isDigit(ch)
                    || (!dot && (dot = ch == '.'))  // 小数点
                    || (dot && !loop && (loop = ch == '_')); // 循环节
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