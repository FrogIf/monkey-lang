package sch.frog.monkey.lang;

import sch.frog.monkey.lang.ast.Program;
import sch.frog.monkey.lang.evaluator.Evaluator;
import sch.frog.monkey.lang.exception.EvalException;
import sch.frog.monkey.lang.exception.ExpressionException;
import sch.frog.monkey.lang.lexer.Lexer;
import sch.frog.monkey.lang.lexer.StringScriptStream;
import sch.frog.monkey.lang.parser.Parser;
import sch.frog.monkey.lang.token.Token;
import sch.frog.monkey.lang.token.TokenType;
import sch.frog.monkey.lang.util.AstTreeUtil;
import sch.frog.monkey.lang.val.Value;

import java.util.Scanner;

public class LangTest {


    public static void main(String[] args){
        // "let ab = 12 + 34.34_5; if(a >= b){ println(\"asfd\"); }"
        Scanner sc = new Scanner(System.in);
        while(true){
            String line = sc.nextLine();
            if("exit".equals(line)){ break; }
            Lexer lexer = new Lexer(new StringScriptStream(line));
            Parser parser = new Parser(lexer);
            Program program = null;
            try {
                program = parser.parse();
                Value val = new Evaluator(program).eval();
                System.out.println("result : " + val.inspect());
            } catch (ExpressionException e) {
                System.out.println("error occur at " + e.getPos());
                printTokens(line);
                e.printStackTrace();
            } catch (EvalException e) {
                System.out.println(AstTreeUtil.generateTree(program));
                e.printStackTrace();
            } catch (Exception e){
                System.out.println(AstTreeUtil.generateTree(program));
                e.printStackTrace();
            }
        }
    }

    private static void printTokens(String exp){
        Lexer lexer = new Lexer(new StringScriptStream(exp));
        Token t;
        while((t = lexer.nextToken()).getType() != TokenType.EOF && t.getType() != TokenType.ILLEGAL){
            System.out.println(t);
        }
    }

}
