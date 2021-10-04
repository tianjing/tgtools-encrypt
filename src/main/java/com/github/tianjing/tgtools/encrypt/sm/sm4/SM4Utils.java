package com.github.tianjing.tgtools.encrypt.sm.sm4;


import tgtools.exceptions.APPErrorException;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Base64;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author 田径
 * @create 2019-10-23 11:00
 * @desc https://blog.csdn.net/qq_38683138/article/details/99609068
 **/
public class SM4Utils {
    protected static final Pattern PATTERN_ECB = Pattern.compile("\\s*|\t|\r|\n");


    protected String secretKey = "";
    protected String iv = "";
    protected Charset charset = Charset.forName("UTF-8");
    private boolean hexString = false;

    public SM4Utils() {
    }

    public static void main(String[] args) throws IOException, APPErrorException {
        String plainText = "hello word";
        SM4Utils sm4 = new SM4Utils();
        sm4.secretKey = "qawsedrftgyhujik";
        plainText.getBytes("UTF-8");
        System.out.println("ECB模式");
        String cipherText = sm4.encryptData_ECB(plainText);
        System.out.println("密文: " + cipherText);
        System.out.println("");

        plainText = sm4.decryptData_ECB(cipherText);
        System.out.println("明文: " + plainText);
        System.out.println("");

        System.out.println("CBC模式");
        sm4.iv = "UISwD9fW6cFh9SNS";
        cipherText = sm4.encryptData_CBC(plainText);
        System.out.println("密文: " + cipherText);
        System.out.println("");

        plainText = sm4.decryptData_CBC(cipherText);
        System.out.println("明文: " + plainText);
        //PI4ke7HMoUMD/LOSHWX5/g==

    }

    public String getIv() {
        return iv;
    }

    public void setIv(String iv) {
        this.iv = iv;
    }

    public Charset getCharset() {
        return charset;
    }

