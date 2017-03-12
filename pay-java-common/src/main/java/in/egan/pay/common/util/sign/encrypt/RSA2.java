
package in.egan.pay.common.util.sign.encrypt;

import java.security.PrivateKey;

public class RSA2 {

	private static final String SIGN_SHA256RSA_ALGORITHMS = "SHA256WithRSA";



	public static String sign(String content, String privateKey, String characterEncoding) {

		return RSA.sign(content, privateKey, SIGN_SHA256RSA_ALGORITHMS, characterEncoding);
	}




	/**
	* RSA验签名检查
	* @param content 待签名数据
	* @param sign 签名值
	* @param  publicKey 公钥
	* @param characterEncoding 编码格式
	* @return 布尔值
	*/
	public static boolean verify(String content, String sign, String publicKey, String characterEncoding){

		return RSA.verify(content, sign, publicKey, SIGN_SHA256RSA_ALGORITHMS, characterEncoding );
	}
	
	/**
	* 解密
	* @param content 密文
	* @param privateKey 商户私钥
	* @param characterEncoding 编码格式
	* @return 解密后的字符串
	*/
	public static String decrypt(String content, String privateKey, String characterEncoding) throws Exception {
        return RSA.decrypt(content, privateKey, characterEncoding);
    }

	
	/**
	* 得到私钥
	* @param key 密钥字符串（经过base64编码）
	* @throws Exception
	*/
	public static PrivateKey getPrivateKey(String key) throws Exception {
		return RSA.getPrivateKey(key);
	}
}
