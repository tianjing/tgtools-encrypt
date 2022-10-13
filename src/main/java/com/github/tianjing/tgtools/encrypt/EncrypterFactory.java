package com.github.tianjing.tgtools.encrypt;


import com.github.tianjing.tgtools.encrypt.hash.Md5Encrypter;
import com.github.tianjing.tgtools.encrypt.hash.Sha1Encrypter;
import com.github.tianjing.tgtools.encrypt.hash.Sha256Encrypter;
import com.github.tianjing.tgtools.encrypt.spring.security.DelegatingEncrypter;
import com.github.tianjing.tgtools.encrypt.spring.security.PasswordEncoderFactory;
import com.github.tianjing.tgtools.encrypt.spring.security.hash.Sha1PasswordEncoder;
import com.github.tianjing.tgtools.encrypt.spring.security.hash.Sha256PasswordEncoder;
import com.github.tianjing.tgtools.encrypt.standard.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 田径
 * @create 2019-09-02 9:03
 * @desc
 **/
public final class EncrypterFactory {

    /**
     * 创建 Md5
     *
     * @return
     */
    public static Md5Encrypter newMd5Encrypter() {
        return new Md5Encrypter();
    }

    /**
     * 创建 Sha1
     *
     * @return
     */
    public static Sha1Encrypter newSha1Encrypter() {
        return new Sha1Encrypter();
    }

    /**
     * 创建 Sha256
     *
     * @return
     */
    public static Sha256Encrypter newSha256Encrypter() {
        return new Sha256Encrypter();
    }

    /**
     * 创建 Aes/Ecb/Pkcs5Paddind 加密器
     *
     * @return
     */
    public static AesEncrypter newAesEcbPkcs5PaddingEncrypter() {
        return new AesEncrypter();
    }

    /**
     * 创建一个没有模式没有填充的加密器
     *
     * @return
     */
    public static DesEncrypter newDesNoModeNoPaddingEncrypter() {
        return new DesEncrypter();
    }


    /**
     * 创建一个没有模式没有填充的加密器 使用 salt 进行混淆
     *
     * @return
     */
    public static DesSaltEncrypter newDesSaltNoModeNoPaddingEncrypter() {
        return new DesSaltEncrypter();
    }

    /**
     * sm4 cbc 模式加密器
     *
     * @return
     */
    public static Sm4CbcEncrypter newSm4CbcEncrypter() {
        return new Sm4CbcEncrypter();
    }

    /**
     * sm4 ecb 模式加密器
     *
     * @return
     */
    public static Sm4EcbEncrypter newSm4EcbEncrypter() {
        return new Sm4EcbEncrypter();
    }


    /**
     * 创建一个综合加密器
     *
     * @param pDefaultEncodeId
     * @param pPassword
     * @return
     */
    public static DelegatingEncrypter createDelegatingEncrypter(String pDefaultEncodeId, String pPassword) {
        Map<String, PasswordEncoder> vPasswordEncoders = new HashMap<>(20);
        vPasswordEncoders.put("noop", org.springframework.security.crypto.password.NoOpPasswordEncoder.getInstance());
        //hash加密器
        vPasswordEncoders.put("md5", PasswordEncoderFactory.newMd5Encrypter());
        vPasswordEncoders.put("MD5", vPasswordEncoders.get("md5"));

        vPasswordEncoders.put("SHA-1", new Sha1PasswordEncoder());
        vPasswordEncoders.put("SHA-256", new Sha256PasswordEncoder());
        vPasswordEncoders.put("sha256", new Sha256PasswordEncoder());
        vPasswordEncoders.put("pbkdf2", new Pbkdf2PasswordEncoder());
        vPasswordEncoders.put("scrypt", new SCryptPasswordEncoder());

        //对称加密器
        vPasswordEncoders.put("des", PasswordEncoderFactory.newDesNoModeNoPaddingEncrypter());
        vPasswordEncoders.put("desa", PasswordEncoderFactory.newDesSaltNoModeNoPaddingEncrypter());
        vPasswordEncoders.put("sm4c", PasswordEncoderFactory.newSm4CbcEncrypter());
        vPasswordEncoders.put("sm4e", PasswordEncoderFactory.newSm4EcbEncrypter());

        DelegatingEncrypter vDelegatingPasswordEncoder = new DelegatingEncrypter(pDefaultEncodeId, vPasswordEncoders);
        vDelegatingPasswordEncoder.setKey(pPassword);

        return vDelegatingPasswordEncoder;
    }
}
