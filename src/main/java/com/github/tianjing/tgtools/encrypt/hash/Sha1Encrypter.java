package com.github.tianjing.tgtools.encrypt.hash;

import tgtools.exceptions.APPErrorException;
import tgtools.exceptions.APPRuntimeException;
import tgtools.util.StringUtil;

import java.security.MessageDigest;

/**
 * @author 田径
 * @create 2019-09-01 18:29
 * @desc
 **/
public class Sha1Encrypter extends Md5Encrypter {
    private static final char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     * 转字符串
     *
     * @param bytes
     * @return
     */
    private static String getFormattedText(byte[] bytes) {
        int len = bytes.length;
        StringBuilder buf = new StringBuilder(len * 2);
        // 把密文转换成十六进制的字符串形式
        for (int j = 0; j < len; j++) {
            buf.append(HEX_DIGITS[(bytes[j] >> 4) & 0x0f]);
            buf.append(HEX_DIGITS[bytes[j] & 0x0f]);
        }
        return buf.toString();
    }

    protected String getAlgorithm() {
        return "SHA1";
    }

    @Override
    public String encode(CharSequence pRaw)  {
        if (null == pRaw) {
            return null;
        }

        try {
            return getFormattedText(encode(pRaw.toString().getBytes(ENCODE)));
        } catch (Exception e) {
            if (e instanceof APPRuntimeException) {
                throw (APPRuntimeException) e;
            }

            throw new APPRuntimeException("encode 出错！原因： " + e.toString(), e);
        }
    }

    @Override
    public byte[] encode(byte[] pRaw) throws APPErrorException {
        if (null == pRaw) {
            return null;
        }
        try {
            //信息摘要器 算法名称
            MessageDigest md = MessageDigest.getInstance(getAlgorithm());
            //把字符串转为字节数组
            byte[] b = pRaw;
            //使用指定的字节来更新我们的摘要
            md.update(b);
            //获取密文  （完成摘要计算）
            return md.digest();
        } catch (Exception e) {
            throw new APPErrorException("encode 出错！原因： " + e.toString(), e);
        }
    }

    @Override
    public boolean matches(CharSequence pRaw, String pEncode) {
        return null != pRaw && StringUtil.isNotEmpty(pRaw.toString())
                && StringUtil.isNotEmpty(pEncode) && pEncode.equals(encode(pRaw));
    }

}
