package com.egzosn.pay.common.util.sign.encrypt;

import com.egzosn.pay.common.util.str.StringUtils;
import org.apache.commons.codec.digest.DigestUtils;


/**
 * SHA1签名工具
 * @author Actinia
 * <pre>
 * email hayesfu@qq.com
 *
 * create 2017 2017/11/27 0027
 * </pre>
 */
public class SHA1 {



    /**
     * 签名字符串
     *
     * @param text          需要签名的字符串
     * @param key           密钥
     * @param inputCharset 编码格式
     * @return 签名结果
     */
    public static String sign(String text, String key, String inputCharset) {
        //拼接key
        text = text + key;
        return DigestUtils.sha1Hex(  StringUtils.getContentBytes(text, inputCharset));
    }


    /**
     * 签名字符串
     *
     * @param text          需要签名的字符串
     * @param sign          签名结果
     * @param key           密钥
     * @param inputCharset 编码格式
     * @return 签名结果
     */
    public static boolean verify(String text, String sign, String key, String inputCharset) {
        //判断是否一样
        return StringUtils.equals(sign(text, key, inputCharset).toUpperCase(), sign.toUpperCase());
    }

}
