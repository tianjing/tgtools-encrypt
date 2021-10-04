package com.github.tianjing.tgtools.encrypt;

import org.apache.commons.codec.binary.Base64;
import tgtools.exceptions.APPErrorException;
import tgtools.util.StringUtil;

/**
 * @author 田径
 * @create 2019-08-30 8:46
 * @desc
 **/
public abstract class AbstractEncrypter implements Encrypter{
    /**
     * 对比 原文和密文是否一致
     *
     * @param pRaw
     * @param pEncode
     * @return
     */
    @Override
    public boolean matches(CharSequence pRaw, String pEncode)  {
        return null != pRaw && StringUtil.isNotEmpty(pRaw.toString())
                && StringUtil.isNotEmpty(pEncode) && pEncode.equals(encode(pRaw));
    }


    /**
     * 获取密钥的字节
     *
     * @throws APPErrorException
     */
    public byte[] getKeyBytes() throws APPErrorException {
        try {
            return getKey().getBytes(ENCODE);
        } catch (Exception e) {
            throw new APPErrorException("encode 出错；原因：" + e.toString(), e);
        }
    }

    /**
     * base 64 encode
     *
     * @param bytes 待编码的byte[]
     * @return 编码后的base 64 code
     */
    public String base64ToString(byte[] bytes) {
        return Base64.encodeBase64String(bytes);
    }

    /**
     * base 64 decode
     *
     * @param base64Code 待解码的base 64 code
     * @return 解码后的byte[]
     * @throws Exception
     */
    public byte[] base64FromString(String base64Code) throws Exception {
        return StringUtil.isNullOrEmpty(base64Code) ? null : Base64.decodeBase64(base64Code);
    }
}
