package com.github.tianjing.tgtools.encrypt.spring.security;

import com.github.tianjing.tgtools.encrypt.Encrypter;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import tgtools.exceptions.APPErrorException;
import tgtools.util.StringUtil;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author 田径
 * @create 2019-10-23 15:56
 * @desc
 **/
public class DelegatingEncrypter extends DelegatingPasswordEncoder implements Encrypter {
    protected static final String SUFFIX = "}";
    protected static final String PREFIX = "{";
    protected Map<String, PasswordEncoder> passwordEncoders;
    protected String key;


    public DelegatingEncrypter(String pIdForEncode, Map<String, PasswordEncoder> pIdToPasswordEncoder) {
        super(pIdForEncode, pIdToPasswordEncoder);
        passwordEncoders = new HashMap<>(pIdToPasswordEncoder);
        try {
            setField("idToPasswordEncoder", passwordEncoders);
        } catch (Exception e) {
        }
    }


    @Override
    public boolean matches(CharSequence rawPassword, String prefixEncodedPassword) {
        if (null == rawPassword && null != prefixEncodedPassword) {
            return false;
        }

        String vRawPassword = rawPassword.toString();
        if (StringUtil.isNullOrEmpty(vRawPassword) && StringUtil.isNullOrEmpty(prefixEncodedPassword)) {
            return true;
        }

        String vRawId = extractId(vRawPassword);
        String vEncodedId = extractId(prefixEncodedPassword);
        //rawPassword没加密，prefixEncodedPassword没加密
        if (StringUtil.isNullOrEmpty(vRawId) && StringUtil.isNullOrEmpty(vEncodedId)) {
            return vRawPassword.equals(prefixEncodedPassword);
        }

        //rawPassword加密，prefixEncodedPassword加密
        if (StringUtil.isNotEmpty(vRawId) && StringUtil.isNotEmpty(vEncodedId)) {
            return vRawId.equals(vEncodedId) && vRawPassword.equals(prefixEncodedPassword);
        }

        //rawPassword加密，prefixEncodedPassword没加密
        if (StringUtil.isNotEmpty(vRawId) && StringUtil.isNullOrEmpty(vEncodedId)) {
            try {
                PasswordEncoder vPasswordEncoder = getEncoderById(vRawId);
                if (null != vPasswordEncoder) {
                    String vEncodedPassword = vPasswordEncoder.encode(prefixEncodedPassword);
                    return vRawPassword.equals(PREFIX + vRawId + SUFFIX + vEncodedPassword);
                }

                return false;
            } catch (APPErrorException e) {
                return false;
            }
        }

        //rawPassword没加密，prefixEncodedPassword加密
        return super.matches(rawPassword, prefixEncodedPassword);
    }


    /**
     * 加密
     *
     * @param pEncodeContent
     * @return
     * @throws APPErrorException
     */
    @Override
    public String encode(CharSequence pEncodeContent) {
        return super.encode(pEncodeContent);
    }

    @Override
    public byte[] encode(byte[] pRaw) throws APPErrorException {
        throw new APPErrorException("不支持 encode byte[] 可以通过获取具体加密器再 encode");
    }

    /**
     * 解密
     *
     * @param pEncodeContent
     * @return
     * @throws APPErrorException
     */
    @Override
    public String decode(CharSequence pEncodeContent) throws APPErrorException {
        if (null == pEncodeContent) {
            return StringUtil.EMPTY_STRING;
        }
        String vEncodeContent = pEncodeContent.toString();
        String vId = extractId(vEncodeContent);

        if ("noop".equals(vId)) {
            return extractEncodedPassword(vEncodeContent);
        }

        if (StringUtil.isNullOrEmpty(vId)) {
            return vEncodeContent;
        }

        PasswordEncoder vDelegate = getEncoderByIdEncodedPassword(vEncodeContent);

        if (!(vDelegate instanceof Encrypter)) {
            throw new APPErrorException("不支持 Encrypter 的加密器：" + "class:" + vDelegate.getClass().getName());
        }

        return ((Encrypter) vDelegate).decode(extractEncodedPassword(vEncodeContent));
    }

    /**
     * 字节解密
     * 字节如何和id 对应规则 目前没有想好
     *
     * @param pRaw
     * @return
     * @throws APPErrorException
     */
    @Override
    public byte[] decode(byte[] pRaw) throws APPErrorException {
        throw new APPErrorException("不支持 decode byte[] 可以通过获取具体加密再decode");
    }


