package com.github.tianjing.tgtools.encrypt.sm.sm4;

/**
 * @author 田径
 * @create 2019-10-23 11:01
 * @desc
 * https://blog.csdn.net/qq_38683138/article/details/99609068
 **/
public class SM4_Context {

    public int mode;

    public int[] sk;

    public boolean isPadding;

    public SM4_Context() {
        this.mode = 1;
        this.isPadding = true;
        this.sk = new int[32];

    }
}