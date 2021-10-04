package com.github.tianjing.tgtools.encrypt.spring.security;


import com.github.tianjing.tgtools.encrypt.spring.security.hash.Md5PasswordEncoder;
import com.github.tianjing.tgtools.encrypt.spring.security.hash.Sha1PasswordEncoder;
import com.github.tianjing.tgtools.encrypt.spring.security.hash.Sha256PasswordEncoder;
import com.github.tianjing.tgtools.encrypt.spring.security.standard.*;

/**
 * @author 田径
 * @create 2019-09-02 9:03
 * @desc
 **/
public final class PasswordEncoderFactory {

    /**
     * 创建 Md5
     * @return
     */
    public static Md5PasswordEncoder newMd5Encrypter()
    {
        return new Md5PasswordEncoder();
    }

    /**
     * 创建 Sha1
     * @return
     */
    public static Sha1PasswordEncoder newSha1Encrypter()
    {
        return new Sha1PasswordEncoder();
    }

    /**
     * 创建 Sha256
     * @return
     */
    public static Sha256PasswordEncoder newSha256Encrypter()
    {
        return new Sha256PasswordEncoder();
    }

    /**
     * 创建 Aes/Ecb/Pkcs5Paddind 加密器
     * @return
     */
    public static AesPasswordEncoder newAesEcbPkcs5PaddingEncrypter()
    {
        return new AesPasswordEncoder();
    }

    /**
     * 创建一个没有模式没有填充的加密器
     * @return
     */
    public static DesPasswordEncoder newDesNoModeNoPaddingEncrypter()
    {
        return new DesPasswordEncoder();
    }

    /**
     * 创建一个没有模式没有填充的加密器 使用 salt 进行混淆
     * @return
     */
    public static DesSaltPasswordEncoder newDesSaltNoModeNoPaddingEncrypter()
    {
        return new DesSaltPasswordEncoder();
    }

    /**
     * sm4 cbc 模式加密器
     * @return
     */
    public static Sm4CbcPasswordEncoder newSm4CbcEncrypter()
    {
        return new Sm4CbcPasswordEncoder();
    }

    /**
     * sm4 ecb 模式加密器
     * @return
     */
    public static Sm4EcbPasswordEncoder newSm4EcbEncrypter()
    {
        return new Sm4EcbPasswordEncoder();
    }
}
