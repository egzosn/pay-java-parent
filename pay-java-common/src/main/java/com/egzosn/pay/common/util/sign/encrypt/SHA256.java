package com.egzosn.pay.common.util.sign.encrypt;/**
 * Description：
 * author: Fuzx
 * date: 2017/11/27 0027
 */

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

/**
 * @author Actinia
 * @email hayesfu@qq.com
 * @create 2017 2017/11/27 0027
 */
public class SHA256 {
    //日志
    protected static final Log log = LogFactory.getLog(SHA256.class);

    /**
     * 算法常量： SHA256
     */
    private static final String ALGORITHM_SHA256 = "SHA-256";
    /**
     * sha256计算后进行16进制转换
     *
     * @param data
     *            待计算的数据
     * @param encoding
     *            编码
     * @return 计算结果
     */
    public static byte[] sha256X16(String data, String encoding) {
        byte[] bytes = sha256(data, encoding);
        StringBuilder sha256StrBuff = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            if (Integer.toHexString(0xFF & bytes[i]).length() == 1) {
                sha256StrBuff.append("0").append(
                        Integer.toHexString(0xFF & bytes[i]));
            } else {
                sha256StrBuff.append(Integer.toHexString(0xFF & bytes[i]));
            }
        }
        try {
            return sha256StrBuff.toString().getBytes(encoding);
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }
    /**
     * sha256计算
     *
     * @param datas
     *            待计算的数据
     * @param encoding
     *            字符集编码
     * @return
     */
    private static byte[] sha256(String datas, String encoding) {
        try {
            return sha256(datas.getBytes(encoding));
        } catch (UnsupportedEncodingException e) {
            log.error("SHA256计算失败", e);
            return null;
        }
    }
    /**
     * sha256计算.
     *
     * @param data
     *            待计算的数据
     * @return 计算结果
     */
    private static byte[] sha256(byte[] data) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance(ALGORITHM_SHA256);
            md.reset();
            md.update(data);
            return md.digest();
        } catch (Exception e) {
            log.error("SHA256计算失败", e);
            return null;
        }
    }
}
