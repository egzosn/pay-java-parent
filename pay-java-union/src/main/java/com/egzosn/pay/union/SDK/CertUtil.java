/**
 *
 * Licensed Property to China UnionPay Co., Ltd.
 * 
 * (C) Copyright of China UnionPay Co., Ltd. 2010
 *     All Rights Reserved.
 *
 * 
 * Modification History:
 * =============================================================================
 *   Author         Date          Description
 *   ------------ ---------- ---------------------------------------------------
 *   xshu       2014-05-28       证书工具类.
 * =============================================================================
 */
package com.egzosn.pay.union.SDK;

import com.egzosn.pay.common.util.str.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.*;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.*;
import java.security.spec.RSAPublicKeySpec;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @ClassName: CertUtil
 * @Description: acpsdk证书工具类，主要用于对证书的加载和使用
 * @date 2016-7-22 下午2:46:20
 *
 */
public class CertUtil {
	/**
	 * 日志
	 */
	protected static final Log log = LogFactory.getLog(CertUtil.class);

	public static final String UNIONPAY_CNNAME = "中国银联股份有限公司";

	/** 证书容器，存储对商户请求报文签名私钥证书. */
	private static KeyStore keyStore = null;
	/** 敏感信息加密公钥证书 */
	private static X509Certificate encryptCert = null;
	/** 磁道加密公钥 */
	private static PublicKey encryptTrackKey = null;
	/** 验证银联返回报文签名证书. */
	private static X509Certificate validateCert = null;
	/** 验签中级证书 */
	private static X509Certificate middleCert = null;
	/** 验签根证书 */
	private static X509Certificate rootCert = null;
	/** 验证银联返回报文签名的公钥证书存储Map. */
	private static Map<String, X509Certificate> certMap = new HashMap<String, X509Certificate>();
	/** 商户私钥存储Map */
	private final static Map<String, KeyStore> keyStoreMap = new ConcurrentHashMap<String, KeyStore>();



	/**
	 * 请求报文签名(使用配置文件中配置的私钥证书或者对称密钥签名)<br>
	 * 功能：对请求报文进行签名,并计算赋值certid,signature字段并返回<br>
	 * @param reqData 请求报文map<br>
	 * @param encoding 上送请求报文域encoding字段的值<br>
	 * @return　签名后的map对象<br>
	 */
	public static Map<String, String> sign(Map<String, String> reqData,String encoding) {
		reqData = filterBlank(reqData);
		SDKUtils.signParams(reqData, encoding);
		return reqData;
	}
	/**
	 * 过滤请求报文中的空字符串或者空字符串
	 * @param contentData
	 * @return
	 */
	public static Map<String, String> filterBlank(Map<String, String> contentData){
		Map<String, String> submitFromData = new HashMap<String, String>();
		Set<String> keyset = contentData.keySet();
		for(String key:keyset){
			String value = contentData.get(key);
			if (StringUtils.isNotBlank(value)) {
				// 对value值进行去除前后空处理
				submitFromData.put(key, value.trim());
			}
		}
		return submitFromData;
	}





	static {
		init();
	}

	/**
	 * 初始化所有证书.
	 */
	private static void init() {
		try {
			addProvider();//向系统添加BC provider
			initSignCert();//初始化签名私钥证书
			initMiddleCert();//初始化验签证书的中级证书
			initRootCert();//初始化验签证书的根证书
			initEncryptCert();//初始化加密公钥
			initTrackKey();//构建磁道加密公钥
			initValidateCertFromDir();//初始化所有的验签证书
		} catch (Exception e) {
			e.printStackTrace();
            log.error("init失败。（如果是用对称密钥签名的可无视此异常。）", e);
		}
	}

	/**
	 * 添加签名，验签，加密算法提供者
	 */
	private static void addProvider(){
		if (Security.getProvider("BC") == null) {
            log.info("add BC provider");
			Security.addProvider(new BouncyCastleProvider());
		} else {
			Security.removeProvider("BC"); //解决eclipse调试时tomcat自动重新加载时，BC存在不明原因异常的问题。
			Security.addProvider(new BouncyCastleProvider());
			log.info("re-add BC provider");
		}
//		printSysInfo();
	}

