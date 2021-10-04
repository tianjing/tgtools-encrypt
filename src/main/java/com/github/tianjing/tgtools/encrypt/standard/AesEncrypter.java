package com.github.tianjing.tgtools.encrypt.standard;


import com.github.tianjing.tgtools.encrypt.AbstractEncrypter;
import tgtools.exceptions.APPErrorException;
import tgtools.exceptions.APPRuntimeException;
import tgtools.util.StringUtil;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;

/**
 * @author 田径
 * @create 2019-08-30 10:23
 * @desc
 **/
public class AesEncrypter extends AbstractEncrypter {
    private static final String AES = "AES";
    /**
     * 算法
     */
    private static final String ALGORITHMSTR = "AES/ECB/PKCS5Padding";
    private String key = "";
    private KeyGenerator kgen;
    private SecretKeySpec secretKeySpec;

    /**
     * AES解密
     *
     * @param encryptBytes 待解密的byte[]
     * @param decryptKey   解密密钥
     * @return 解密后的String
     * @throws Exception
     */
    private String decryptByBytes(byte[] encryptBytes, String decryptKey) throws APPErrorException {
        try {
            return new String(doDecrypt(encryptBytes, decryptKey.getBytes()), ENCODE);
        } catch (UnsupportedEncodingException e) {
            throw new APPErrorException("decryptByBytes 出错；原因：" + e.toString(), e);
        }
    }

    public  SecretKeySpec getSecretKeySpec(byte[] decryptKey) throws APPErrorException {
        if (null == secretKeySpec) {
            try {
                secretKeySpec = new SecretKeySpec(getKeyBytes(), AES);
            } catch (Exception e) {
                throw new APPErrorException("getSecretKeySpec 出错；原因：" + e.toString(), e);
            }
        }
        return secretKeySpec;
    }

    /**
     * AES加密
     *
     * @param content    待加密的内容
     * @param encryptKey 密钥
     * @return 加密后的byte[]
     * @throws Exception
     */
    private byte[] doEncrypt(byte[] content, byte[] encryptKey) throws APPErrorException {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHMSTR);
            cipher.init(Cipher.ENCRYPT_MODE, getSecretKeySpec(encryptKey));
            return cipher.doFinal(content);
        } catch (Exception e) {
            throw new APPErrorException("aesEncryptToBytes 出错；原因：" + e.toString(), e);
        }
    }

    /**
     * AES解密
     *
     * @param encryptBytes    加密的内容
     * @param decryptKey 密钥
     * @return 加密后的byte[]
     * @throws Exception
     */
    private byte[] doDecrypt(byte[] encryptBytes, byte[] decryptKey) throws APPErrorException {
        try {
            Cipher cipher = Cipher.getInstance(ALGORITHMSTR);
            cipher.init(Cipher.DECRYPT_MODE, getSecretKeySpec(decryptKey));
            byte[] decryptBytes = cipher.doFinal(encryptBytes);
            return decryptBytes;
        } catch (Exception e) {
            throw new APPErrorException("aesDecryptByBytes:" + e.toString(), e);
        }
    }

    /**
     * 将base 64 code AES解密
     *
     * @param encryptStr 待解密的base 64 code
     * @param decryptKey 解密密钥
     * @return 解密后的string
     * @throws Exception
     */
    public String decrypt(String encryptStr, String decryptKey) throws Exception {
        return StringUtil.isNullOrEmpty(encryptStr) ? null : decryptByBytes(base64FromString(encryptStr), decryptKey);
    }

    @Override
    public String getKey() throws APPErrorException {
        return key;
    }

    @Override
    public void setKey(String pPassword) throws APPErrorException {
        key = pPassword;

    }

    @Override
    public String encode(CharSequence pRaw)  {
        try {
            return base64ToString(encode(pRaw.toString().getBytes(ENCODE)));
        } catch (Exception e) {
            throw new APPRuntimeException("encode 出错；原因：" + e.toString(), e);
        }
    }

    @Override
    public byte[] encode(byte[] pRaw) throws APPErrorException {
        return doEncrypt(pRaw, getKeyBytes());
    }

    @Override
    public boolean matches(CharSequence pRaw, String pEncode)  {
        return null != pRaw && StringUtil.isNotEmpty(pRaw.toString())
                && StringUtil.isNotEmpty(pEncode) && pEncode.equals(encode(pRaw));
    }

    @Override
    public String decode(CharSequence pEncode) throws APPErrorException {
        try {
            return decrypt(pEncode.toString(), getKey());
        } catch (Exception e) {
            throw new APPErrorException("decode 出错；原因：" + e.toString(), e);
        }
    }

    @Override
    public byte[] decode(byte[] pRaw) throws APPErrorException {
        return doDecrypt(pRaw, getKeyBytes());
    }

}
