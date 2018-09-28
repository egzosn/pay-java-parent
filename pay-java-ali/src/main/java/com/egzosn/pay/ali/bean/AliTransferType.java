package com.egzosn.pay.ali.bean;

import com.egzosn.pay.common.bean.TransferType;

/**
 *  收款方账户类型
 * @author egan
 *         email egzosn@gmail.com
 *         date 2018/9/28.20:32
 */
public enum AliTransferType implements TransferType {
    /**
     * 支付宝账号对应的支付宝唯一用户号。以2088开头的16位纯数字组成。
     */
    ALIPAY_USERID,
    /**
     * 支付宝登录号，支持邮箱和手机号格式。
     */
    ALIPAY_LOGONID
    ;

    /**
     * 获取转账类型
     *
     * @return 转账类型
     */
    @Override
    public String getType() {
        return name();
    }

    /**
     * 获取接口
     *
     * @return 接口
     */
    @Override
    public String getMethod() {
        return name();
    }
}
