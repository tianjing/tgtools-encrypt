package com.github.tianjing.tgtools.encrypt;

import tgtools.exceptions.APPErrorException;

/**
 * @author 田径
 * @create 2019-08-30 8:46
 * @desc
 **/
public interface Encrypter {
    final static String ENCODE = "UTF-8";

    /**
     * 获取密钥
     */
    String getKey() throws APPErrorException;

    /**
     * 设置密钥
     *
     * @param pPassword
     */
    void setKey(String pPassword) throws APPErrorException;

    /**
     * 加密 将字符串进行加密
     * 通常将密文字节通过BASE64转成字符串
     *
     * @param pRaw
     * @return
     */
    String encode(CharSequence pRaw);

    /**
     * 加密 将字符串进行加密
     * 通常将密文字节通过BASE64转成字符串
     *
     * @param pRaw
     * @return
     */
    byte[] encode(byte[] pRaw) throws APPErrorException;



    /**
     * 解密 将字符串进行解密
     * 通常将密文字节通过BASE64转成字符串
     *
     * @param pEncode
     * @return
     */
    String decode(CharSequence pEncode) throws APPErrorException;

    /**
     * 解密
     *
     * @param pRaw
     * @return
     */
    byte[] decode(byte[] pRaw) throws APPErrorException;

    /**
     * 对比 原文和密文是否一致
     *
     * @param pRaw
     * @param pEncode
     * @return
     */
    boolean matches(CharSequence pRaw, String pEncode);

}