	/**
	 * 用配置文件acp_sdk.properties中配置的私钥路径和密码 加载签名证书
	 */
	private static void initSignCert() {
		if(!"01".equals(SDKConfig.getConfig().getSignMethod())){
			log.info("非rsa签名方式，不加载签名证书。");
			return;
		}
		if (SDKConfig.getConfig().getSignCertPath() == null
				|| SDKConfig.getConfig().getSignCertPwd() == null
				|| SDKConfig.getConfig().getSignCertType() == null) {
			log.error("WARN: " + SDKConfig.SDK_SIGNCERT_PATH + "或" + SDKConfig.SDK_SIGNCERT_PWD
					+ "或" + SDKConfig.SDK_SIGNCERT_TYPE + "为空。 停止加载签名证书。");
			return;
		}
		if (null != keyStore) {
			keyStore = null;
		}
		try {
			keyStore = getKeyInfo(SDKConfig.getConfig().getSignCertPath(),
					SDKConfig.getConfig().getSignCertPwd(), SDKConfig
							.getConfig().getSignCertType());
			log.info("InitSignCert Successful. CertId=["
					+ getSignCertId() + "]");
		} catch (IOException e) {
			e.printStackTrace();
			log.error("InitSignCert Error", e);
		}
	}

	/**
	 * 用配置文件acp_sdk.properties配置路径 加载敏感信息加密证书
	 */
	private static void initMiddleCert() {
//		log.info("加载中级证书==>"+SDKConfig.getConfig().getMiddleCertPath());
		if (!StringUtils.isEmpty(SDKConfig.getConfig().getMiddleCertPath())) {
			middleCert = initCert(SDKConfig.getConfig().getMiddleCertPath());
			log.info("Load MiddleCert Successful");
		} else {
			log.info("WARN: acpsdk.middle.path is empty");
		}
	}

	/**
	 * 用配置文件acp_sdk.properties配置路径 加载敏感信息加密证书
	 */
	private static void initRootCert() {
//		log.info("加载根证书==>"+SDKConfig.getConfig().getRootCertPath());
		if (!StringUtils.isEmpty(SDKConfig.getConfig().getRootCertPath())) {
			rootCert = initCert(SDKConfig.getConfig().getRootCertPath());
			log.info("Load RootCert Successful");
		} else {
			log.info("WARN: acpsdk.rootCert.path is empty");
		}
	}

	/**
	 * 用配置文件acp_sdk.properties配置路径 加载银联公钥上级证书（中级证书）
	 */
	private static void initEncryptCert() {
//		log.info("加载敏感信息加密证书==>"+SDKConfig.getConfig().getEncryptCertPath());
		if (!StringUtils.isEmpty(SDKConfig.getConfig().getEncryptCertPath())) {
			encryptCert = initCert(SDKConfig.getConfig().getEncryptCertPath());
			log.info("Load EncryptCert Successful");
		} else {
			log.info("WARN: acpsdk.encryptCert.path is empty");
		}
	}

	/**
	 * 用配置文件acp_sdk.properties配置路径 加载磁道公钥
	 */
	private static void initTrackKey() {
		if (!StringUtils.isEmpty(SDKConfig.getConfig().getEncryptTrackKeyModulus())
				&& !StringUtils.isEmpty(SDKConfig.getConfig().getEncryptTrackKeyExponent())) {
			encryptTrackKey = getPublicKey(SDKConfig.getConfig().getEncryptTrackKeyModulus(),
					SDKConfig.getConfig().getEncryptTrackKeyExponent());
			log.info("LoadEncryptTrackKey Successful");
		} else {
			log.info("WARN: acpsdk.encryptTrackKey.modulus or acpsdk.encryptTrackKey.exponent is empty");
		}
	}

