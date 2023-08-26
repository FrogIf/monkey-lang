package sch.frog.monkey.lang.token;

public class Token {

    private final int pos;

    private final String literal;

    private final TokenType tokenType;

    private final String scriptId; // 文件标识

    public Token(String scriptId, int pos, String literal, TokenType tokenType) {
        this.scriptId = scriptId;
        this.pos = pos;
        this.literal = literal;
        this.tokenType = tokenType;
    }

    public int getPos() {
        return pos;
    }

    public String getLiteral() {
        return literal;
    }

    public TokenType getType() {
        return tokenType;
    }

    public String getScriptId() {
        return scriptId;
    }

    @Override
    public String toString() {
        return literal + "("+pos + ", " + tokenType.name() + ", " + scriptId + ")";
    }
}
