package sch.frog.monkey.lang.token;

public enum TokenType {

    ASSIGN, // =
    SEMICOLON, // ;
    LPAREN, // (
    RPAREN, // )
    COMMA, // ,
    PLUS, // +
    LBRACE, // {
    RBRACE, // }
    EOF, // 0 终止(end of file)
    ILLEGAL, // 非法token
    IDENT, // 标识符
    NUMBER, // 数字
    STRING, // 字符串
    LET, // 关键字let
    FUNCTION, // 关键字fun
    MINUS, // -
    BANG, // !
    ASTERISK, // *
    SLASH, // /
    LT, // <
    GT, // >
    EQ, // ==
    NOT_EQ, // !=
    LTE, // <=
    GTE, // >=


}
