
package com.egzosn.pay.common.util.sign.encrypt;

import javax.crypto.Cipher;
import java.io.*;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * RSA
 * @author egan
 * <pre>
 * email egzosn@gmail.com
 *</pre>
 */
public class RSA{

	private static final String ALGORITHM = "RSA";


	private static final String SIGN_ALGORITHMS = "SHA1WithRSA";


	/**
	 * RSA签名
	 * @param content 待签名数据
	 * @param privateKey 私钥
	 * @param signAlgorithms 签名算法
	 * @param characterEncoding 编码格式
	 * @return 签名值
	 */
	public static String sign(String content, String privateKey, String signAlgorithms, String characterEncoding) {
		try {
			PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec( Base64.decode(privateKey));
			KeyFactory keyf 			= KeyFactory.getInstance(ALGORITHM);
			PrivateKey priKey 			= keyf.generatePrivate(priPKCS8);

			java.security.Signature signature = java.security.Signature.getInstance(signAlgorithms);

			signature.initSign(priKey);
			signature.update(content.getBytes(characterEncoding));

			byte[] signed = signature.sign();

			return Base64.encode(signed);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}



	/**
	 * RSA签名
	 * @param content 待签名数据
	 * @param privateKey 私钥
	 * @param signAlgorithms 签名算法
	 * @param characterEncoding 编码格式
	 * @return 签名值
	 */
	public static String sign(String content, PrivateKey privateKey, String signAlgorithms, String characterEncoding) {
		try {
			java.security.Signature signature = java.security.Signature.getInstance(signAlgorithms);
			signature.initSign(privateKey);
			signature.update(content.getBytes(characterEncoding));
			byte[] signed = signature.sign();
			return Base64.encode(signed);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}


	/**
	* RSA签名
	* @param content 待签名数据
	* @param privateKey 私钥
	* @param characterEncoding 编码格式
	* @return 签名值
	*/
	public static String sign(String content, String privateKey ,String characterEncoding){
        return sign(content, privateKey, SIGN_ALGORITHMS, characterEncoding);
    }

	/**
	* RSA签名
	* @param content 待签名数据
	* @param privateKey 私钥
	* @param characterEncoding 编码格式
	* @return 签名值
	*/
	public static String sign(String content, PrivateKey privateKey ,String characterEncoding){
        return sign(content, privateKey, SIGN_ALGORITHMS, characterEncoding);
    }

	/**
	* RSA验签名检查
	* @param content 待签名数据
	* @param sign 签名值
	* @param  publicKey 公钥
	* @param signAlgorithms 签名算法
	* @param characterEncoding 编码格式
	* @return 布尔值
	*/
	public static boolean verify(String content, String sign, String publicKey, String signAlgorithms, String characterEncoding){
		try {
	        PublicKey pubKey 		= getPublicKey(publicKey, ALGORITHM);
			java.security.Signature signature = java.security.Signature.getInstance(signAlgorithms);
			signature.initVerify(pubKey);
			signature.update( content.getBytes(characterEncoding) );
			return signature.verify( Base64.decode(sign) );
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	* RSA验签名检查
	* @param content 待签名数据
	* @param sign 签名值
	* @param  publicKey 公钥
	* @param signAlgorithms 签名算法
	* @param characterEncoding 编码格式
	* @return 布尔值
	*/
	public static boolean verify(String content, String sign, PublicKey publicKey, String signAlgorithms, String characterEncoding){
		try {
			java.security.Signature signature = java.security.Signature.getInstance(signAlgorithms);
			signature.initVerify(publicKey);
			signature.update( content.getBytes(characterEncoding) );
			return signature.verify( Base64.decode(sign) );
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
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

		return verify(content, sign, publicKey, SIGN_ALGORITHMS, characterEncoding);
	}


	/**
	 * RSA验签名检查
	 * @param content 待签名数据
	 * @param sign 签名值
	 * @param  publicKey 公钥
	 * @param characterEncoding 编码格式
	 * @return 布尔值
	 */
	public static boolean verify(String content, String sign, PublicKey publicKey, String characterEncoding){
		return verify(content, sign, publicKey, SIGN_ALGORITHMS, characterEncoding);
	}

	/**
	* 解密
	* @param content 密文
	* @param privateKey 商户私钥
	* @param characterEncoding 编码格式
	* @return 解密后的字符串
	 * @throws Exception 解密异常
	*/
	public static String decrypt(String content, String privateKey, String characterEncoding) throws Exception {
        PrivateKey prikey 	= getPrivateKey(privateKey);
        Cipher cipher 		= Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, prikey);
       try(InputStream ins 	= new ByteArrayInputStream(Base64.decode(content));   ByteArrayOutputStream writer = new ByteArrayOutputStream();) {

		   //rsa解密的字节大小最多是128，将需要解密的内容，按128位拆开解密
		   byte[] buf = new byte[128];
		   int bufl;
		   while ((bufl = ins.read(buf)) != -1) {
			   byte[] block = null;

			   if (buf.length == bufl) {
				   block = buf;
			   } else {
				   block = new byte[bufl];

				   for (int i = 0; i < bufl; i++) {
					   block[i] = buf[i];
				   }
			   }
			   writer.write(cipher.doFinal(block));
		   }

		   return new String(writer.toByteArray(), characterEncoding);
	   }
    }

	
	/**
	* 得到私钥
	* @param key 密钥字符串（经过base64编码）
	 * @throws Exception 加密异常
	 * @return 私钥
	*/
	public static PrivateKey getPrivateKey(String key) throws Exception {

		byte[] keyBytes;
		keyBytes = Base64.decode(key);
		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
		PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
		return privateKey;
	}

	/**
	* 得到公钥
	* @param key 密钥字符串（经过base64编码）
	* @param signAlgorithms 密钥类型
	 * @throws Exception 加密异常
	 * @return 公钥
	*/
	public static PublicKey getPublicKey(String key, String signAlgorithms) throws Exception {
		return getPublicKey(new ByteArrayInputStream(key.getBytes("ISO8859-1")), signAlgorithms);
	}


	/**
	* 得到公钥
	* @param key 密钥字符串（经过base64编码）
	 * @throws Exception 加密异常
	 * @return 公钥
	*/
	public static PublicKey getPublicKey(String key) throws Exception {

		return getPublicKey(key, ALGORITHM);
	}

	public static PublicKey getPublicKey(InputStream inputStream, String keyAlgorithm) throws Exception {
		try (BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));) {
			StringBuilder sb = new StringBuilder();
			String readLine = null;
			while ((readLine = br.readLine()) != null) {
				if (readLine.charAt(0) == '-') {
					continue;
				}
				sb.append(readLine);
				sb.append('\r');
			}
			X509EncodedKeySpec pubX509 = new X509EncodedKeySpec(Base64.decode(sb.toString()));
			KeyFactory keyFactory = KeyFactory.getInstance(keyAlgorithm);
			PublicKey publicKey = keyFactory.generatePublic(pubX509);
			return publicKey;
		}
	}

	public static byte[] encrypt(byte[] plainBytes, PublicKey publicKey, int keyLength, int reserveSize, String cipherAlgorithm) throws Exception {
		int keyByteSize = keyLength / 8;
		int encryptBlockSize = keyByteSize - reserveSize;
		int nBlock = plainBytes.length / encryptBlockSize;
		if ((plainBytes.length % encryptBlockSize) != 0) {
			nBlock += 1;
		}
		try (ByteArrayOutputStream outbuf = new ByteArrayOutputStream(nBlock * keyByteSize);) {
			Cipher cipher = Cipher.getInstance(cipherAlgorithm);
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			for (int offset = 0; offset < plainBytes.length; offset += encryptBlockSize) {
				int inputLen = plainBytes.length - offset;
				if (inputLen > encryptBlockSize) {
					inputLen = encryptBlockSize;
				}
				byte[] encryptedBlock = cipher.doFinal(plainBytes, offset, inputLen);
				outbuf.write(encryptedBlock);
			}
			outbuf.flush();
			return outbuf.toByteArray();
		}
	}
	public static String encrypt(String content, String publicKey, String cipherAlgorithm, String characterEncoding ) throws Exception {
		return Base64.encode(RSA.encrypt(content.getBytes(characterEncoding), RSA.getPublicKey(publicKey),1024, 11, cipherAlgorithm));
	}

}