	/**
	 * 用配置文件acp_sdk.properties配置路径 加载验证签名证书
	 */
	private static void initValidateCertFromDir() {
		if(!"01".equals(SDKConfig.getConfig().getSignMethod())){
			log.info("非rsa签名方式，不加载验签证书。");
			return;
		}
		certMap.clear();
		String dir = SDKConfig.getConfig().getValidateCertDir();
		log.info("加载验证签名证书目录==>" + dir +" 注：如果请求报文中version=5.1.0那么此验签证书目录使用不到，可以不需要设置（version=5.0.0必须设置）。");
		if (StringUtils.isEmpty(dir)) {
			log.error("WARN: acpsdk.validateCert.dir is empty");
			return;
		}
		CertificateFactory cf = null;
		FileInputStream in = null;
		try {
			cf = CertificateFactory.getInstance("X.509", "BC");
		}catch (NoSuchProviderException e) {
			log.error("LoadVerifyCert Error: No BC Provider", e);
			return ;
		} catch (CertificateException e) {
			log.error("LoadVerifyCert Error", e);
			return ;
		}
		File fileDir = new File(dir);
		File[] files = fileDir.listFiles(new CerFilter());
		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			try {
				in = new FileInputStream(file.getAbsolutePath());
				validateCert = (X509Certificate) cf.generateCertificate(in);
				if(validateCert == null) {
					log.error("Load verify cert error, " + file.getAbsolutePath() + " has error cert content.");
					continue;
				}
				certMap.put(validateCert.getSerialNumber().toString(),
						validateCert);
				// 打印证书加载信息,供测试阶段调试
				log.info("[" + file.getAbsolutePath() + "][CertId="
						+ validateCert.getSerialNumber().toString() + "]");
			} catch (CertificateException e) {
				log.error("LoadVerifyCert Error", e);
				e.printStackTrace();
			}catch (FileNotFoundException e) {
				log.error("LoadVerifyCert Error File Not Found", e);
				e.printStackTrace();
			}finally {
				if (null != in) {
					try {
						in.close();
					} catch (IOException e) {
						log.error(e.toString());
						e.printStackTrace();
					}
				}
			}
		}
		log.info("LoadVerifyCert Finish");
	}

	/**
	 * 用给定的路径和密码 加载签名证书，并保存到certKeyStoreMap
	 *
	 * @param certFilePath
	 * @param certPwd
	 */
	private static void loadSignCert(String certFilePath, String certPwd) {
		KeyStore keyStore = null;
		try {
			keyStore = getKeyInfo(certFilePath, certPwd, "PKCS12");
			keyStoreMap.put(certFilePath, keyStore);
			log.info("LoadRsaCert Successful");
		} catch (IOException e) {
			log.error("LoadRsaCert Error", e);
			e.printStackTrace();
		}
	}

	/**
	 * 通过证书路径初始化为公钥证书
	 * @param path
	 * @return
	 */
	private static X509Certificate initCert(String path) {
		X509Certificate encryptCertTemp = null;
		CertificateFactory cf = null;
		FileInputStream in = null;
		try {
			cf = CertificateFactory.getInstance("X.509", "BC");
			in = new FileInputStream(path);
			encryptCertTemp = (X509Certificate) cf.generateCertificate(in);
			// 打印证书加载信息,供测试阶段调试
			log.info("[" + path + "][CertId="
					+ encryptCertTemp.getSerialNumber().toString() + "]");
		} catch (CertificateException e) {
			log.error("InitCert Error", e);
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			log.error("InitCert Error File Not Found", e);
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
			log.error("LoadVerifyCert Error No BC Provider", e);
		} finally {
			if (null != in) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
					log.error(e.toString());
				}
			}
		}
		return encryptCertTemp;
	}

	/**
	 * 通过keyStore 获取私钥签名证书PrivateKey对象
	 *
	 * @return
	 */
	public static PrivateKey getSignCertPrivateKey() {
		try {
			Enumeration<String> aliasenum = keyStore.aliases();
			String keyAlias = null;
			if (aliasenum.hasMoreElements()) {
				keyAlias = aliasenum.nextElement();
			}
			PrivateKey privateKey = (PrivateKey) keyStore.getKey(keyAlias,
					SDKConfig.getConfig().getSignCertPwd().toCharArray());
			return privateKey;
		} catch (KeyStoreException e) {
			e.printStackTrace();
			log.error("getSignCertPrivateKey Error", e);
			return null;
		} catch (UnrecoverableKeyException e) {
			e.printStackTrace();
			log.error("getSignCertPrivateKey Error", e);
			return null;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			log.error("getSignCertPrivateKey Error", e);
			return null;
		}
	}
	/**
	 * 通过指定路径的私钥证书  获取PrivateKey对象
	 *
	 * @return
	 */
	public static PrivateKey getSignCertPrivateKeyByStoreMap(String certPath,
                                                             String certPwd) {
		if (!keyStoreMap.containsKey(certPath)) {
			loadSignCert(certPath, certPwd);
		}
		try {
			Enumeration<String> aliasenum = keyStoreMap.get(certPath)
					.aliases();
			String keyAlias = null;
			if (aliasenum.hasMoreElements()) {
				keyAlias = aliasenum.nextElement();
			}
			PrivateKey privateKey = (PrivateKey) keyStoreMap.get(certPath)
					.getKey(keyAlias, certPwd.toCharArray());
			return privateKey;
		} catch (KeyStoreException e) {
			e.printStackTrace();
			log.error("getSignCertPrivateKeyByStoreMap Error", e);
			return null;
		} catch (UnrecoverableKeyException e) {
			e.printStackTrace();
			log.error("getSignCertPrivateKeyByStoreMap Error", e);
			return null;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			log.error("getSignCertPrivateKeyByStoreMap Error", e);
			return null;
		}
	}

	/**
	 * 获取敏感信息加密证书PublicKey
	 *
	 * @return
	 */
	public static PublicKey getEncryptCertPublicKey() {
		if (null == encryptCert) {
			String path = SDKConfig.getConfig().getEncryptCertPath();
			if (!StringUtils.isEmpty(path)) {
				encryptCert = initCert(path);
				return encryptCert.getPublicKey();
			} else {
				log.error("acpsdk.encryptCert.path is empty");
				return null;
			}
		} else {
			return encryptCert.getPublicKey();
		}
	}

	/**
	 * 重置敏感信息加密证书公钥
	 */
	public static void resetEncryptCertPublicKey() {
		encryptCert = null;
	}

	/**
	 * 获取磁道加密证书PublicKey
	 *
	 * @return
	 */
	public static PublicKey getEncryptTrackPublicKey() {
		if (null == encryptTrackKey) {
			initTrackKey();
		}
		return encryptTrackKey;
	}

	/**
	 * 通过certId获取验签证书Map中对应证书PublicKey
	 *
	 * @param certId 证书物理序号
	 * @return 通过证书编号获取到的公钥
	 */
	public static PublicKey getValidatePublicKey(String certId) {
		X509Certificate cf = null;
		if (certMap.containsKey(certId)) {
			// 存在certId对应的证书对象
			cf = certMap.get(certId);
			return cf.getPublicKey();
		} else {
			// 不存在则重新Load证书文件目录
			initValidateCertFromDir();
			if (certMap.containsKey(certId)) {
				// 存在certId对应的证书对象
				cf = certMap.get(certId);
				return cf.getPublicKey();
			} else {
				log.error("缺少certId=[" + certId + "]对应的验签证书.");
				return null;
			}
		}
	}

	/**
	 * 获取配置文件acp_sdk.properties中配置的签名私钥证书certId
	 *
	 * @return 证书的物理编号
	 */
	public static String getSignCertId() {
		try {
			Enumeration<String> aliasenum = keyStore.aliases();
			String keyAlias = null;
			if (aliasenum.hasMoreElements()) {
				keyAlias = aliasenum.nextElement();
			}
			X509Certificate cert = (X509Certificate) keyStore
					.getCertificate(keyAlias);
			return cert.getSerialNumber().toString();
		} catch (Exception e) {
			log.error("getSignCertId Error", e);
			return null;
		}
	}

	/**
	 * 获取敏感信息加密证书的certId
	 *
	 * @return
	 */
	public static String getEncryptCertId() {
		if (null == encryptCert) {
			String path = SDKConfig.getConfig().getEncryptCertPath();
			if (!StringUtils.isEmpty(path)) {
				encryptCert = initCert(path);
				return encryptCert.getSerialNumber().toString();
			} else {
				log.error("acpsdk.encryptCert.path is empty");
				return null;
			}
		} else {
			return encryptCert.getSerialNumber().toString();
		}
	}

	/**
	 * 将签名私钥证书文件读取为证书存储对象
	 *
	 * @param pfxkeyfile
	 *            证书文件名
	 * @param keypwd
	 *            证书密码
	 * @param type
	 *            证书类型
	 * @return 证书对象
	 * @throws IOException
	 */
	private static KeyStore getKeyInfo(String pfxkeyfile, String keypwd,
                                       String type) throws IOException {
		log.info("加载签名证书==>" + pfxkeyfile);
		FileInputStream fis = null;
		try {
			KeyStore ks = KeyStore.getInstance(type, "BC");
			log.info("Load RSA CertPath=[" + pfxkeyfile + "],Pwd=["+ keypwd + "],type=["+type+"]");
			fis = new FileInputStream(pfxkeyfile);
			char[] nPassword = null;
			nPassword = null == keypwd || "".equals(keypwd.trim()) ? null: keypwd.toCharArray();
			if (null != ks) {
				ks.load(fis, nPassword);
			}
			return ks;
		} catch (Exception e) {
			log.error("getKeyInfo Error", e);
			e.printStackTrace();
			return null;
		} finally {
			if(null!=fis)
				fis.close();
		}
	}

	/**
	 * 通过签名私钥证书路径，密码获取私钥证书certId
	 * @param certPath
	 * @param certPwd
	 * @return
	 */
	public static String getCertIdByKeyStoreMap(String certPath, String certPwd) {
		if (!keyStoreMap.containsKey(certPath)) {
			// 缓存中未查询到,则加载RSA证书
			loadSignCert(certPath, certPwd);
		}
		return getCertIdIdByStore(keyStoreMap.get(certPath));
	}

	/**
	 * 通过keystore获取私钥证书的certId值
	 * @param keyStore
	 * @return
	 */
	private static String getCertIdIdByStore(KeyStore keyStore) {
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
			log.error("getCertIdIdByStore Error", e);
			return null;
		}
	}

	/**
	 * 使用模和指数生成RSA公钥 注意：此代码用了默认补位方式，为RSA/None/PKCS1Padding，不同JDK默认的补位方式可能不同
	 *
	 * @param modulus
	 *            模
	 * @param exponent
	 *            指数
	 * @return
	 */
	private static PublicKey getPublicKey(String modulus, String exponent) {
		try {
			BigInteger b1 = new BigInteger(modulus);
			BigInteger b2 = new BigInteger(exponent);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA", "BC");
			RSAPublicKeySpec keySpec = new RSAPublicKeySpec(b1, b2);
			return keyFactory.generatePublic(keySpec);
		} catch (Exception e) {
			log.error("构造RSA公钥失败：" + e);
			return null;
		}
	}

	/**
	 * 将字符串转换为X509Certificate对象.
	 *
	 * @param x509CertString
	 * @return
	 */
	public static X509Certificate genCertificateByStr(String x509CertString) {
		X509Certificate x509Cert = null;
		try {
			CertificateFactory cf = CertificateFactory.getInstance("X.509", "BC");
			InputStream tIn = new ByteArrayInputStream(
					x509CertString.getBytes("ISO-8859-1"));
			x509Cert = (X509Certificate) cf.generateCertificate(tIn);
		} catch (Exception e) {
			log.error("gen certificate error", e);
		}
		return x509Cert;
	}

	/**
	 * 从配置文件acp_sdk.properties中获取验签公钥使用的中级证书
	 * @return
	 */
	public static X509Certificate getMiddleCert() {
		if (null == middleCert) {
			String path = SDKConfig.getConfig().getMiddleCertPath();
			if (!StringUtils.isEmpty(path)) {
				initMiddleCert();
			} else {
				log.error(SDKConfig.SDK_MIDDLECERT_PATH + " not set in " + SDKConfig.FILE_NAME);
				return null;
			}
		}
		return middleCert;
	}

	/**
	 * 从配置文件acp_sdk.properties中获取验签公钥使用的根证书
	 * @return
	 */
	public static X509Certificate getRootCert() {
		if (null == rootCert) {
			String path = SDKConfig.getConfig().getRootCertPath();
			if (!StringUtils.isEmpty(path)) {
				initRootCert();
			} else {
				log.error(SDKConfig.SDK_ROOTCERT_PATH + " not set in " + SDKConfig.FILE_NAME);
				return null;
			}
		}
		return rootCert;
	}

	/**
	 * 获取证书的CN
	 * @param aCert
	 * @return
	 */
	private static String getIdentitiesFromCertficate(X509Certificate aCert) {
		String tDN = aCert.getSubjectDN().toString();
		String tPart = "";
		if ((tDN != null)) {
			String tSplitStr[] = tDN.substring(tDN.indexOf("CN=")).split("@");
			if (tSplitStr != null && tSplitStr.length > 2
					&& tSplitStr[2] != null)
				tPart = tSplitStr[2];
		}
		return tPart;
	}

	/**
	 * 验证书链。
	 * @param cert
	 * @return
	 */
	private static boolean verifyCertificateChain(X509Certificate cert){

		if ( null == cert) {
			log.error("cert must Not null");
			return false;
		}

		X509Certificate middleCert = CertUtil.getMiddleCert();
		if (null == middleCert) {
			log.error("middleCert must Not null");
			return false;
		}

		X509Certificate rootCert = CertUtil.getRootCert();
		if (null == rootCert) {
			log.error("rootCert or cert must Not null");
			return false;
		}

		try {

	        X509CertSelector selector = new X509CertSelector();
	        selector.setCertificate(cert);

	        Set<TrustAnchor> trustAnchors = new HashSet<TrustAnchor>();
	        trustAnchors.add(new TrustAnchor(rootCert, null));
	        PKIXBuilderParameters pkixParams = new PKIXBuilderParameters(
			        trustAnchors, selector);

	        Set<X509Certificate> intermediateCerts = new HashSet<X509Certificate>();
	        intermediateCerts.add(rootCert);
	        intermediateCerts.add(middleCert);
	        intermediateCerts.add(cert);

	        pkixParams.setRevocationEnabled(false);

	        CertStore intermediateCertStore = CertStore.getInstance("Collection",
	                new CollectionCertStoreParameters(intermediateCerts), "BC");
	        pkixParams.addCertStore(intermediateCertStore);

	        CertPathBuilder builder = CertPathBuilder.getInstance("PKIX", "BC");

        	@SuppressWarnings("unused")
            PKIXCertPathBuilderResult result = (PKIXCertPathBuilderResult) builder
                .build(pkixParams);
			log.info("verify certificate chain succeed.");
			return true;
        } catch (CertPathBuilderException e){
			log.error("verify certificate chain fail.", e);
		} catch (Exception e) {
			log.error("verify certificate chain exception: ", e);
		}
		return false;
	}

	/**
	 * 检查证书链
	 *
	 * @param cert
	 *            待验证的证书
	 * @return
	 */
	public static boolean verifyCertificate(X509Certificate cert) {

		if ( null == cert) {
			log.error("cert must Not null");
			return false;
		}
		try {
			cert.checkValidity();//验证有效期
//			cert.verify(middleCert.getPublicKey());
			if(!verifyCertificateChain(cert)){
				return false;
			}
		} catch (Exception e) {
			log.error("verifyCertificate fail", e);
			return false;
		}

		if(SDKConfig.getConfig().isIfValidateCNName()){
			// 验证公钥是否属于银联
			if(!UNIONPAY_CNNAME.equals(CertUtil.getIdentitiesFromCertficate(cert))) {
				log.error("cer owner is not CUP:" + CertUtil.getIdentitiesFromCertficate(cert));
				return false;
			}
		} else {
			// 验证公钥是否属于银联
			if(!UNIONPAY_CNNAME.equals(CertUtil.getIdentitiesFromCertficate(cert))
					&& !"00040000:SIGN".equals(CertUtil.getIdentitiesFromCertficate(cert))) {
				log.error("cer owner is not CUP:" + CertUtil.getIdentitiesFromCertficate(cert));
				return false;
			}
		}
		return true;
	}

	/**
	 * 打印系统环境信息
	 */
	private static void printSysInfo() {
		log.info("================= SYS INFO begin====================");
		log.info("os_name:" + System.getProperty("os.name"));
		log.info("os_arch:" + System.getProperty("os.arch"));
		log.info("os_version:" + System.getProperty("os.version"));
		log.info("java_vm_specification_version:"
				+ System.getProperty("java.vm.specification.version"));
		log.info("java_vm_specification_vendor:"
				+ System.getProperty("java.vm.specification.vendor"));
		log.info("java_vm_specification_name:"
				+ System.getProperty("java.vm.specification.name"));
		log.info("java_vm_version:"
				+ System.getProperty("java.vm.version"));
		log.info("java_vm_name:" + System.getProperty("java.vm.name"));
		log.info("java.version:" + System.getProperty("java.version"));
		log.info("java.vm.vendor=[" + System.getProperty("java.vm.vendor") + "]");
		log.info("java.version=[" + System.getProperty("java.version") + "]");
//		printProviders();
		log.info("================= SYS INFO end=====================");
	}

	/**
	 * 打jre中印算法提供者列表
	 */
	private static void printProviders() {
		log.info("Providers List:");
		Provider[] providers = Security.getProviders();
		for (int i = 0; i < providers.length; i++) {
			log.info(i + 1 + "." + providers[i].getName());
		}
	}

	/**
	 * 证书文件过滤器
	 *
	 */
	static class CerFilter implements FilenameFilter {
		public boolean isCer(String name) {
			if (name.toLowerCase().endsWith(".cer")) {
				return true;
			} else {
				return false;
			}
		}
		@Override
		public boolean accept(File dir, String name) {
			return isCer(name);
		}
	}

}
