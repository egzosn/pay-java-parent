package com.egzosn.pay.wx.v3.utils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.management.openmbean.InvalidKeyException;

import com.egzosn.pay.common.exception.PayErrorException;
import com.egzosn.pay.common.util.sign.SignUtils;
import com.egzosn.pay.common.util.sign.encrypt.Base64;
import com.egzosn.pay.wx.bean.WxPayError;
import com.egzosn.pay.wx.v3.bean.CertEnvironment;

/**
 * 证书文件可信校验
 *
 * @author egan
 * email egzosn@gmail.com
 * date 2021/07/18.20:29
 */
public final class AntCertificationUtil {

    /**
     * 微信平台证书容器  key = 序列号  value = 证书对象
     */
    private static final Map<String, Certificate> CERTIFICATE_MAP = new ConcurrentHashMap<>();

    private AntCertificationUtil() {
    }

    private static final KeyStore PKCS12_KEY_STORE;

    private static final CertificateFactory CERTIFICATE_FACTORY;

    static {
        String javaVersion = System.getProperty("java.version");
        if (javaVersion.contains("1.8") || javaVersion.startsWith("8")){
            Security.setProperty("crypto.policy", "unlimited");
        }
        SignUtils.initBc();
        try {
            PKCS12_KEY_STORE = KeyStore.getInstance("PKCS12");
        }
        catch (KeyStoreException e) {
            throw new PayErrorException(new WxPayError(WxConst.FAILURE, " keystore 初始化失败"), e);
        }

        try {
            CERTIFICATE_FACTORY = CertificateFactory.getInstance("X509", WxConst.BC_PROVIDER);
        }
        catch (NoSuchProviderException | CertificateException e) {
            throw new PayErrorException(new WxPayError(WxConst.FAILURE, " keystore 初始化失败"), e);
        }

    }


    /**
     * 装载平台证书
     *
     * @param serialNo          证书序列
     * @param certificateStream 证书流
     * @return 平台证书
     */
    public static Certificate loadCertificate(String serialNo, InputStream certificateStream) {
        try {
            Certificate certificate = CERTIFICATE_FACTORY.generateCertificate(certificateStream);
            CERTIFICATE_MAP.put(serialNo, certificate);
            return certificate;
        }
        catch (CertificateException e) {
            throw new PayErrorException(new WxPayError(WxConst.FAILURE, " 在生成微信v3证书时发生错误，原因是" + e.getMessage()), e);
        }

    }

    /**
     * 获取平台证书
     *
     * @param serialNo 证书序列
     * @return 平台证书
     */
    public static Certificate getCertificate(String serialNo) {
        return CERTIFICATE_MAP.get(serialNo);

    }

    /**
     * 获取公私钥.
     *
     * @param keyCertStream 商户API证书
     * @param keyAlias      证书别名
     * @param keyPass       证书对应的密码
     * @return 证书信息集合
     */
    public static CertEnvironment initCertification(InputStream keyCertStream, String keyAlias, String keyPass) {

        char[] pem = keyPass.toCharArray();
        try {
            PKCS12_KEY_STORE.load(keyCertStream, pem);
            X509Certificate certificate = (X509Certificate) PKCS12_KEY_STORE.getCertificate(keyAlias);
            certificate.checkValidity();
            String serialNumber = certificate.getSerialNumber().toString(16).toUpperCase();
            PublicKey publicKey = certificate.getPublicKey();
            PrivateKey privateKey = (PrivateKey) PKCS12_KEY_STORE.getKey(keyAlias, pem);
            return new CertEnvironment(privateKey, publicKey, serialNumber);
        }
        catch (InvalidKeyException e) {
            throw new PayErrorException(new WxPayError(WxConst.FAILURE, "获取公私钥失败， 解决方式：替换jre包：local_policy.jar，US_export_policy.jar"), e);
        }
        catch (GeneralSecurityException e) {
            throw new PayErrorException(new WxPayError(WxConst.FAILURE, "获取公私钥失败"), e);
        }
        catch (IOException e) {
            throw new PayErrorException(new WxPayError(WxConst.FAILURE, "私钥证书流加载失败"), e);
        }

    }


    /**
     * 解密响应体.
     *
     * @param associatedData    相关数据
     * @param nonce             随机串
     * @param cipherText        需要解密的文本
     * @param secretKey         密钥
     * @param characterEncoding 编码类型
     * @return 解密后的信息
     */
    public static String decryptToString(String associatedData, String nonce, String cipherText, String secretKey, String characterEncoding) {

        try {
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding", WxConst.BC_PROVIDER);
            SecretKeySpec key = new SecretKeySpec(secretKey.getBytes(Charset.forName(characterEncoding)), "AES");
            GCMParameterSpec spec = new GCMParameterSpec(128, nonce.getBytes(Charset.forName(characterEncoding)));
            cipher.init(Cipher.DECRYPT_MODE, key, spec);
            cipher.updateAAD(associatedData.getBytes(Charset.forName(characterEncoding)));
            byte[] bytes = cipher.doFinal(Base64.decode(cipherText));
            return new String(bytes, Charset.forName(characterEncoding));
        }
        catch (GeneralSecurityException e) {
            throw new PayErrorException(new WxPayError(WxConst.FAILURE, e.getMessage()), e);
        }
    }

    /**
     * 对请求敏感字段进行加密
     *
     * @param message     the message
     * @param certificate the certificate
     * @return 加密后的内容
     */
    public static String encryptToString(String message, Certificate certificate) {
        try {
            Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-1AndMGF1Padding", WxConst.BC_PROVIDER);
            cipher.init(Cipher.ENCRYPT_MODE, certificate.getPublicKey());

            byte[] data = message.getBytes(StandardCharsets.UTF_8);
            byte[] cipherData = cipher.doFinal(data);
            return Base64.encode(cipherData);

        }
        catch (GeneralSecurityException e) {
            throw new PayErrorException(new WxPayError(WxConst.FAILURE, e.getMessage()), e);
        }
    }


}
