package com.egzosn.pay.wx.v3.utils;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;

import com.egzosn.pay.common.exception.PayErrorException;
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
    private AntCertificationUtil() {
    }

    private static final KeyStore PKCS12_KEY_STORE;

    static {
        try {
            PKCS12_KEY_STORE = KeyStore.getInstance("PKCS12");
        }
        catch (KeyStoreException e) {
            throw new PayErrorException(new WxPayError("500", " keystore 初始化失败"));
        }
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
        catch (GeneralSecurityException e) {
            throw new PayErrorException(new WxPayError("500", "获取公私钥失败"), e);
        }
        catch (IOException e) {
            throw new PayErrorException(new WxPayError("500", "私钥证书流加载失败"), e);
        }

    }
}
