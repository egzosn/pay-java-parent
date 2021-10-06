package com.egzosn.pay.wx.v3.bean.sharing;


/**
 * 分账接收方类型
 *
 * @author Egan
 * <pre>
 * email egan@egzosn.com
 * date 2021/10/6
 * </pre>
 */
public enum ReceiverType {
    /**
     * 商户号
     */
    MERCHANT_ID,
    /**
     * 个人openid（由父商户APPID转换得到）
     */
    PERSONAL_OPENID,
    /**
     * 个人sub_openid（由子商户APPID转换得到），服务商模式
     */
    PERSONAL_SUB_OPENID
}