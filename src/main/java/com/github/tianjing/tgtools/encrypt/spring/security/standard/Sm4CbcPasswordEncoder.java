package com.github.tianjing.tgtools.encrypt.spring.security.standard;

import com.github.tianjing.tgtools.encrypt.standard.Sm4CbcEncrypter;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 国密 对称 cbc 模式
 * @author 田径
 * @create 2019-08-30 10:23
 * @desc
 **/
public class Sm4CbcPasswordEncoder extends Sm4CbcEncrypter implements PasswordEncoder {

}
