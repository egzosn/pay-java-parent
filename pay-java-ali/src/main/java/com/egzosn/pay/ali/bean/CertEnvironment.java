/**
 * Alipay.com Inc. Copyright (c) 2004-2020 All Rights Reserved.
 */
package com.egzosn.pay.ali.bean;

import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.egzosn.pay.ali.utils.AntCertificationUtil;
import com.egzosn.pay.common.bean.result.PayException;
import com.egzosn.pay.common.exception.PayErrorException;
import com.egzosn.pay.common.util.str.StringUtils;

/**
 * 证书模式运行时环境
 *
 * @author zhongyu
 * @version $Id: CertEnvironment.java, v 0.1 2020年01月02日 5:21 PM zhongyu Exp $
 *
 * @author egan update 2020/10/12
 *
 */
public class CertEnvironment {
    /**
     * 支付宝根证书内容
     */
    private String rootCertContent;

    /**
     * 支付宝根证书序列号
     */
    private String rootCertSN;

    /**
     * 商户应用公钥证书序列号
     */
    private String merchantCertSN;
    /**
     * 默认的支付宝公钥证书序列号
     */
    private String aliPayPublicKeySN;

    /**
     * 缓存的不同支付宝公钥证书序列号对应的支付宝公钥
     */
    private static final Map<String, String> CACHED_ALI_PAY_PUBLIC_KEY = new ConcurrentHashMap<String, String>();

    /**
     * 构造证书运行环境
     *
     * @param merchantCert   商户公钥证书路径
     * @param aliPayCert     支付宝公钥证书路径
     * @param aliPayRootCert 支付宝根证书路径
     */
    public CertEnvironment(InputStream merchantCert, InputStream aliPayCert, InputStream aliPayRootCert) {
        if (null == merchantCert || null ==  aliPayCert || null == aliPayRootCert) {
            throw new PayErrorException(new PayException("", "证书参数merchantCert、aliPayCert或aliPayRootCert设置不完整。"));
        }

        this.rootCertContent = AntCertificationUtil.readFromInputStream(aliPayRootCert);
        this.rootCertSN = AntCertificationUtil.getRootCertSN(rootCertContent);
        this.merchantCertSN = AntCertificationUtil.getCertSN(AntCertificationUtil.readFromInputStream((merchantCert)));

        String aliPayPublicCertContent = AntCertificationUtil.readFromInputStream(aliPayCert);
        aliPayPublicKeySN = AntCertificationUtil.getCertSN(aliPayPublicCertContent);
        CACHED_ALI_PAY_PUBLIC_KEY.put(aliPayPublicKeySN,
                AntCertificationUtil.getCertPublicKey(aliPayPublicCertContent));
    }

    public String getRootCertSN() {
        return rootCertSN;
    }

    public String getMerchantCertSN() {
        return merchantCertSN;
    }

    public String getAliPayPublicKey(String sn) {
        //如果没有指定sn，则默认取缓存中的第一个值
        if (StringUtils.isEmpty(sn)) {
            return CACHED_ALI_PAY_PUBLIC_KEY.values().iterator().next();
        }

        if (CACHED_ALI_PAY_PUBLIC_KEY.containsKey(sn)) {
            return CACHED_ALI_PAY_PUBLIC_KEY.get(sn);
        } else {
            //网关在支付宝公钥证书变更前，一定会确认通知到商户并在商户做出反馈后，才会更新该商户的支付宝公钥证书
            //TODO: 后续可以考虑加入自动升级支付宝公钥证书逻辑，注意并发更新冲突问题
            throw new PayErrorException(new PayException("", "支付宝公钥证书[" + sn + "]已过期，请重新下载最新支付宝公钥证书并替换原证书文件"));
        }
    }
}