package com.egzosn.pay.common.util.sign;


import java.security.Security;
import java.util.Map;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import static com.egzosn.pay.common.util.sign.SignTextUtils.parameterText;

import com.egzosn.pay.common.bean.SignType;
import com.egzosn.pay.common.util.sign.encrypt.HmacSha256;

/**
 * 签名 工具
 *
 * @author egan
 * <pre>
 * email egzosn@gmail.com
 * date 2016/11/9 17:45
 * </pre>
 */
public enum SignUtils implements SignType {

    MD5 {
        /**
         *
         * @param content           需要签名的内容
         * @param key               密钥
         * @param characterEncoding 字符编码
         * @return 签名值
         */
        @Override
        public String createSign(String content, String key, String characterEncoding) {

            return com.egzosn.pay.common.util.sign.encrypt.MD5.sign(content, key, characterEncoding);
        }

        /**
         * 签名字符串
         * @param text 需要签名的字符串
         * @param sign 签名结果
         * @param key 密钥
         * @param characterEncoding 编码格式
         * @return 签名结果
         */
        @Override
        public boolean verify(String text, String sign, String key, String characterEncoding) {
            return com.egzosn.pay.common.util.sign.encrypt.MD5.verify(text, sign, key, characterEncoding);
        }
    }, HMACSHA256 {
        @Override
        public String getName() {
            return "HMAC-SHA256";
        }

        /**
         * 签名
         *
         * @param content           需要签名的内容
         * @param key               密钥
         * @param characterEncoding 字符编码
         *
         * @return 签名值
         */
        @Override
        public String createSign(String content, String key, String characterEncoding) {
            return HmacSha256.createSign(content, key, characterEncoding);
        }

        /**
         * 签名字符串
         *
         * @param text              需要签名的字符串
         * @param sign              签名结果
         * @param key               密钥
         * @param characterEncoding 编码格式
         *
         * @return 签名结果
         */
        @Override
        public boolean verify(String text, String sign, String key, String characterEncoding) {
            return createSign(text, key, characterEncoding).equals(sign);
        }
    },

    RSA {
        @Override
        public String createSign(String content, String key, String characterEncoding) {
            return com.egzosn.pay.common.util.sign.encrypt.RSA.sign(content, key, characterEncoding);
        }

        @Override
        public boolean verify(String text, String sign, String publicKey, String characterEncoding) {
            return com.egzosn.pay.common.util.sign.encrypt.RSA.verify(text, sign, publicKey, characterEncoding);
        }
    },

    RSA2 {
        @Override
        public String createSign(String content, String key, String characterEncoding) {
            return com.egzosn.pay.common.util.sign.encrypt.RSA2.sign(content, key, characterEncoding);
        }

        @Override
        public boolean verify(String text, String sign, String publicKey, String characterEncoding) {
            return com.egzosn.pay.common.util.sign.encrypt.RSA2.verify(text, sign, publicKey, characterEncoding);
        }
    },
    SHA1 {
        @Override
        public String createSign(String content, String key, String characterEncoding) {
            return com.egzosn.pay.common.util.sign.encrypt.SHA1.sign(content, key, characterEncoding);
        }

        @Override
        public boolean verify(String text, String sign, String publicKey, String characterEncoding) {
            return com.egzosn.pay.common.util.sign.encrypt.SHA1.verify(text, sign, publicKey, characterEncoding);
        }
    },
    SHA256 {
        @Override
        public String createSign(String content, String key, String characterEncoding) {
            return com.egzosn.pay.common.util.sign.encrypt.SHA256.sign(content, key, characterEncoding);
        }

        @Override
        public boolean verify(String text, String sign, String publicKey, String characterEncoding) {
            return com.egzosn.pay.common.util.sign.encrypt.SHA256.verify(text, sign, publicKey, characterEncoding);
        }
    },
    SM3 {
        @Override
        public String createSign(String content, String key, String characterEncoding) {
            return com.egzosn.pay.common.util.sign.encrypt.RSA2.sign(content, key, characterEncoding);
        }

        @Override
        public boolean verify(String text, String sign, String publicKey, String characterEncoding) {
            return com.egzosn.pay.common.util.sign.encrypt.RSA2.verify(text, sign, publicKey, characterEncoding);
        }
    };



    @Override
    public String getName() {
        return this.name();
    }

    /**
     * 签名
     *
     * @param parameters        需要进行排序签名的参数
     * @param key               密钥
     * @param characterEncoding 编码格式
     * @return 签名值
     */
    @Override
    public String sign(Map parameters, String key, String characterEncoding) {

        return createSign(parameterText(parameters, "&"), key, characterEncoding);
    }

    /**
     * 签名
     *
     * @param parameters        需要进行排序签名的参数
     * @param key               密钥
     * @param separator         分隔符  默认 &amp;
     * @param characterEncoding 编码格式
     * @return 签名值
     */
    @Override
    public String sign(Map parameters, String key, String separator, String characterEncoding) {

        return createSign(parameterText(parameters, separator), key, characterEncoding);

    }


    /**
     * 签名字符串
     *
     * @param params            需要签名的字符串
     * @param sign              签名结果
     * @param key               密钥
     * @param characterEncoding 编码格式
     * @return 签名结果
     */
    @Override
    public boolean verify(Map params, String sign, String key, String characterEncoding) {
        //判断是否一样
        return this.verify(parameterText(params), sign, key, characterEncoding);
    }

    /**
     * 初始化BC
     */
    public static void initBc() {
        if (null == Security.getProvider("BC")) {
            Security.removeProvider("SunEC");
            Security.addProvider(new BouncyCastleProvider());
        }
    }


}
