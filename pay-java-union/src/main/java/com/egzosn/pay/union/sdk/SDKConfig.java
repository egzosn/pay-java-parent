package com.egzosn.pay.union.sdk;

import com.egzosn.pay.common.util.str.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;
import java.util.Properties;

/**
 * 
 * @ClassName SDKConfig
 * @Description acpsdk配置文件acp_sdk.properties配置信息类
 * @date 2016-7-22 下午4:04:55
 * @lastUpdate Actinia Date:2017/11/7 0007 21:55
 *
 */
public class SDKConfig {
	protected final Log log = LogFactory.getLog(SDKConfig.class);

	public static final String FILE_NAME = "acp_sdk.properties";
	/** 前台请求URL. */
	private String frontRequestUrl;
	/** 后台请求URL. */
	private String backRequestUrl;
	/** 单笔查询 */
	private String singleQueryUrl;
	/** 批量查询 */
	private String batchQueryUrl;
	/** 批量交易 */
	private String batchTransUrl;
	/** 文件传输 */
	private String fileTransUrl;
	/** 签名证书路径. */
	private String signCertPath;
	/** 签名证书密码. */
	private String signCertPwd;
	/** 签名证书类型. */
	private String signCertType;
	/** 加密公钥证书路径. */
	private String encryptCertPath;
	/** 验证签名公钥证书目录. */
	private String validateCertDir;
	/** 按照商户代码读取指定签名证书目录. */
	private String signCertDir;
	/** 磁道加密证书路径. */
	private String encryptTrackCertPath;
	/** 磁道加密公钥模数. */
	private String encryptTrackKeyModulus;
	/** 磁道加密公钥指数. */
	private String encryptTrackKeyExponent;
	/** 有卡交易. */
	private String cardRequestUrl;
	/** app交易 */
	private String appRequestUrl;
	/** 证书使用模式(单证书/多证书) */
	private String singleMode;
	/** 安全密钥(SHA256和SM3计算时使用) */
	private String secureKey;
	/** 中级证书路径  */
	private String middleCertPath;
	/** 根证书路径  */
	private String rootCertPath;
	/** 是否验证验签证书CN，除了false都验  */
	private boolean ifValidateCNName = true;
	/** 是否验证https证书，默认都不验  */
	private boolean ifValidateRemoteCert = false;
	/** signMethod，没配按01吧  */
	private String signMethod = "01";

	/** frontUrl  */
	private String frontUrl;
	/** backUrl  */
	private String backUrl;

	/*缴费相关地址*/
	private String jfFrontRequestUrl;
	private String jfBackRequestUrl;
	private String jfSingleQueryUrl;
	private String jfCardRequestUrl;
	private String jfAppRequestUrl;
	
	private String qrcBackTransUrl;
	private String qrcB2cIssBackTransUrl;
	private String qrcB2cMerBackTransUrl;

	/** 配置文件中的前台URL常量. */
	public static final String SDK_FRONT_URL = "acpsdk.frontTransUrl";
	/** 配置文件中的后台URL常量. */
	public static final String SDK_BACK_URL = "acpsdk.backTransUrl";
	/** 配置文件中的单笔交易查询URL常量. */
	public static final String SDK_SIGNQ_URL = "acpsdk.singleQueryUrl";
	/** 配置文件中的批量交易查询URL常量. */
	public static final String SDK_BATQ_URL = "acpsdk.batchQueryUrl";
	/** 配置文件中的批量交易URL常量. */
	public static final String SDK_BATTRANS_URL = "acpsdk.batchTransUrl";
	/** 配置文件中的文件类交易URL常量. */
	public static final String SDK_FILETRANS_URL = "acpsdk.fileTransUrl";
	/** 配置文件中的有卡交易URL常量. */
	public static final String SDK_CARD_URL = "acpsdk.cardTransUrl";
	/** 配置文件中的app交易URL常量. */
	public static final String SDK_APP_URL = "acpsdk.appTransUrl";