    /**
     * 根据带id标识的加密内容获取加密器
     * 如：{md5}DFJADSFJKLASJFKLADJLFJDASL
     *
     * @param pPrefixEncodedPassword
     * @return
     * @throws APPErrorException
     */
    public PasswordEncoder getEncoderByIdEncodedPassword(String pPrefixEncodedPassword) throws APPErrorException {
        String vId = this.extractId(pPrefixEncodedPassword);
        return getEncoderById(vId);
    }

    /**
     * 根据带id获取加密器
     * 如：md5
     *
     * @param pId
     * @return
     * @throws APPErrorException
     */
    public PasswordEncoder getEncoderById(String pId) throws APPErrorException {
        if (!this.passwordEncoders.containsKey(pId)) {
            throw new APPErrorException("不支持的加密类型：" + pId);
        }
        return this.passwordEncoders.get(pId);
    }

    /**
     * id标识的密文获取密文
     *
     * @param prefixEncodedPassword
     * @return
     */
    public String extractEncodedPassword(String prefixEncodedPassword) {
        int start = prefixEncodedPassword.indexOf(SUFFIX);
        return prefixEncodedPassword.substring(start + 1);
    }


    /**
     * 获取当前支持的所有加密方式的id
     *
     * @return
     */
    public Set<String> getIdSet() {
        return passwordEncoders.keySet();
    }

    /**
     * 获取只读的 所有 加密器 map
     *
     * @return
     */
    public Map<String, PasswordEncoder> getPasswordEncoderMap() {
        return new HashMap<>(passwordEncoders);
    }


    /**
     * 重新初始化
     *
     * @param pIdForEncode
     * @param pIdToPasswordEncoder
     */
    public void reset(String pIdForEncode, Map<String, PasswordEncoder> pIdToPasswordEncoder) throws APPErrorException {
        if (pIdForEncode == null) {
            throw new IllegalArgumentException("idForEncode cannot be null");
        }

        passwordEncoders = new HashMap<>(pIdToPasswordEncoder);
        ;

        if (!passwordEncoders.containsKey(pIdForEncode)) {
            throw new IllegalArgumentException("idForEncode " + pIdForEncode + "is not found in idToPasswordEncoder " + passwordEncoders);
        }

        for (String id : passwordEncoders.keySet()) {
            if (id == null) {
                continue;
            }
            if (id.contains(PREFIX)) {
                throw new IllegalArgumentException("id " + id + " cannot contain " + PREFIX);
            }
            if (id.contains(SUFFIX)) {
                throw new IllegalArgumentException("id " + id + " cannot contain " + SUFFIX);
            }
        }
        setField("idForEncode", pIdForEncode);
        setField("passwordEncoderForEncode", passwordEncoders.get(pIdForEncode));
        setField("idToPasswordEncoder", passwordEncoders);
    }

    protected void setField(String pFieldName, Object pFieldValue) throws APPErrorException {
        Field vField = tgtools.util.ReflectionUtil.findField(super.getClass(), pFieldName);
        vField.setAccessible(true);
        try {
            vField.set(this, pFieldValue);
        } catch (IllegalAccessException e) {
            throw new APPErrorException("setField 出错！", e);
        }
    }

    /**
     * id标识的密文中获取id
     *
     * @param pPrefixEncodedPassword
     * @return
     */
    public String extractId(String pPrefixEncodedPassword) {
        if (pPrefixEncodedPassword == null) {
            return null;
        } else {
            int vStart = pPrefixEncodedPassword.indexOf("{");
            if (vStart != 0) {
                return null;
            } else {
                int vEnd = pPrefixEncodedPassword.indexOf("}", vStart);
                return vEnd < 0 ? null : pPrefixEncodedPassword.substring(vStart + 1, vEnd);
            }
        }
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public void setKey(String pPassword) {
        key = pPassword;
        for (PasswordEncoder vPasswordEncoder : passwordEncoders.values()) {
            if (vPasswordEncoder instanceof Encrypter) {
                try {
                    ((Encrypter) vPasswordEncoder).setKey(key);
                } catch (Exception e) {
                }
            }
        }
    }
}
