package org.javaboy.api_sign_demo.utils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * @author：江南一点雨
 * @site：http://www.javaboy.org
 * @微信公众号：江南一点雨
 * @github：https://github.com/lenve
 * @gitee：https://gitee.com/lenve
 */
public class SignUtils {

    /**
     * 使用 HmacSHA1 算法进行签名
     * @param secretKey 密钥
     * @param data 数据
     * @return
     */
    public static String signWithHmacSha1(String secretKey, String data) {

        try {
            SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes("UTF-8"), "HmacSHA1");
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(signingKey);
            return Base64.getEncoder().encodeToString(mac.doFinal(data.getBytes("UTF-8")));
        } catch (NoSuchAlgorithmException | InvalidKeyException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 验证签名
     * @param secretKey 密钥
     * @param data 数据
     * @param hmac 签名
     * @return
     */
    public static boolean verify(String secretKey, String data, String hmac) {
        String calculatedHmac = signWithHmacSha1(secretKey, data);
        return calculatedHmac.equals(hmac);
    }
}
