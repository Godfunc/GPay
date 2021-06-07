package com.godfunc.util;

import com.godfunc.exception.GException;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;

/**
 * rsa2签名工具类
 */
public class SignUtils {

    private static final String SIGN_SHA256RSA_ALGORITHMS = "SHA256WithRSA";
    private static final String CHARSET_UTF8 = "UTF-8";
    private static final String SIGN_NAME = "sign";

    /**
     * 对 String 进行签名
     * algorithms: SHA256WithRSA
     * charset: UTF-8
     *
     * @param content    待签名字符串
     * @param privateKey 私钥
     * @return
     */
    public static String rsa2Sign(String content, String privateKey) {
        return sign(content, privateKey, SIGN_SHA256RSA_ALGORITHMS, CHARSET_UTF8);
    }

    /**
     * 对 String 进行签名，可以定义编码
     * algorithms: SHA256WithRSA
     * charset: 自定义
     *
     * @param content    待签名字符串
     * @param privateKey 私钥
     * @param charset    编码
     * @return
     */
    public static String rsa2Sign(String content, String privateKey, String charset) {
        return sign(content, privateKey, SIGN_SHA256RSA_ALGORITHMS, charset);
    }

    /**
     * 对 Object 对象进行签名
     * algorithms: SHA256WithRSA
     * charset: UTF-8
     * signName: sign
     *
     * @param params     待签名对象
     * @param privateKey 私钥
     * @return
     */
    public static String rsa2Sign(Object params, String privateKey) {
        return sign(getSignContent(params), privateKey, SIGN_SHA256RSA_ALGORITHMS, CHARSET_UTF8);
    }

    /**
     * 对 Object 对象进行签名
     * algorithms: SHA256WithRSA
     * charset: UTF-8
     * signName: 自定义
     *
     * @param params     待签名对象
     * @param privateKey 私钥
     * @param signName   存放签名的属性名
     * @return
     */
    public static String rsa2Sign(Object params, String privateKey, String signName) {
        return sign(getSignContent(params, signName), privateKey, SIGN_SHA256RSA_ALGORITHMS, CHARSET_UTF8);
    }

    /**
     * 对 Map 对象进行签名
     * algorithms: SHA256WithRSA
     * charset: UTF-8
     * signName: sign
     *
     * @param params     待签名集合
     * @param privateKey 私钥
     * @return
     */
    public static String rsa2Sign(Map<String, Object> params, String privateKey) {
        return sign(getSignContent(params), privateKey, SIGN_SHA256RSA_ALGORITHMS, CHARSET_UTF8);
    }

    /**
     * 对 Map 对象进行签名
     * algorithms: SHA256WithRSA
     * charset: UTF-8
     * signName: 自定义
     *
     * @param params     待签名集合
     * @param privateKey 私钥
     * @param signName   存放签名的属性名
     * @return
     */
    public static String rsa2Sign(Map<String, Object> params, String privateKey, String signName) {
        return sign(getSignContent(params, signName), privateKey, SIGN_SHA256RSA_ALGORITHMS, CHARSET_UTF8);
    }

    public static boolean rsa2Check(String content, String publicKey, String sign) {
        return signCheck(content, publicKey, sign, SIGN_SHA256RSA_ALGORITHMS, CHARSET_UTF8);
    }

    public static boolean rsa2Check(String content, String publicKey, String sign, String charset) {
        return signCheck(content, publicKey, sign, SIGN_SHA256RSA_ALGORITHMS, charset);
    }

    public static boolean rsa2Check(Object params, String publicKey, String sign) {
        return signCheck(getSignContent(params), publicKey, sign, SIGN_SHA256RSA_ALGORITHMS, CHARSET_UTF8);
    }

    public static boolean rsa2Check(Object params, String publicKey, String sign, String signName) {
        return signCheck(getSignContent(params, signName), publicKey, sign, SIGN_SHA256RSA_ALGORITHMS, CHARSET_UTF8);
    }

    public static boolean rsa2Check(Object params, String publicKey, String sign, String signName, String charset) {
        return signCheck(getSignContent(params, signName), publicKey, sign, SIGN_SHA256RSA_ALGORITHMS, charset);
    }

    public static boolean rsa2Check(Map<String, Object> params, String publicKey, String sign) {
        return signCheck(getSignContent(params), publicKey, sign, SIGN_SHA256RSA_ALGORITHMS, CHARSET_UTF8);
    }

    public static boolean rsa2Check(Map<String, Object> params, String publicKey, String sign, String signName) {
        return signCheck(getSignContent(params, signName), publicKey, sign, SIGN_SHA256RSA_ALGORITHMS, CHARSET_UTF8);
    }

    public static boolean rsa2Check(Map<String, Object> params, String publicKey, String sign, String signName, String charset) {
        return signCheck(getSignContent(params, signName), publicKey, sign, SIGN_SHA256RSA_ALGORITHMS, charset);
    }

    public static String getSignContent(Object params) {
        return getSignContent(params, SIGN_NAME);
    }

    public static String getSignContent(Map<String, Object> params) {
        return getSignContent(params, SIGN_NAME);
    }

    public static String getSignContent(Object params, String signName) {
        try {
            Map<String, String> describe = BeanUtils.describe(params);
            describe.remove("class");
            return getSignContent(describe, signName);
        } catch (Exception e) {
            throw new GException(e, "签名内容生成异常 params={}, signName={}", params, signName);
        }
    }

    public static String getSignContent(Map<String, Object> params, String signName) {
        if (params == null) {
            return null;
        }
        StringBuilder content = new StringBuilder();
        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);

        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key).toString();
            if (StringUtils.isNotBlank(signName) && key.equals(signName)) {
                continue;
            }
            content.append((i == 0 ? "" : "&") + key + "=" + value);
        }
        return content.toString();
    }

    public static String sign(String content, String privateKey, String signType, String charset) {
        try {
            PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKey));
            PrivateKey priKey = KeyFactory.getInstance("RSA").generatePrivate(priPKCS8);
            Signature signature = Signature.getInstance(signType);
            signature.initSign(priKey);
            signature.update(content.getBytes(charset));
            return Base64.encodeBase64String(signature.sign());
        } catch (Exception e) {
            throw new GException(e, "签名异常 content={}, privateKey={}, signType={}, charset={}", content, privateKey, signType, charset);
        }
    }

    public static boolean signCheck(String content, String publicKey, String sign, String signType, String charset) {
        try {
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(Base64.decodeBase64(publicKey));
            PublicKey pubKey = KeyFactory.getInstance("RSA").generatePublic(keySpec);
            Signature signature = Signature.getInstance(signType);
            signature.initVerify(pubKey);
            signature.update(content.getBytes(charset));
            return signature.verify(Base64.decodeBase64(sign));
        } catch (Exception e) {
            throw new GException(e, "签名异常 content={}, privateKey={}, signType={} charset={}", content, publicKey, signType, charset);
        }
    }
}
