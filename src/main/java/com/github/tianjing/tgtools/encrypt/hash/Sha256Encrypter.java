package com.github.tianjing.tgtools.encrypt.hash;

/**
 * @author 田径
 * @create 2019-09-01 18:29
 * @desc
 **/
public class Sha256Encrypter extends Sha1Encrypter {
    private static final char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    @Override
    protected String getAlgorithm() {
        return "SHA-256";
    }


}