	/** 以下缴费产品使用，其余产品用不到，无视即可 */
	// 前台请求地址
	public static final String JF_SDK_FRONT_TRANS_URL= "acpsdk.jfFrontTransUrl";
	// 后台请求地址
	public static final String JF_SDK_BACK_TRANS_URL="acpsdk.jfBackTransUrl";
	// 单笔查询请求地址
	public static final String JF_SDK_SINGLE_QUERY_URL="acpsdk.jfSingleQueryUrl";
	// 有卡交易地址
	public static final String JF_SDK_CARD_TRANS_URL="acpsdk.jfCardTransUrl";
	// App交易地址
	public static final String JF_SDK_APP_TRANS_URL="acpsdk.jfAppTransUrl";
	// 人到人
	public static final String QRC_BACK_TRANS_URL="acpsdk.qrcBackTransUrl";
	// 人到人
	public static final String QRC_B2C_ISS_BACK_TRANS_URL="acpsdk.qrcB2cIssBackTransUrl";
	// 人到人
	public static final String QRC_B2C_MER_BACK_TRANS_URL="acpsdk.qrcB2cMerBackTransUrl";
	
	
	/** 配置文件中签名证书路径常量. */
	public static final String SDK_SIGNCERT_PATH = "acpsdk.signCert.path";
	/** 配置文件中签名证书密码常量. */
	public static final String SDK_SIGNCERT_PWD = "acpsdk.signCert.pwd";
	/** 配置文件中签名证书类型常量. */
	public static final String SDK_SIGNCERT_TYPE = "acpsdk.signCert.type";
	/** 配置文件中密码加密证书路径常量. */
	public static final String SDK_ENCRYPTCERT_PATH = "acpsdk.encryptCert.path";
	/** 配置文件中磁道加密证书路径常量. */
	public static final String SDK_ENCRYPTTRACKCERT_PATH = "acpsdk.encryptTrackCert.path";
	/** 配置文件中磁道加密公钥模数常量. */
	public static final String SDK_ENCRYPTTRACKKEY_MODULUS = "acpsdk.encryptTrackKey.modulus";
	/** 配置文件中磁道加密公钥指数常量. */
	public static final String SDK_ENCRYPTTRACKKEY_EXPONENT = "acpsdk.encryptTrackKey.exponent";
	/** 配置文件中验证签名证书目录常量. */
	public static final String SDK_VALIDATECERT_DIR = "acpsdk.validateCert.dir";

	/** 配置文件中是否加密cvn2常量. */
	public static final String SDK_CVN_ENC = "acpsdk.cvn2.enc";
	/** 配置文件中是否加密cvn2有效期常量. */
	public static final String SDK_DATE_ENC = "acpsdk.date.enc";
	/** 配置文件中是否加密卡号常量. */
	public static final String SDK_PAN_ENC = "acpsdk.pan.enc";
	/** 配置文件中证书使用模式 */
	public static final String SDK_SINGLEMODE = "acpsdk.singleMode";
	/** 配置文件中安全密钥 */
	public static final String SDK_SECURITYKEY = "acpsdk.secureKey";
	/** 配置文件中根证书路径常量  */
	public static final String SDK_ROOTCERT_PATH = "acpsdk.rootCert.path";
	/** 配置文件中根证书路径常量  */
	public static final String SDK_MIDDLECERT_PATH = "acpsdk.middleCert.path";
	/** 配置是否需要验证验签证书CN，除了false之外的值都当true处理 */
	public static final String SDK_IF_VALIDATE_CN_NAME = "acpsdk.ifValidateCNName";
	/** 配置是否需要验证https证书，除了true之外的值都当false处理 */
	public static final String SDK_IF_VALIDATE_REMOTE_CERT = "acpsdk.ifValidateRemoteCert";
	/** signmethod */
	public static final String SDK_SIGN_METHOD ="acpsdk.signMethod";
	/** version */
	public static final String SDK_VERSION = "acpsdk.version";
	/** 后台通知地址  */
	public static final String SDK_BACKURL = "acpsdk.backUrl";
	/** 前台通知地址  */
	public static final String SDK_FRONTURL = "acpsdk.frontUrl";
	/** 操作对象. */
	private static SDKConfig config = new SDKConfig();
	/** 属性文件对象. */
	private Properties properties;


