package com.github.tianjing.tgtools.encrypt.hash;


import com.github.tianjing.tgtools.encrypt.AbstractEncrypter;
import tgtools.exceptions.APPErrorException;
import tgtools.util.StringUtil;

/**
 * @author 田径
 * @create 2019-09-01 18:29
 * @desc
 **/
public class Md5Encrypter extends AbstractEncrypter {
    @Override
    public String getKey() throws APPErrorException {
        throw new APPErrorException("不支持 Key");
    }

    @Override
    public void setKey(String pPassword) throws APPErrorException {
        throw new APPErrorException("不支持 Key");
    }

    @Override
    public String encode(CharSequence pRaw)  {
        if (null == pRaw) {
            return null;
        }

        return tgtools.util.EncrpytionUtil.str2MD5(pRaw.toString());
    }

    @Override
    public byte[] encode(byte[] pRaw) throws APPErrorException {
        if (null == pRaw) {
            return null;
        }

        try {
            return tgtools.util.EncrpytionUtil.str2MD5Bytes(pRaw.toString());
        } catch (Exception e) {
            throw new APPErrorException("encode 出错！原因：" + e.toString());
        }
    }

    @Override
    public boolean matches(CharSequence pRaw, String pEncode)  {
        return null != pRaw && StringUtil.isNotEmpty(pRaw.toString())
                && StringUtil.isNotEmpty(pEncode) && pEncode.equals(encode(pRaw));
    }

    @Override
    public String decode(CharSequence pEncode) throws APPErrorException {
        throw new APPErrorException("不支持 解密");
    }

    @Override
    public byte[] decode(byte[] pRaw) throws APPErrorException {
        throw new APPErrorException("不支持 解密");
    }
}
