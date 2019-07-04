/**
 * Licensed Property to China UnionPay Co., Ltd.
 * <p>
 * (C) Copyright of China UnionPay Co., Ltd. 2010
 * All Rights Reserved.
 * <p>
 * <p>
 * Modification History:
 * =============================================================================
 * Author         Date          Description
 * ------------ ---------- ---------------------------------------------------
 * xshu       2014-05-28       证书工具类.
 * =============================================================================
 */
package com.egzosn.pay.common.util.sign;

import com.egzosn.pay.common.util.str.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Enumeration;


/**
 * acpsdk证书工具类，主要用于对证书的加载和使用
 * date 2016-7-22 下午2:46:20
 * 声明：以下代码只是为了方便接入方测试而提供的样例代码，商户可以根据自己需要，按照技术文档编写。该代码仅供参考，不提供编码，性能，规范性等方面的保障
 */
public class CertDescriptor {
    protected static final Log LOG = LogFactory.getLog(CertDescriptor.class);
    /**
     * 证书容器，存储对商户请求报文签名私钥证书.
     */
    private KeyStore keyStore = null;

    /**
     * 验签公钥/中级证书
     */
    private X509Certificate publicKeyCert = null;
    /**
     * 验签根证书
     */
    private X509Certificate rootKeyCert = null;

    public CertDescriptor() {
    }

    /**
     * 通过证书路径初始化为公钥证书
     *
     * @param certIn 证书流
     * @return X509 证书
     */
    private static X509Certificate initCert(InputStream certIn) {
        X509Certificate encryptCertTemp = null;
        CertificateFactory cf = null;
        try {
            cf = CertificateFactory.getInstance("X.509");
            encryptCertTemp = (X509Certificate) cf.generateCertificate(certIn);
            // 打印证书加载信息,供测试阶段调试
            if (LOG.isWarnEnabled()) {
                LOG.warn("[CertId=" + encryptCertTemp.getSerialNumber().toString() + "]");
            }
        } catch (CertificateException e) {
            LOG.error("InitCert Error", e);
        } finally {
            if (null != certIn) {
                try {
                    certIn.close();
                } catch (IOException e) {
                    LOG.error(e.toString());
                }
            }
        }
        return encryptCertTemp;
    }

    /**
     * 通过证书路径初始化为公钥证书
     *
     * @param path 证书地址
     * @return X509 证书
     */
    private static X509Certificate initCert(String path) {
        X509Certificate encryptCertTemp = null;
        CertificateFactory cf = null;
        FileInputStream in = null;
        try {
            in = new FileInputStream(path);
            encryptCertTemp = initCert(in);
        } catch (FileNotFoundException e) {
            LOG.error("InitCert Error File Not Found", e);
        }
        return encryptCertTemp;
    }

    /**
     * 通过keyStore 获取私钥签名证书PrivateKey对象
     *
     * @param pwd 证书对应密码
     * @return PrivateKey 私钥
     */
    public PrivateKey getSignCertPrivateKey(String pwd) {
        try {
            Enumeration<String> aliasenum = keyStore.aliases();
            String keyAlias = null;
            if (aliasenum.hasMoreElements()) {
                keyAlias = aliasenum.nextElement();
            }
            PrivateKey privateKey = (PrivateKey) keyStore.getKey(keyAlias,
                    pwd.toCharArray());
            return privateKey;
        } catch (KeyStoreException e) {
            LOG.error("getSignCertPrivateKey Error", e);
            return null;
        } catch (UnrecoverableKeyException e) {
            LOG.error("getSignCertPrivateKey Error", e);
            return null;
        } catch (NoSuchAlgorithmException e) {
            LOG.error("getSignCertPrivateKey Error", e);
            return null;
        }
    }


    /**
     * 配置的签名私钥证书certId
     *
     * @return 证书的物理编号
     */
    public String getSignCertId() {
        try {
            Enumeration<String> aliasenum = keyStore.aliases();
            String keyAlias = null;
            if (aliasenum.hasMoreElements()) {
                keyAlias = aliasenum.nextElement();
            }
            X509Certificate cert = (X509Certificate) keyStore.getCertificate(keyAlias);
            return cert.getSerialNumber().toString();
        } catch (Exception e) {
            LOG.error("getSignCertId Error", e);
            return null;
        }
    }


    /**
     * 将签名私钥证书文件读取为证书存储对象
     *
     * @param signCertPath 证书文件名
     * @param signCertPwd  证书密码
     * @param signCertType 证书类型
     */
    public void initPrivateSignCert(String signCertPath, String signCertPwd, String signCertType) {
        if (null != keyStore) {
            keyStore = null;
        }
        try {
            keyStore = getKeyInfo(signCertPath, signCertPwd, signCertType);
            if (LOG.isInfoEnabled()) {
                LOG.info("InitSignCert Successful. CertId=[" + getSignCertId() + "]");
            }
        } catch (IOException e) {
            LOG.error("InitSignCert Error", e);
        }
    }