	private SDKConfig () {
		super();
	}

	/**
	 * 获取config对象.
	 * @return
	 */
	public static SDKConfig getConfig() {
		return config;
	}

	/**
	 * 从classpath路径下加载配置参数
	 */
	public void loadPropertiesFromSrc() {
		InputStream in = null;
		try {
			log.info("从classpath: " +SDKConfig.class.getClassLoader().getResource("").getPath()+" 获取属性文件"+FILE_NAME);
			in = SDKConfig.class.getClassLoader().getResourceAsStream(FILE_NAME);
			if (null != in) {
				properties = new Properties();
				try {
					properties.load(in);
				} catch (IOException e) {
					throw e;
				}
			} else {
				log.info(FILE_NAME + "属性文件未能在classpath指定的目录下 "+SDKConfig.class.getClassLoader().getResource("").getPath()+" 找到!");
				return;
			}
			loadProperties(properties);
		} catch (IOException e) {
			log.info(e.getMessage(), e);
		} finally {
			if (null != in) {
				try {
					in.close();
				} catch (IOException e) {
					log.info(e.getMessage(), e);
				}
			}
		}
	}

	/**
	 * 根据传入的 {@link #loadProperties(java.util.Properties)}对象设置配置参数
	 * 
	 * @param pro
	 */
	public void loadProperties(Properties pro) {
		log.info("开始从属性文件中加载配置项");
		String value = null;
		
		value = pro.getProperty(SDK_SIGNCERT_PATH);
		if (!StringUtils.isEmpty(value)) {
			this.signCertPath = value.trim();
		}
		value = pro.getProperty(SDK_SIGNCERT_PWD);
		if (!StringUtils.isEmpty(value)) {
			this.signCertPwd = value.trim();
		}
		value = pro.getProperty(SDK_SIGNCERT_TYPE);
		if (!StringUtils.isEmpty(value)) {
			this.signCertType = value.trim();
		}
		value = pro.getProperty(SDK_ENCRYPTCERT_PATH);
		if (!StringUtils.isEmpty(value)) {
			this.encryptCertPath = value.trim();
		}
		value = pro.getProperty(SDK_VALIDATECERT_DIR);
		if (!StringUtils.isEmpty(value)) {
			this.validateCertDir = value.trim();
		}
		value = pro.getProperty(SDK_FRONT_URL);
		if (!StringUtils.isEmpty(value)) {
			this.frontRequestUrl = value.trim();
		}
		value = pro.getProperty(SDK_BACK_URL);
		if (!StringUtils.isEmpty(value)) {
			this.backRequestUrl = value.trim();
		}
		value = pro.getProperty(SDK_BATQ_URL);
		if (!StringUtils.isEmpty(value)) {
			this.batchQueryUrl = value.trim();
		}
		value = pro.getProperty(SDK_BATTRANS_URL);
		if (!StringUtils.isEmpty(value)) {
			this.batchTransUrl = value.trim();
		}
		value = pro.getProperty(SDK_FILETRANS_URL);
		if (!StringUtils.isEmpty(value)) {
			this.fileTransUrl = value.trim();
		}
		value = pro.getProperty(SDK_SIGNQ_URL);
		if (!StringUtils.isEmpty(value)) {
			this.singleQueryUrl = value.trim();
		}
		value = pro.getProperty(SDK_CARD_URL);
		if (!StringUtils.isEmpty(value)) {
			this.cardRequestUrl = value.trim();
		}
		value = pro.getProperty(SDK_APP_URL);
		if (!StringUtils.isEmpty(value)) {
			this.appRequestUrl = value.trim();
		}
		value = pro.getProperty(SDK_ENCRYPTTRACKCERT_PATH);
		if (!StringUtils.isEmpty(value)) {
			this.encryptTrackCertPath = value.trim();
		}

		value = pro.getProperty(SDK_SECURITYKEY);
		if (!StringUtils.isEmpty(value)) {
			this.secureKey = value.trim();
		}
		value = pro.getProperty(SDK_ROOTCERT_PATH);
		if (!StringUtils.isEmpty(value)) {
			this.rootCertPath = value.trim();
		}
		value = pro.getProperty(SDK_MIDDLECERT_PATH);
		if (!StringUtils.isEmpty(value)) {
			this.middleCertPath = value.trim();
		}

		/**缴费部分**/
		value = pro.getProperty(JF_SDK_FRONT_TRANS_URL);
		if (!StringUtils.isEmpty(value)) {
			this.jfFrontRequestUrl = value.trim();
		}

		value = pro.getProperty(JF_SDK_BACK_TRANS_URL);
		if (!StringUtils.isEmpty(value)) {
			this.jfBackRequestUrl = value.trim();
		}
		
		value = pro.getProperty(JF_SDK_SINGLE_QUERY_URL);
		if (!StringUtils.isEmpty(value)) {
			this.jfSingleQueryUrl = value.trim();
		}
		
		value = pro.getProperty(JF_SDK_CARD_TRANS_URL);
		if (!StringUtils.isEmpty(value)) {
			this.jfCardRequestUrl = value.trim();
		}
		
		value = pro.getProperty(JF_SDK_APP_TRANS_URL);
		if (!StringUtils.isEmpty(value)) {
			this.jfAppRequestUrl = value.trim();
		}
		
		value = pro.getProperty(QRC_BACK_TRANS_URL);
		if (!StringUtils.isEmpty(value)) {
			this.qrcBackTransUrl = value.trim();
		}
		
		value = pro.getProperty(QRC_B2C_ISS_BACK_TRANS_URL);
		if (!StringUtils.isEmpty(value)) {
			this.qrcB2cIssBackTransUrl = value.trim();
		}
		
		value = pro.getProperty(QRC_B2C_MER_BACK_TRANS_URL);
		if (!StringUtils.isEmpty(value)) {
			this.qrcB2cMerBackTransUrl = value.trim();
		}

		value = pro.getProperty(SDK_ENCRYPTTRACKKEY_EXPONENT);
		if (!StringUtils.isEmpty(value)) {
			this.encryptTrackKeyExponent = value.trim();
		}

		value = pro.getProperty(SDK_ENCRYPTTRACKKEY_MODULUS);
		if (!StringUtils.isEmpty(value)) {
			this.encryptTrackKeyModulus = value.trim();
		}
		
		value = pro.getProperty(SDK_IF_VALIDATE_CN_NAME);
		if (!StringUtils.isEmpty(value)) {
			if( "false".equals(value.trim())) {
				this.ifValidateCNName = false;
			}
		}
		
		value = pro.getProperty(SDK_IF_VALIDATE_REMOTE_CERT);
		if (!StringUtils.isEmpty(value)) {
			if( "true".equals(value.trim())) {
				this.ifValidateRemoteCert = true;
			}
		}
		
		value = pro.getProperty(SDK_SIGN_METHOD);
		if (!StringUtils.isEmpty(value)) {
			this.signMethod = value.trim();
		}
		
		value = pro.getProperty(SDK_SIGN_METHOD);
		if (!StringUtils.isEmpty(value)) {
			this.signMethod = value.trim();
		}

		value = pro.getProperty(SDK_FRONTURL);
		if (!StringUtils.isEmpty(value)) {
			this.frontUrl = value.trim();
		}
		value = pro.getProperty(SDK_BACKURL);
		if (!StringUtils.isEmpty(value)) {
			this.backUrl = value.trim();
		}
	}

