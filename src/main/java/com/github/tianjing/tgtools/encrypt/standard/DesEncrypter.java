package com.github.tianjing.tgtools.encrypt.standard;


import com.github.tianjing.tgtools.encrypt.Encrypter;
import org.apache.commons.codec.binary.Base64;
import tgtools.exceptions.APPErrorException;
import tgtools.exceptions.APPRuntimeException;
import tgtools.util.StringUtil;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import java.security.SecureRandom;

/**
 * @author 田径
 * @create 2019-08-30 10:23
 * @desc
 **/
public class DesEncrypter implements Encrypter {

    protected final static String DES = "DES";
    protected String key = "";


    /**
     * Description 根据键值进行加密
     *
     * @param data
     * @param key  加密键byte数组
     * @return
     * @throws Exception
     */
    protected byte[] doEncrypt(byte[] data, byte[] key) throws APPErrorException {
        // 生成一个可信任的随机数源
        SecureRandom sr = new SecureRandom();
        try {
            // 从原始密钥数据创建DESKeySpec对象
            DESKeySpec dks = new DESKeySpec(key);
            // 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
            SecretKey securekey = keyFactory.generateSecret(dks);
            // Cipher对象实际完成加密操作
            Cipher cipher = Cipher.getInstance(DES);
            // 用密钥初始化Cipher对象
            cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);
            return cipher.doFinal(data);
        } catch (Exception e) {
            throw new APPErrorException("encrypt 出错；原因：" + e.toString(), e);
        }
    }

    /**
     * Description 根据键值进行解密
     *
     * @param data
     * @param key  加密键byte数组
     * @return
     * @throws Exception
     */
    protected byte[] doDecrypt(byte[] data, byte[] key) throws APPErrorException {
        // 生成一个可信任的随机数源
        SecureRandom sr = new SecureRandom();
        try {
            // 从原始密钥数据创建DESKeySpec对象
            DESKeySpec dks = new DESKeySpec(key);

            // 创建一个密钥工厂，然后用它把DESKeySpec转换成SecretKey对象
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(DES);
            SecretKey securekey = keyFactory.generateSecret(dks);

            // Cipher对象实际完成解密操作
            Cipher cipher = Cipher.getInstance(DES);

            // 用密钥初始化Cipher对象
            cipher.init(Cipher.DECRYPT_MODE, securekey, sr);

            return cipher.doFinal(data);
        } catch (Exception e) {
            throw new APPErrorException("decrypt 出错；原因：" + e.toString(), e);
        }
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public void setKey(String pKey) {
        key = pKey;
    }


    @Override
    public String encode(CharSequence pRaw)  {
        byte[] vRaw = null;
        byte[] vKey = null;
        try {
            vRaw = pRaw.toString().getBytes(ENCODE);
            vKey = getKey().getBytes(ENCODE);
            byte[] vEncode = doEncrypt(vRaw, vKey);
            return Base64.encodeBase64String(vEncode);

        } catch (Exception e) {
            throw new APPRuntimeException("encode 出错；原因：" + e.toString(), e);
        }
    }

    @Override
    public byte[] encode(byte[] pRaw) throws APPErrorException {
        try {
            return doEncrypt(pRaw, getKey().getBytes(ENCODE));
        } catch (Exception e) {
            throw new APPErrorException("encode 出错；原因：" + e.toString(), e);
        }
    }

    @Override
    public boolean matches(CharSequence pRaw, String pEncode) {
        return null != pRaw && StringUtil.isNotEmpty(pRaw.toString())
                && StringUtil.isNotEmpty(pEncode) && encode(pRaw).equals(pEncode);
    }

    @Override
    public String decode(CharSequence pEncode) throws APPErrorException {
        if (pEncode == null) {
            return null;
        }

        try {
            byte[] buf = Base64.decodeBase64(pEncode.toString());
            byte[] bt = doDecrypt(buf, getKey().getBytes(ENCODE));
            return new String(bt, ENCODE);
        } catch (Exception e) {
            throw new APPErrorException("decode 出错；原因：" + e.toString(), e);
        }
    }

    @Override
    public byte[] decode(byte[] pRaw) throws APPErrorException {
        try {
            return doDecrypt(pRaw, getKey().getBytes(ENCODE));
        } catch (Exception e) {
            throw new APPErrorException("byte[] decrypt(byte[] pRaw) 出错；原因：" + e.toString(), e);
        }
    }

}
