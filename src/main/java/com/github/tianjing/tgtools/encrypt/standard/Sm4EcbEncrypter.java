package com.github.tianjing.tgtools.encrypt.standard;

import com.github.tianjing.tgtools.encrypt.Encrypter;
import com.github.tianjing.tgtools.encrypt.sm.sm4.SM4Utils;
import tgtools.exceptions.APPErrorException;
import tgtools.exceptions.APPRuntimeException;
import tgtools.util.StringUtil;

/**
 * 国密 对称 ecb 模式
 * @author 田径
 * @create 2019-08-30 10:23
 * @desc
 **/
public class Sm4EcbEncrypter implements Encrypter {

    protected String key = "";
    SM4Utils sm4 = new SM4Utils();


    @Override
    public String getKey() {
        return key;
    }

    @Override
    public void setKey(String pKey) {
        key = pKey;
    }


    @Override
    public String encode(CharSequence pRaw) {
        try {
            sm4.setSecretKey(getKey());
            return sm4.encryptData_ECB(pRaw.toString());
        } catch (Exception e) {
            throw new APPRuntimeException("encode CharSequence 出错；原因：" + e.toString(), e);
        }
    }

    @Override
    public byte[] encode(byte[] pRaw) throws APPErrorException {
        try {
            sm4.setSecretKey(getKey());
            return sm4.encryptData_ECB(pRaw);
        } catch (Exception e) {
            throw new APPErrorException("encode byte[] 出错；原因：" + e.toString(), e);
        }
    }

    @Override
    public String decode(CharSequence pEncode) throws APPErrorException {
        if (pEncode == null) {
            return null;
        }
        try {
            sm4.setSecretKey(getKey());
            return sm4.decryptData_ECB(pEncode.toString());
        } catch (Exception e) {
            throw new APPErrorException("decode CharSequence 出错；原因：" + e.toString(), e);
        }
    }

    @Override
    public byte[] decode(byte[] pEncode) throws APPErrorException {
        try {
            sm4.setSecretKey(getKey());
            return sm4.decryptData_ECB(pEncode);
        } catch (Exception e) {
            throw new APPErrorException("decode byte[] 出错；原因：" + e.toString(), e);
        }
    }

    @Override
    public boolean matches(CharSequence pRaw, String pEncode) {
        return null != pRaw && StringUtil.isNotEmpty(pRaw.toString())
                && StringUtil.isNotEmpty(pEncode) && encode(pRaw).equals(pEncode);
    }
}
