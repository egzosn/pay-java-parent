package com.egzosn.pay.union.SDK;/**
 * Description：
 * author: Fuzx
 * date: 2017/11/27 0027
 */

import com.egzosn.pay.common.util.sign.SecureUtil;
import com.egzosn.pay.common.util.sign.SignUtils;
import com.egzosn.pay.common.util.sign.encrypt.Base64;
import com.egzosn.pay.common.util.sign.encrypt.RSA;
import com.egzosn.pay.common.util.sign.encrypt.SHA256;
import com.egzosn.pay.common.util.str.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import static com.egzosn.pay.union.SDK.SDKConstants.*;

/**
 * @author Actinia
 * @email hayesfu@qq.com
 * @create 2017 2017/11/27 0027
 */
public class SDKUtils {
    //日志
    protected static final Log log = LogFactory.getLog(SDKUtils.class);

    /**
     * 验证签名
     *
     * @param resData
     *            返回报文数据
     * @param encoding
     *            编码格式
     * @return
     */
    public static boolean validate(Map<String, Object> resData, String encoding) {
        log.info("验签处理开始");
        if (StringUtils.isEmpty(encoding)) {
            encoding = "UTF-8";
        }
        String signMethod = resData.get(SDKConstants.param_signMethod).toString();
        String version = resData.get(SDKConstants.param_version).toString();
        if (SIGNMETHOD_RSA.equals(signMethod) || VERSION_1_0_0.equals(version) || VERSION_5_0_1.equals(version)) {
            // 获取返回报文的版本号
            if (VERSION_5_0_0.equals(version) || VERSION_1_0_0.equals(version) || VERSION_5_0_1.equals(version)) {
                String stringSign = resData.get(SDKConstants.param_signature).toString();
                log.info("签名原文：["+stringSign+"]");
                // 从返回报文中获取certId ，然后去证书静态Map中查询对应验签证书对象
                String certId = resData.get(SDKConstants.param_certId).toString();
                log.info("对返回报文串验签使用的验签公钥序列号：["+certId+"]");
                // 将Map信息转换成key1=value1&key2=value2的形式
                String stringData = SignUtils.parameterText(resData);
                log.info("待验签返回报文串：["+stringData+"]");
                try {
                    // 验证签名需要用银联发给商户的公钥证书.
                    return SecureUtil.validateSignBySoft(CertUtil.getValidatePublicKey(certId), Base64.decode(stringSign),
                            SecureUtil.sha1X16(stringData, encoding));
                } catch (UnsupportedEncodingException e) {
                    log.error(e.getMessage(), e);
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            } else if (VERSION_5_1_0.equals(version)) {
                // 1.从返回报文中获取公钥信息转换成公钥对象
                String strCert = resData.get(SDKConstants.param_signPubKeyCert).toString();
				log.info("验签公钥证书：["+strCert+"]");
                X509Certificate x509Cert = CertUtil.genCertificateByStr(strCert);
                if(x509Cert == null) {
                    log.error("convert signPubKeyCert failed");
                    return false;
                }
                // 2.验证证书链
                if (!CertUtil.verifyCertificate(x509Cert)) {
                    log.error("验证公钥证书失败，证书信息：["+strCert+"]");
                    return false;
                }

                // 3.验签
                String stringSign = resData.get(SDKConstants.param_signature).toString();
                log.info("签名原文：["+stringSign+"]");
                // 将Map信息转换成key1=value1&key2=value2的形式
                String stringData = SignUtils.parameterText(resData);
                log.info("待验签返回报文串：["+stringData+"]");
                try {
                    // 验证签名需要用银联发给商户的公钥证书.
                    boolean result = SecureUtil.validateSignBySoft256(x509Cert
                            .getPublicKey(), Base64.decode(stringSign
                            ), SHA256.sha256X16(
                            stringData, encoding));
                    log.info("验证签名" + (result? "成功":"失败"));
                    return result;
                } catch (UnsupportedEncodingException e) {
                    log.error(e.getMessage(), e);
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }

        } else if (SIGNMETHOD_SHA256.equals(signMethod)) {
            // 1.进行SHA256验证
            String stringSign = resData.get(SDKConstants.param_signature).toString();
            log.info("签名原文：["+stringSign+"]");
            // 将Map信息转换成key1=value1&key2=value2的形式
            String stringData = SignUtils.parameterText(resData);
            log.info("待验签返回报文串：["+stringData+"]");
            String strBeforeSha256 = stringData
                    + SDKConstants.AMPERSAND
                    + SecureUtil.sha256X16Str(SDKConfig.getConfig()
                    .getSecureKey(), encoding);
            String strAfterSha256 = SecureUtil.sha256X16Str(strBeforeSha256,
                    encoding);
            boolean result =  stringSign.equals(strAfterSha256);
            log.info("验证签名" + (result? "成功":"失败"));
            return result;
        } else if (SIGNMETHOD_SM3.equals(signMethod)) {
            // 1.进行SM3验证
            String stringSign = resData.get(SDKConstants.param_signature).toString();
            log.info("签名原文：["+stringSign+"]");
            // 将Map信息转换成key1=value1&key2=value2的形式
            String stringData = SignUtils.parameterText(resData);
            log.info("待验签返回报文串：["+stringData+"]");
            String strBeforeSM3 = stringData
                    + SDKConstants.AMPERSAND
                    + SecureUtil.sm3X16Str(SDKConfig.getConfig()
                    .getSecureKey(), encoding);
            String strAfterSM3 = SecureUtil
                    .sm3X16Str(strBeforeSM3, encoding);
            boolean result =  stringSign.equals(strAfterSM3);
            log.info("验证签名" + (result? "成功":"失败"));
            return result;
        }
        return false;
    }

    /**
     * 对参数加密
     * @param data
     * @param encoding
     * @return
     */
    public static boolean signParams(Map<String, String> data,String encoding){
        if (StringUtils.isBlank(encoding)) {
            encoding = "UTF-8";
        }
        String signMethod = data.get(SDKConstants.param_signMethod);
        String version = data.get(SDKConstants.param_version);
        if(StringUtils.isBlank(signMethod)){
            log.info("银联签名方式不能为空");
            return false;
        }
        if(StringUtils.isBlank(version)){
            log.info("版本号不能为空");
            return false;
        }
        if (SIGNMETHOD_RSA.equals(signMethod)|| VERSION_1_0_0.equals(version) || SDKConstants.VERSION_5_0_1.equals(version)) {
            if (SDKConstants.VERSION_5_0_0.equals(version) || VERSION_1_0_0.equals(version) || SDKConstants.VERSION_5_0_1.equals(version)) {
                data.put(SDKConstants.param_certId, CertUtil.getSignCertId());
                String stringData = SignUtils.parameterText(data);
                String stringSign = null;
                try {
                    // 通过SHA1进行摘要并转16进制
                    byte[] signDigest = SecureUtil
                            .sha1X16(stringData, encoding);
                    stringSign = RSA.sign(signDigest, CertUtil.getSignCertPrivateKey());
                    // 设置签名域值
                    data.put(SDKConstants.param_signature, stringSign);
                    return true;
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
            }
        }else if (SDKConstants.SIGNMETHOD_SHA256.equals(signMethod)) {
            String stringData = SignUtils.parameterText(data);
            String strBeforeSha256 = stringData
                    + SDKConstants.AMPERSAND
                    + SecureUtil.sha256X16Str(SDKConfig.getConfig().getSecureKey(), encoding);
            String strAfterSha256 = SecureUtil.sha256X16Str(strBeforeSha256,
                    encoding);
            data.put(SDKConstants.param_signature, strAfterSha256);
        } else if (SDKConstants.SIGNMETHOD_SM3.equals(signMethod)) {
            String stringData = SignUtils.parameterText(data);
            String strBeforeSM3 = stringData
                    + SDKConstants.AMPERSAND
                    + SecureUtil.sm3X16Str(SDKConfig.getConfig().getSecureKey(), encoding);
            String strAfterSM3 = SecureUtil.sm3X16Str(strBeforeSM3, encoding);
            // 设置签名域值
            data.put(SDKConstants.param_signature, strAfterSM3);
        }

        return false;
    }


}
