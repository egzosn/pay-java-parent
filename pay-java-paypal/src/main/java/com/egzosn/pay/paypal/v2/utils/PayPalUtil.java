package com.egzosn.pay.paypal.v2.utils;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.security.GeneralSecurityException;
import java.security.Signature;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.egzosn.pay.common.bean.result.PayException;
import com.egzosn.pay.common.exception.PayErrorException;

/**
 * @author Egan
 * email egan@egzosn.com
 * date 2023/9/12
 */
public final class PayPalUtil {
    private static final Logger LOG = LoggerFactory.getLogger(PayPalUtil.class);

    private PayPalUtil() {
    }

    public static Collection<X509Certificate> getCertificateFromStream(InputStream stream) {
        if (stream == null) {
            throw new PayErrorException(new PayException("failure", "未找到证书"));
        }
        try {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");

            return (Collection<X509Certificate>) cf.generateCertificates(stream);
        }
        catch (CertificateException ex) {
            throw new PayErrorException(new PayException("failure", "证书加载异常"), ex);
        }

    }

    /**
     * 生成字符串传递的CRC 32值
     *
     * @param data 字符
     * @return 返回长crc32输入值。-1如果string为null
     */
    public static long crc32(String data) {
        if (data == null) {
            return -1;
        }
        byte[] bytes = data.getBytes(Charset.forName("utf-8"));
        Checksum checksum = new CRC32();
        checksum.update(bytes, 0, bytes.length);
        return checksum.getValue();

    }


    /**
     * 基于https://developer.paypal.com/docs/integration/direct/rest-webhooks-overview/#event-signature验证Webhook签名验证，如果签名有效则返回true
     *
     * @param clientCerts            客户端证书
     * @param algo                   服务器生成签名时使用的算法
     * @param actualSignatureEncoded Paypal-Transmission-Sig服务器传递的报头值
     * @param expectedSignature      用请求体的CRC32值格式化数据生成的签名
     * @return true 校验通过
     */
    public static boolean validateData(Collection<X509Certificate> clientCerts, String algo, String actualSignatureEncoded, String expectedSignature) {
        // 从paypal-auth-algorithm HTTP头中获取signatureAlgorithm
        Signature signatureAlgorithm = null;
        try {
            signatureAlgorithm = Signature.getInstance(algo);
            //从HTTP头中提供的URL中获取certData并缓存它
            X509Certificate[] clientChain = clientCerts.toArray(new X509Certificate[0]);
            signatureAlgorithm.initVerify(clientChain[0].getPublicKey());
            signatureAlgorithm.update(expectedSignature.getBytes());
            // 实际的签名是base 64编码的，可以在HTTP头中找到
            byte[] actualSignature = Base64.decodeBase64(actualSignatureEncoded.getBytes());
            return signatureAlgorithm.verify(actualSignature);
        }
        catch (GeneralSecurityException e) {
            LOG.error("校验异常", e);
            return false;
        }

    }
}
