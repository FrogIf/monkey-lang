package sch.frog.monkey.lang;

import sch.frog.monkey.lang.lexer.Lexer;
import sch.frog.monkey.lang.lexer.StringScriptStream;
import sch.frog.monkey.lang.token.Token;
import sch.frog.monkey.lang.token.TokenType;

import java.util.Scanner;

public class LexerTest {

    public static void main(String[] args){
        // "let ab = 12 + 34.34_5; if(a >= b){ println(\"asfd\"); }"
        Scanner sc = new Scanner(System.in);
        while(true){
            String line = sc.nextLine();
            if("exit".equals(line)){ break; }
            Lexer lexer = new Lexer(new StringScriptStream(line));
            System.out.println("----------------");
            Token t;
            while((t = lexer.nextToken()).getType() != TokenType.EOF){
                System.out.println(t);
            }
            System.out.println("----------------");
        }
    }

}
