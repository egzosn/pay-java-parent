package com.egzosn.pay.common.util.sign.encrypt;

import java.io.IOException;
import java.security.GeneralSecurityException;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.digest.DigestUtils;

import com.egzosn.pay.common.util.sign.SignUtils;

/**
 * AES 加解密
 *
 * @author Egan
 * <pre>
 *  email egan@egzosn.com
 *  date 2022/3/20
 *  </pre>
 */
public class AES {
    /**
     * 密钥算法
     */
    private static final String ALGORITHM = "AES";
    /**
     * 加解密算法/工作模式/填充方式
     */
    private static final String ALGORITHM_MODE_PADDING = "AES/ECB/PKCS7Padding";

    static {
        SignUtils.initBc();
    }


    /**
     * 解密
     *
     * @param content           密文
     * @param privateKey        商户私钥
     * @param characterEncoding 编码格式
     * @return 解密后的字符串
     * @throws GeneralSecurityException 解密异常
     * @throws IOException              IOException
     */
    public static String decrypt(String content, String privateKey, String characterEncoding) throws GeneralSecurityException, IOException {
        byte[] reqInfoB = Base64.decode(content);
        String key$ = DigestUtils.md5Hex(privateKey).toLowerCase();
        Cipher cipher = Cipher.getInstance(ALGORITHM_MODE_PADDING, "BC");
        SecretKeySpec secretKeySpec = new SecretKeySpec(key$.getBytes(), ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
        return new String(cipher.doFinal(reqInfoB), characterEncoding);
    }

    /**
     * 解密
     *
     * @param content           密文
     * @param privateKey        商户私钥
     * @param characterEncoding 编码格式
     * @return 解密后的字符串
     * @throws GeneralSecurityException 解密异常
     * @throws IOException              IOException
     */
    public static String encrypt(String content, String privateKey, String characterEncoding) throws GeneralSecurityException, IOException {
        String key$ = DigestUtils.md5Hex(privateKey).toLowerCase();
        Cipher cipher = Cipher.getInstance(ALGORITHM_MODE_PADDING, "BC");
        SecretKeySpec secretKeySpec = new SecretKeySpec(key$.getBytes(), ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
        byte[] doFinal = cipher.doFinal(content.getBytes(characterEncoding));
        return Base64.encode(doFinal);
    }

}