	public String getSignMethod () {
		return signMethod;
	}

	public void setSignMethod (String signMethod) {
		this.signMethod = signMethod;
	}

	public String getSignMethodByStr(String signStr) {
		if(StringUtils.isBlank(signStr)){
			return SDKConstants.SIGNMETHOD_RSA;
		}
		signStr = signStr.toUpperCase();
		switch (signStr) {
			case "RSA":
				return SDKConstants.SIGNMETHOD_RSA;
			case "SHA256":
				return SDKConstants.SIGNMETHOD_SHA256;
			case "sm3":
				return SDKConstants.SIGNMETHOD_SM3;
			default:
				return SDKConstants.SIGNMETHOD_RSA;
		}
	}

	public String getFrontRequestUrl() {
		return frontRequestUrl;
	}

	public void setFrontRequestUrl(String frontRequestUrl) {
		this.frontRequestUrl = frontRequestUrl;
	}

	public String getBackRequestUrl() {
		return backRequestUrl;
	}

	public void setBackRequestUrl(String backRequestUrl) {
		this.backRequestUrl = backRequestUrl;
	}

	public String getSignCertPath() {
		return signCertPath;
	}

	public void setSignCertPath(String signCertPath) {
		this.signCertPath = signCertPath;
	}

	public String getSignCertPwd() {
		return signCertPwd;
	}

