package sch.frog.monkey.lang.lexer;

public class StringScriptStream implements IScriptStream{

    private final String scriptString;

    private final int len;

    private int pos;

    private int nextPos;

    public StringScriptStream(String scriptString) {
        this.scriptString = scriptString;
        this.len = scriptString.length();
        this.next();
    }

    private char ch = 0;

    @Override
    public String scriptId() {
        return "input";
    }

    @Override
    public char current() {
        return ch;
    }

    @Override
    public char peek() {
        if(nextPos < len){
            return scriptString.charAt(nextPos);
        }else{
            return 0;
        }
    }

    @Override
    public void next() {
        if(nextPos < len){
            ch = scriptString.charAt(nextPos);
            pos = nextPos;
            nextPos++;
        }else{
            ch = 0;
        }
    }

    @Override
    public int position() {
        return pos;
    }
}
