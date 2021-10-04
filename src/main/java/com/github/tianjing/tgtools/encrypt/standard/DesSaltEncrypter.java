package com.github.tianjing.tgtools.encrypt.standard;

import org.apache.commons.codec.binary.Base64;
import tgtools.exceptions.APPErrorException;
import tgtools.exceptions.APPRuntimeException;
import tgtools.util.StringUtil;

import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * @author 田径
 * @create 2019-08-30 10:23
 * @desc
 **/
public class DesSaltEncrypter extends DesEncrypter {

    protected String salt = "@tianjing123$";
    protected byte[] saltBytes = null;
    protected int saltLength = 0;

    public DesSaltEncrypter() {
        initSalt();
    }

    protected static byte[] concat(byte[] pFirst, byte[] pSecond) {

        byte[] c = new byte[pFirst.length + pSecond.length];

        System.arraycopy(pFirst, 0, c, 0, pFirst.length);

        System.arraycopy(pSecond, 0, c, pFirst.length, pSecond.length);

        return c;

    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String pSalt) {
        if (StringUtil.isNotEmpty(pSalt)) {
            this.salt = pSalt;
            initSalt();
        }
    }

    protected void initSalt() {
        saltBytes = salt.getBytes(Charset.forName(ENCODE));
        saltLength = saltBytes.length;
    }

    @Override
    public String encode(CharSequence pRaw) {
        byte[] vRaw = null;
        byte[] vKey = null;
        try {
            vRaw = (salt + pRaw.toString()).getBytes(ENCODE);
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
            byte[] vNewRaw = concat(saltBytes, pRaw);
            return doEncrypt(vNewRaw, getKey().getBytes(ENCODE));
        } catch (Exception e) {
            throw new APPErrorException("encode 出错；原因：" + e.toString(), e);
        }
    }

    @Override
    public String decode(CharSequence pEncode) throws APPErrorException {
        if (pEncode == null) {
            return null;
        }

        try {
            byte[] buf = Base64.decodeBase64(pEncode.toString());
            byte[] bt = doDecrypt(buf, getKey().getBytes(ENCODE));
            String vRaw = new String(bt, ENCODE);
            if (vRaw.length() > salt.length()) {
                return vRaw.substring(salt.length());
            }
            return vRaw;
        } catch (Exception e) {
            throw new APPErrorException("decode 出错；原因：" + e.toString(), e);
        }
    }

    @Override
    public byte[] decode(byte[] pRaw) throws APPErrorException {
        try {
            byte[] vRaw = doDecrypt(pRaw, getKey().getBytes(ENCODE));
            if (vRaw.length > saltLength) {
                return Arrays.copyOf(pRaw, saltLength);
            }
            return vRaw;
        } catch (Exception e) {
            throw new APPErrorException("byte[] decrypt(byte[] pRaw) 出错；原因：" + e.toString(), e);
        }
    }
}
