package sch.frog.monkey.lang.parser;

import sch.frog.monkey.lang.ast.BOOL;
import sch.frog.monkey.lang.ast.BlockExpression;
import sch.frog.monkey.lang.ast.CallExpression;
import sch.frog.monkey.lang.ast.FunctionDefineExpression;
import sch.frog.monkey.lang.ast.GroupExpression;
import sch.frog.monkey.lang.ast.IExpressionStatement;
import sch.frog.monkey.lang.ast.IStatement;
import sch.frog.monkey.lang.ast.Identifier;
import sch.frog.monkey.lang.ast.IfExpression;
import sch.frog.monkey.lang.ast.InfixExpression;
import sch.frog.monkey.lang.ast.LetStatement;
import sch.frog.monkey.lang.ast.Number;
import sch.frog.monkey.lang.ast.PrefixExpression;
import sch.frog.monkey.lang.ast.Program;
import sch.frog.monkey.lang.ast.ReturnStatement;
import sch.frog.monkey.lang.ast.StringExp;
import sch.frog.monkey.lang.exception.ExpressionException;
import sch.frog.monkey.lang.lexer.Lexer;
import sch.frog.monkey.lang.token.Token;
import sch.frog.monkey.lang.token.TokenType;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Parser {

    public static final HashMap<TokenType, Integer> precedenceMap = new HashMap<>();

    static{
        precedenceMap.put(TokenType.EQ, 0);
        precedenceMap.put(TokenType.NOT_EQ, 0);
        precedenceMap.put(TokenType.GT, 1);
        precedenceMap.put(TokenType.GTE, 1);
        precedenceMap.put(TokenType.LT, 1);
        precedenceMap.put(TokenType.LTE, 1);
        precedenceMap.put(TokenType.PLUS, 2);
        precedenceMap.put(TokenType.MINUS, 2);
        precedenceMap.put(TokenType.ASTERISK, 3);
        precedenceMap.put(TokenType.SLASH, 3);
        precedenceMap.put(TokenType.LPAREN, 4);
    }

    private Token currentToken;

    private Token peekToken;

    private final Lexer lexer;

    public Parser(Lexer lexer) {
        this.lexer = lexer;
        next();
        next();
    }

    public Program parse() throws ExpressionException {
        Program program = new Program();
        TokenType tt;
        while((tt = this.currentToken.getType()) != TokenType.EOF){
            IStatement statement;
            if(tt == TokenType.LET){
                statement = parseLetStatement(this);
            }else if(tt == TokenType.RETURN){
                statement = parseReturnStatement(this);
            }else if(tt == TokenType.SEMICOLON){
                this.next();
                continue;
            }else{
                statement = parseExpressionStatement(this);
            }
            program.addStatement(statement);
        }
        return program;
    }

    public static IExpressionStatement parseExpressionStatement(Parser parser) throws ExpressionException {
        return parseExpressionStatement(parser, -1);
    }

    public static IExpressionStatement parseExpressionStatement(Parser parser, int precedence) throws ExpressionException {
        IExpressionStatement exp = parsePrefixExpression(parser);
        if(exp == null){
            throw new ExpressionException("expression parse failed", parser.current().getPos());
        }

        TokenType type;
        Integer p;
        while((type = parser.current().getType()) != TokenType.SEMICOLON
                && type != TokenType.EOF
                && (p = precedenceMap.get(type)) != null && p > precedence){
            exp = parseInfixExpression(parser, exp);
        }
        return exp;
    }

    public static IExpressionStatement parseInfixExpression(Parser parser, IExpressionStatement left) throws ExpressionException {
        Token current = parser.current();
        TokenType type = current.getType();
        switch (type){
            case EQ:
            case NOT_EQ:
            case LT:
            case LTE:
            case GT:
            case GTE:
            case PLUS:
            case MINUS:
            case ASTERISK:
            case SLASH:
                parser.next();
                return new InfixExpression(left, current, parseExpressionStatement(parser, precedenceMap.get(type)));
            case LPAREN:
                return new CallExpression(left, current, parseCallArguments(parser));
        }
        return null;
    }

    public static IExpressionStatement parsePrefixExpression(Parser parser) throws ExpressionException {
        Token current = parser.current();
        TokenType type = current.getType();
        switch (type){
            case IDENT:
                return parseIdentifier(parser);
            case NUMBER:
                return parseNumber(parser);
            case BOOL:
                return parseBool(parser);
            case STRING:
                return parseString(parser);
            case LPAREN:
                return parseGroup(parser);
            case MINUS:
            case BANG:
                parser.next();
                return new PrefixExpression(current, parseExpressionStatement(parser));
            case IF:
                return parseIfElse(parser);
            case FUNCTION:
                return parseFunDefine(parser);
            default:
                return null;
        }
    }

    public static List<IExpressionStatement> parseCallArguments(Parser parser) throws ExpressionException{
        Token begin = parser.current();
        if(begin.getType() != TokenType.LPAREN){
            throw new ExpressionException("call arguments incorrect", begin.getPos());
        }
        parser.next();
        LinkedList<IExpressionStatement> arguments = new LinkedList<>();
        if(parser.current().getType() == TokenType.RPAREN){
            parser.next();
            return arguments;
        }

        arguments.add(parseExpressionStatement(parser));

        TokenType tt;
        while((tt = parser.current().getType()) == TokenType.COMMA){
            parser.next();
            arguments.add(parseExpressionStatement(parser));
        }
        if(tt != TokenType.RPAREN){
            throw new ExpressionException("call not close", parser.current().getPos());
        }
        parser.next();
        return arguments;
    }

    public static IExpressionStatement parseFunDefine(Parser parser) throws ExpressionException{
        Token funToken = parser.current();
        if(funToken.getType() != TokenType.FUNCTION){
            throw new ExpressionException("function define incorrect", funToken.getPos());
        }
        parser.next();

        List<Identifier> paramList = parseParamList(parser);

        IExpressionStatement body = parseBlockExpression(parser);

        return new FunctionDefineExpression(funToken, paramList, body);
    }

    public static List<Identifier> parseParamList(Parser parser) throws ExpressionException {
        if(parser.current().getType() != TokenType.LPAREN){
            throw new ExpressionException("param list is incorrect", parser.current().getPos());
        }
        parser.next();
        LinkedList<Identifier> arguments = new LinkedList<>();
        if(parser.current().getType() == TokenType.RPAREN){
            parser.next();
            return arguments;
        }
        arguments.add(parseIdentifier(parser));
        TokenType tt;
        while((tt = parser.current().getType()) == TokenType.COMMA){
            parser.next();
            arguments.add(parseIdentifier(parser));
        }
        if(tt != TokenType.RPAREN){
            throw new ExpressionException("param list not close", parser.current().getPos());
        }else{
            parser.next();
        }
        return arguments;
    }

    public static IExpressionStatement parseIfElse(Parser parser) throws ExpressionException{
        Token ifToken = parser.current();
        IfExpression.ConditionExpPair ifPair = parseIfConditionExpPair(parser);

        IExpressionStatement elseExp = null;
        LinkedList<IfExpression.ConditionExpPair> elifList = new LinkedList<>();
        while(parser.current().getType() == TokenType.ELSE){
            if(parser.peek().getType() == TokenType.IF){    // else if
                parser.next();
                IfExpression.ConditionExpPair elif = parseIfConditionExpPair(parser);
                elifList.add(elif);
            }else{  // else
                parser.next();
                elseExp = parseBlockExpression(parser);
                break;
            }
        }

        return new IfExpression(ifToken, ifPair, elifList, elseExp);
    }

    private static IfExpression.ConditionExpPair parseIfConditionExpPair(Parser parser) throws ExpressionException{
        Token ifToken = parser.current();
        if(ifToken.getType() != TokenType.IF){
            throw new ExpressionException("if expression incorrect", ifToken.getPos());
        }
        parser.next();
        if(parser.current().getType() != TokenType.LPAREN){
            throw new ExpressionException("if condition incorrect", parser.current().getPos());
        }else{
            parser.next();
        }
        IExpressionStatement condition = parseExpressionStatement(parser);
        if(parser.current().getType() != TokenType.RPAREN){
            throw new ExpressionException("if condition not close", parser.current().getPos());
        }else{
            parser.next();
        }
        IExpressionStatement ifExp = parseBlockExpression(parser);
        return new IfExpression.ConditionExpPair(condition, ifExp);
    }

    public static IExpressionStatement parseBlockExpression(Parser parser) throws ExpressionException {
        Token begin = parser.current();
        if(begin.getType() != TokenType.LBRACE){
            throw new ExpressionException("block not begin with left brace", begin.getPos());
        }
        parser.next();
        TokenType tt;
        LinkedList<IStatement> statements = new LinkedList<>();
        while((tt = parser.current().getType()) != TokenType.RBRACE && tt != TokenType.EOF){
            IStatement statement;
            if(tt == TokenType.LET){
                statement = parseLetStatement(parser);
            }else if(tt == TokenType.RETURN){
                statement = parseReturnStatement(parser);
            }else if(tt == TokenType.SEMICOLON){
                parser.next();
                continue;
            }else{
                statement = parseExpressionStatement(parser);
            }
            statements.add(statement);
        }
        Token end = parser.current();
        if(end.getType() != TokenType.RBRACE){
            throw new ExpressionException("block not end with left brace", end.getPos());
        }
        parser.next();
        return new BlockExpression(begin, statements, end);
    }

    public static IExpressionStatement parseGroup(Parser parser) throws ExpressionException {
        Token current = parser.current();
        if(current.getType() != TokenType.LPAREN){
            throw new ExpressionException("group incorrect", current.getPos());
        }
        parser.next();
        IExpressionStatement statement = parseExpressionStatement(parser);
        if(parser.current().getType() != TokenType.RPAREN){
            throw new ExpressionException("group not close", current.getPos());
        }else{
            parser.next();
        }
        return new GroupExpression(current, statement);
    }

    public static BOOL parseBool(Parser parser) throws ExpressionException {
        Token current = parser.current();
        if(current.getType() != TokenType.BOOL){
            throw new ExpressionException("bool incorrect", current.getPos());
        }
        parser.next();
        return new BOOL(current);
    }

    public static StringExp parseString(Parser parser) throws ExpressionException {
        Token current = parser.current();
        if(current.getType() != TokenType.STRING){
            throw new ExpressionException("string incorrect", current.getPos());
        }
        parser.next();
        return new StringExp(current);
    }

    public static Number parseNumber(Parser parser) throws ExpressionException {
        Token current = parser.current();
        if(current.getType() != TokenType.NUMBER){
            throw new ExpressionException("number incorrect", current.getPos());
        }
        parser.next();
        return new Number(current);
    }

    public static Identifier parseIdentifier(Parser parser) throws ExpressionException {
        Token current = parser.current();
        if(current.getType() != TokenType.IDENT){
            throw new ExpressionException("identifier incorrect", current.getPos());
        }
        parser.next();
        return new Identifier(current);
    }

    public static ReturnStatement parseReturnStatement(Parser parser) throws ExpressionException {
        Token returnToken = parser.current();
        if(returnToken.getType() != TokenType.RETURN){
            throw new ExpressionException(returnToken.getLiteral() + " is not return token", returnToken.getPos());
        }
        parser.next();
        IExpressionStatement value = parseExpressionStatement(parser);
        return new ReturnStatement(returnToken, value);
    }

    public static LetStatement parseLetStatement(Parser parser) throws ExpressionException {
        Token letToken = parser.current();
        Token peek = parser.peek();
        if(peek.getType() != TokenType.IDENT){
            throw new ExpressionException("let statement incorrect", peek.getPos());
        }
        parser.next();
        Identifier name = parseIdentifier(parser);
        IExpressionStatement value = null;
        Token token = parser.current();
        if(token.getType() == TokenType.ASSIGN){
            parser.next();
            value = parseExpressionStatement(parser);
        }
        return new LetStatement(letToken, name, value);
    }

    public void next(){
        currentToken = peekToken;
        peekToken = lexer.nextToken();
    }

    public Token current() {
        return currentToken;
    }

    public Token peek() {
        return peekToken;
    }
}
