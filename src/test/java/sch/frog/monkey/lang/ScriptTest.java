package sch.frog.monkey.lang;

import sch.frog.monkey.lang.app.MonkeyLangApp;
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class ScriptTest {


    public static void main(String[] args) throws IOException {
        File file = new File("script.mkl");
        try(
                BufferedReader reader = new BufferedReader(new FileReader(file))
                ){
            StringBuilder sb = new StringBuilder();
            String line;
            while((line = reader.readLine()) != null){
                sb.append(line).append('\n');
            }
            line = sb.toString();
            MonkeyLangApp.init();
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
        while((t = lexer.nextToken()).getType() != TokenType.EOF){
            System.out.println(t);
        }
    }

}
