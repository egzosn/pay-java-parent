package com.egzosn.pay.common.util.sign;

import com.egzosn.pay.common.bean.result.PayException;
import com.egzosn.pay.common.exception.PayErrorException;
import com.egzosn.pay.common.util.sign.encrypt.sm3.SM3Digest;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.PublicKey;
import java.security.Signature;

public class SecureUtil {
    //日志
    protected static final Log log = LogFactory.getLog(SecureUtil.class);
    /**
     * 算法常量： SHA1
     */
    private static final String ALGORITHM_SHA1 = "SHA-1";
    /**
     * 算法常量： SHA256
     */
    private static final String ALGORITHM_SHA256 = "SHA-256";
    /**
     * 算法常量：SHA1withRSA
     */
    private static final String BC_PROV_ALGORITHM_SHA1RSA = "SHA1withRSA";
    /**
     * 算法常量：SHA256withRSA
     */
    private static final String BC_PROV_ALGORITHM_SHA256RSA = "SHA256withRSA";

    /**
     * 获取摘要
     *
     * @param data 待计算的数据
     * @param algorithm 算法名
     * @return 计算结果
     */
    private static byte[] digestByData (byte[] data,String algorithm) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance(algorithm);
            md.reset();
            md.update(data);
            return md.digest();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * sha1计算后进行16进制转换
     *
     * @param data     待计算的数据
     * @param encoding 编码
     * @return 计算结果
     */
    public static byte[] sha1X16 (String data, String encoding) {
        try {
            byte[] bytes = digestByData(data.getBytes(encoding),ALGORITHM_SHA1);
            StringBuilder sha1StrBuff = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                if (Integer.toHexString(0xFF & bytes[i]).length() == 1) {
                    sha1StrBuff.append("0").append(
                            Integer.toHexString(0xFF & bytes[i]));
                } else {
                    sha1StrBuff.append(Integer.toHexString(0xFF & bytes[i]));
                }
            }
            return sha1StrBuff.toString().getBytes(encoding);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
    }



    /**
     * sha256计算后进行16进制转换
     *
     * @param data
     *            待计算的数据
     * @param encoding
     *            编码
     * @return 计算结果
     */
    public static String sha256X16Str(String data, String encoding) {
        byte[] bytes =null;
        try {
            bytes = digestByData(data.getBytes(encoding),ALGORITHM_SHA1);
        } catch (UnsupportedEncodingException e) {
           throw new PayErrorException(new PayException("error", e.getLocalizedMessage()));
        }
        StringBuilder sha256StrBuff = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            if (Integer.toHexString(0xFF & bytes[i]).length() == 1) {
                sha256StrBuff.append("0").append(
                        Integer.toHexString(0xFF & bytes[i]));
            } else {
                sha256StrBuff.append(Integer.toHexString(0xFF & bytes[i]));
            }
        }
        return sha256StrBuff.toString();
    }

    /**
     * SM3计算.
     *
     * @param data
     *            待计算的数据
     * @return 计算结果
     */
    private static byte[] sm3(byte[] data) {

        SM3Digest sm3 = new SM3Digest();
        sm3.update(data, 0, data.length);
        byte[] result = new byte[sm3.getDigestSize()];
        sm3.doFinal(result, 0);
        return result;
    }

    /**
     * sm3计算后进行16进制转换
     *
     * @param data
     *            待计算的数据
     * @param encoding
     *            编码
     * @return 计算结果
     */
    public static String sm3X16Str(String data, String encoding) {
        byte[] bytes = new byte[new SM3Digest().getDigestSize()];
        try {
            bytes = SecureUtil.sm3(data.getBytes(encoding));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        StringBuilder sm3StrBuff = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            if (Integer.toHexString(0xFF & bytes[i]).length() == 1) {
                sm3StrBuff.append("0").append(
                        Integer.toHexString(0xFF & bytes[i]));
            } else {
                sm3StrBuff.append(Integer.toHexString(0xFF & bytes[i]));
            }
        }
        return sm3StrBuff.toString();
    }

    public static boolean validateSignBySoft256(PublicKey publicKey, byte[] signData, byte[] srcData) throws Exception {

        Signature st = Signature.getInstance(BC_PROV_ALGORITHM_SHA256RSA, "BC");
        st.initVerify(publicKey);
        st.update(srcData);
        return st.verify(signData);
    }

    public static boolean validateSignBySoft(PublicKey publicKey,
                                             byte[] signData, byte[] srcData) throws Exception {
        Signature st = Signature.getInstance(BC_PROV_ALGORITHM_SHA1RSA, "BC");
        st.initVerify(publicKey);
        st.update(srcData);
        return st.verify(signData);
    }
}
