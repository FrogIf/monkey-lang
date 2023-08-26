package sch.frog.monkey.lang.lexer;

public interface IScriptStream {

    char EOF = 0;

    /**
     * 脚本标识, 如果有多个脚本, 用于区分
     */
    String scriptId();

    /**
     * 当前字符
     */
    char current();

    /**
     * 预看一个字符
     */
    char peek();

    /**
     * 读取下一个字符
     */
    void next();

    /**
     * 当前字符位置
     */
    int position();

}
