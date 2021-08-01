package com.egzosn.pay.common.util.sign.encrypt;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.egzosn.pay.common.bean.result.PayException;
import com.egzosn.pay.common.exception.PayErrorException;

/**
 *
 * HmacSHA256
 * @author Egan
 * email egzosn@gmail.com
 * date 2021/8/1
 */
public class HmacSha256 {
    private static final Logger LOG = LoggerFactory.getLogger(HmacSha256.class);

    /**
     * 签名
     *
     * @param content           需要签名的内容
     * @param key               密钥
     * @param characterEncoding 字符编码
     *
     * @return 签名值
     */
    public static String createSign(String content, String key, String characterEncoding) {
        Mac sha256HMAC = null;
        try {
            sha256HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(characterEncoding), "HmacSHA256");
            sha256HMAC.init(secretKey);
            byte[] array = sha256HMAC.doFinal(content.getBytes(characterEncoding));
            StringBuilder sb = new StringBuilder();
            for (byte item : array) {
                sb.append(Integer.toHexString((item & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString().toUpperCase();
        }
        catch (UnsupportedEncodingException e) {
            LOG.error("", e);
        }
        catch (GeneralSecurityException e) {
            LOG.error("", e);
        }

        throw new PayErrorException(new PayException("fail", "HMACSHA256 签名异常"));
    }
}