    /**
     * 将签名私钥证书文件读取为证书存储对象
     *
     * @param signCert     证书文件
     * @param signCertPwd  证书密码
     * @param signCertType 证书类型
     */
    public void initPrivateSignCert(InputStream signCert, String signCertPwd, String signCertType) {

        if (null != keyStore) {
            keyStore = null;
        }
        keyStore = getKeyInfo(signCert, signCertPwd, signCertType);
        if (LOG.isInfoEnabled()) {
            LOG.info("InitSignCert Successful. CertId=[" + getSignCertId() + "]");
        }
    }

    /**
     * 将签名私钥证书文件读取为证书存储对象
     *
     * @param fxKeyFile 证书文件名
     * @param keyPwd    证书密码
     * @param type      证书类型
     * @return 证书对象
     * @throws IOException
     */
    private KeyStore getKeyInfo(String fxKeyFile, String keyPwd, String type) throws IOException {
        if (LOG.isWarnEnabled()) {
            LOG.warn("加载签名证书==>" + fxKeyFile);
        }
        FileInputStream fis = new FileInputStream(fxKeyFile);
        return getKeyInfo(fis, keyPwd, type);

    }

    /**
     * 将签名私钥证书文件读取为证书存储对象
     *
     * @param fxKeyFile 证书文件
     * @param keyPwd    证书密码
     * @param type      证书类型
     * @return 证书对象
     */
    public KeyStore getKeyInfo(InputStream fxKeyFile, String keyPwd, String type) {

        try {
            KeyStore ks = KeyStore.getInstance(type);
            if (LOG.isWarnEnabled()) {
                LOG.warn("Load RSA CertPath,Pwd=[" + keyPwd + "],type=[" + type + "]");
            }

            char[] nPassword = null;
            nPassword = null == keyPwd || "".equals(keyPwd.trim()) ? null : keyPwd.toCharArray();
            if (null != ks) {
                ks.load(fxKeyFile, nPassword);
            }
            return ks;
        } catch (Exception e) {
            LOG.error("getKeyInfo Error", e);
            return null;
        } finally {
            if (null != fxKeyFile) {
                try {
                    fxKeyFile.close();
                } catch (IOException e) {
                    LOG.error("getKeyInfo Error", e);
                }
            }
        }
    }


    /**
     * 通过keystore获取私钥证书的certId值
     *
     * @param keyStore
     * @return
     */
    private String getCertIdIdByStore(KeyStore keyStore) {
        Enumeration<String> aliasenum = null;
        try {
            aliasenum = keyStore.aliases();
            String keyAlias = null;
            if (aliasenum.hasMoreElements()) {
                keyAlias = aliasenum.nextElement();
            }
            X509Certificate cert = (X509Certificate) keyStore
                    .getCertificate(keyAlias);
            return cert.getSerialNumber().toString();
        } catch (KeyStoreException e) {
            LOG.error("getCertIdIdByStore Error", e);
            return null;
        }
    }


    /**
     * 加载中级证书
     *
     * @param certPath 证书地址
     */
    public void initPublicCert(String certPath) {
        if (!StringUtils.isEmpty(certPath)) {
            publicKeyCert = initCert(certPath);
            if (LOG.isInfoEnabled()) {
                LOG.info("Load PublicKeyCert Successful");
            }
        } else if (LOG.isInfoEnabled()) {
            LOG.info("PublicKeyCert is empty");
        }
    }

    /**
     * 加载中级证书
     *
     * @param cert 证书文件
     */
    public void initPublicCert(InputStream cert) {
        if (null != cert) {
            publicKeyCert = initCert(cert);
            if (LOG.isInfoEnabled()) {
                LOG.info("Load PublicKeyCert Successful");
            }
        } else if (LOG.isInfoEnabled()) {
            LOG.info("PublicKeyCert is empty");
        }
    }

    /**
     * 加载根证书
     *
     * @param certPath 证书地址
     */
    public void initRootCert(String certPath) {
        if (!StringUtils.isEmpty(certPath)) {
            try {
                initRootCert(new FileInputStream(certPath));
            } catch (FileNotFoundException e) {
                LOG.info("RootCert is empty");
            }

        } else if (LOG.isInfoEnabled()) {
            LOG.info("RootCert is empty");
        }
    }
    /**
     * 加载根证书
     *
     * @param cert 证书文件
     */
    public void initRootCert(InputStream cert) {
        if (null != cert) {
            rootKeyCert = initCert(cert);
            if (LOG.isInfoEnabled()) {
                LOG.info("Load RootCert Successful");
            }
        } else if (LOG.isInfoEnabled()) {
            LOG.info("RootCert is empty");
        }
    }

    /**
     * 获取公钥/中级证书
     *
     * @return X509Certificate
     */
    public X509Certificate getPublicCert() {
        return publicKeyCert;
    }

    /**
     * 获取中级证书
     *
     * @return X509Certificate
     */
    public X509Certificate getRootCert() {
        return rootKeyCert;
    }

}
