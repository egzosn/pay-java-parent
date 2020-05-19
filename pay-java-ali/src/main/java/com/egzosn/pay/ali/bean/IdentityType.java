package com.egzosn.pay.ali.bean;

/**
 * 参与方的标识类型
 * @author egan
 * date 2020/5/19 21:45
 * email egzosn@gmail.com
 */
public enum IdentityType {
    /**
     * 支付宝的会员ID
     */
    ALIPAY_USER_ID,
    /**
     * 支付宝登录号，支持邮箱和手机号格式
     */
    ALIPAY_LOGON_ID

}