	public void setSignCertPwd(String signCertPwd) {
		this.signCertPwd = signCertPwd;
	}

	public String getSignCertType() {
		return signCertType;
	}

	public void setSignCertType(String signCertType) {
		this.signCertType = signCertType;
	}

	public String getEncryptCertPath() {
		return encryptCertPath;
	}

	public void setEncryptCertPath(String encryptCertPath) {
		this.encryptCertPath = encryptCertPath;
	}
	
	public String getValidateCertDir() {
		return validateCertDir;
	}

	public void setValidateCertDir(String validateCertDir) {
		this.validateCertDir = validateCertDir;
	}

	public String getSingleQueryUrl() {
		return singleQueryUrl;
	}

	public void setSingleQueryUrl(String singleQueryUrl) {
		this.singleQueryUrl = singleQueryUrl;
	}

	public String getBatchQueryUrl() {
		return batchQueryUrl;
	}

	public void setBatchQueryUrl(String batchQueryUrl) {
		this.batchQueryUrl = batchQueryUrl;
	}

	public String getBatchTransUrl() {
		return batchTransUrl;
	}

	public void setBatchTransUrl(String batchTransUrl) {
		this.batchTransUrl = batchTransUrl;
	}

	public String getFileTransUrl() {
		return fileTransUrl;
	}

	public void setFileTransUrl(String fileTransUrl) {
		this.fileTransUrl = fileTransUrl;
	}

	public String getSignCertDir() {
		return signCertDir;
	}

	public void setSignCertDir(String signCertDir) {
		this.signCertDir = signCertDir;
	}

	public Properties getProperties() {
		return properties;
	}

	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	public String getCardRequestUrl() {
		return cardRequestUrl;
	}

	public void setCardRequestUrl(String cardRequestUrl) {
		this.cardRequestUrl = cardRequestUrl;
	}

	public String getAppRequestUrl() {
		return appRequestUrl;
	}

	public void setAppRequestUrl(String appRequestUrl) {
		this.appRequestUrl = appRequestUrl;
	}
	
	public String getEncryptTrackCertPath() {
		return encryptTrackCertPath;
	}

	public void setEncryptTrackCertPath(String encryptTrackCertPath) {
		this.encryptTrackCertPath = encryptTrackCertPath;
	}
	
	public String getJfFrontRequestUrl() {
		return jfFrontRequestUrl;
	}