    public void setCharset(Charset charset) {
        this.charset = charset;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public byte[] encryptData_ECB(byte[] vData) throws APPErrorException {
        try {
            SM4_Context ctx = new SM4_Context();
            ctx.isPadding = true;
            ctx.mode = SM4.SM4_ENCRYPT;

            byte[] keyBytes;
            keyBytes = secretKey.getBytes();
            SM4 sm4 = new SM4();
            sm4.sm4_setkey_enc(ctx, keyBytes);
            return sm4.sm4_crypt_ecb(ctx, vData);
        } catch (Exception e) {
            if (e instanceof APPErrorException) {
                throw (APPErrorException) e;
            }

            throw new APPErrorException("encryptData_ECB byte[] 出错！原因：" + e.toString(), e);
        }
    }

    public String encryptData_ECB(String plainText) throws APPErrorException {
        try {
            byte[] encrypted = encryptData_ECB(plainText.getBytes(charset));
            String cipherText = Base64.getEncoder().encodeToString(encrypted);
            if (cipherText != null && cipherText.trim().length() > 0) {
                Matcher m = PATTERN_ECB.matcher(cipherText);
                cipherText = m.replaceAll("");
            }
            return cipherText;
        } catch (Exception e) {
            if (e instanceof APPErrorException) {
                throw (APPErrorException) e;
            }

            throw new APPErrorException("encryptData_ECB String 出错！原因：" + e.toString(), e);
        }
    }

    /**
     * sm4 ecb 解密
     *
     * @param vCipher
     * @return
     */
    public byte[] decryptData_ECB(byte[] vCipher) throws APPErrorException {
        try {
            SM4_Context vCtx = new SM4_Context();
            vCtx.isPadding = true;
            vCtx.mode = SM4.SM4_DECRYPT;

            byte[] keyBytes;
            keyBytes = secretKey.getBytes();
            SM4 sm4 = new SM4();
            sm4.sm4_setkey_dec(vCtx, keyBytes);
            byte[] decrypted = sm4.sm4_crypt_ecb(vCtx, vCipher);
            return decrypted;
        } catch (Exception e) {
            if (e instanceof APPErrorException) {
                throw (APPErrorException) e;
            }

            throw new APPErrorException("decryptData_ECB byte[] 出错！原因：" + e.toString(), e);
        }
    }

    /**
     * sm4 ecb 解密
     *
     * @param cipherText
     * @return
     */
    public String decryptData_ECB(String cipherText) throws APPErrorException {
        try {
            byte[] decrypted = decryptData_ECB(Base64.getDecoder().decode(cipherText));
            return new String(decrypted, charset);
        } catch (Exception e) {
            if (e instanceof APPErrorException) {
                throw (APPErrorException) e;
            }

            throw new APPErrorException("decryptData_ECB String 出错！原因：" + e.toString(), e);
        }
    }

    public byte[] encryptData_CBC(byte[] pData) throws APPErrorException {
        try {
            SM4_Context ctx = new SM4_Context();
            ctx.isPadding = true;
            ctx.mode = SM4.SM4_ENCRYPT;

            byte[] keyBytes;
            byte[] ivBytes;

            keyBytes = secretKey.getBytes();
            ivBytes = iv.getBytes();

            SM4 sm4 = new SM4();
            sm4.sm4_setkey_enc(ctx, keyBytes);
            return sm4.sm4_crypt_cbc(ctx, ivBytes, pData);
        } catch (Exception e) {
            if (e instanceof APPErrorException) {
                throw (APPErrorException) e;
            }

            throw new APPErrorException("encryptData_CBC byte[] 出错！原因：" + e.toString(), e);
        }

    }

    /**
     * sm4 cbc 解密
     *
     * @param vPlainText
     * @return
     */
    public String encryptData_CBC(String vPlainText) throws APPErrorException {
        try {
            byte[] encrypted = encryptData_CBC(vPlainText.getBytes(charset));
            String cipherText = Base64.getEncoder().encodeToString(encrypted);
            if (cipherText != null && cipherText.trim().length() > 0) {
                Matcher m = PATTERN_ECB.matcher(cipherText);
                cipherText = m.replaceAll("");
            }
            return cipherText;
        } catch (Exception e) {
            if (e instanceof APPErrorException) {
                throw (APPErrorException) e;
            }

            throw new APPErrorException("encryptData_CBC String 出错！原因：" + e.toString(), e);
        }
    }


    public byte[] decryptData_CBC(byte[] pData) throws APPErrorException {
        try {
            SM4_Context ctx = new SM4_Context();
            ctx.isPadding = true;
            ctx.mode = SM4.SM4_DECRYPT;

            byte[] keyBytes;
            byte[] ivBytes;
            if (hexString) {
                keyBytes = StringByteUtil.hexStringToBytes(secretKey);
                ivBytes = StringByteUtil.hexStringToBytes(iv);
            } else {
                keyBytes = secretKey.getBytes();
                ivBytes = iv.getBytes();
            }

            SM4 sm4 = new SM4();
            sm4.sm4_setkey_dec(ctx, keyBytes);
            return sm4.sm4_crypt_cbc(ctx, ivBytes, pData);

        } catch (Exception e) {
            if (e instanceof APPErrorException) {
                throw (APPErrorException) e;
            }

            throw new APPErrorException("decryptData_CBC byte[] 出错！原因：" + e.toString(), e);
        }
    }

    /**
     * @param cipherText
     * @return
     */
    public String decryptData_CBC(String cipherText) throws APPErrorException {
        try {
            byte[] decrypted = decryptData_CBC(Base64.getDecoder().decode(cipherText));
            return new String(decrypted, charset);
        } catch (Exception e) {
            if (e instanceof APPErrorException) {
                throw (APPErrorException) e;
            }

            throw new APPErrorException("decryptData_CBC String 出错！原因：" + e.toString(), e);
        }
    }

}