	public void setJfFrontRequestUrl(String jfFrontRequestUrl) {
		this.jfFrontRequestUrl = jfFrontRequestUrl;
	}

	public String getJfBackRequestUrl() {
		return jfBackRequestUrl;
	}

	public void setJfBackRequestUrl(String jfBackRequestUrl) {
		this.jfBackRequestUrl = jfBackRequestUrl;
	}

	public String getJfSingleQueryUrl() {
		return jfSingleQueryUrl;
	}

	public void setJfSingleQueryUrl(String jfSingleQueryUrl) {
		this.jfSingleQueryUrl = jfSingleQueryUrl;
	}

	public String getJfCardRequestUrl() {
		return jfCardRequestUrl;
	}

	public void setJfCardRequestUrl(String jfCardRequestUrl) {
		this.jfCardRequestUrl = jfCardRequestUrl;
	}

	public String getJfAppRequestUrl() {
		return jfAppRequestUrl;
	}

	public void setJfAppRequestUrl(String jfAppRequestUrl) {
		this.jfAppRequestUrl = jfAppRequestUrl;
	}

	public String getSingleMode() {
		return singleMode;
	}

	public void setSingleMode(String singleMode) {
		this.singleMode = singleMode;
	}

	public String getEncryptTrackKeyExponent() {
		return encryptTrackKeyExponent;
	}

	public void setEncryptTrackKeyExponent(String encryptTrackKeyExponent) {
		this.encryptTrackKeyExponent = encryptTrackKeyExponent;
	}

	public String getEncryptTrackKeyModulus() {
		return encryptTrackKeyModulus;
	}

	public void setEncryptTrackKeyModulus(String encryptTrackKeyModulus) {
		this.encryptTrackKeyModulus = encryptTrackKeyModulus;
	}
	
	public String getSecureKey() {
		return secureKey;
	}

	public void setSecureKey(String securityKey) {
		this.secureKey = securityKey;
	}
	
	public String getMiddleCertPath() {
		return middleCertPath;
	}

	public void setMiddleCertPath(String middleCertPath) {
		this.middleCertPath = middleCertPath;
	}
	
	public boolean isIfValidateCNName() {
		return ifValidateCNName;
	}

	public void setIfValidateCNName(boolean ifValidateCNName) {
		this.ifValidateCNName = ifValidateCNName;
	}

	public boolean isIfValidateRemoteCert() {
		return ifValidateRemoteCert;
	}

	public void setIfValidateRemoteCert(boolean ifValidateRemoteCert) {
		this.ifValidateRemoteCert = ifValidateRemoteCert;
	}

	public String getQrcBackTransUrl() {
		return qrcBackTransUrl;
	}

	public void setQrcBackTransUrl(String qrcBackTransUrl) {
		this.qrcBackTransUrl = qrcBackTransUrl;
	}

	public String getQrcB2cIssBackTransUrl() {
		return qrcB2cIssBackTransUrl;
	}

	public void setQrcB2cIssBackTransUrl(String qrcB2cIssBackTransUrl) {
		this.qrcB2cIssBackTransUrl = qrcB2cIssBackTransUrl;
	}

	public String getQrcB2cMerBackTransUrl() {
		return qrcB2cMerBackTransUrl;
	}

	public void setQrcB2cMerBackTransUrl(String qrcB2cMerBackTransUrl) {
		this.qrcB2cMerBackTransUrl = qrcB2cMerBackTransUrl;
	}


	public String getFrontUrl() {
		return frontUrl;
	}

	public void setFrontUrl(String frontUrl) {
		this.frontUrl = frontUrl;
	}

	public String getBackUrl() {
		return backUrl;
	}

	public void setBackUrl(String backUrl) {
		this.backUrl = backUrl;
	}

	public String getRootCertPath() {
		return rootCertPath;
	}

	public void setRootCertPath(String rootCertPath) {
		this.rootCertPath = rootCertPath;
	}
	
}
