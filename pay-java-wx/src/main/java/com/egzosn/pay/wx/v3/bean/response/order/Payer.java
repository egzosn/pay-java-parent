package com.egzosn.pay.wx.v3.bean.response.order;

/**
 * 支付者信息
 * @author Egan
 * <pre>
 * email egan@egzosn.com
 * date 2021/10/4
 * </pre>
 */
public class Payer {
    /**
     * 用户在直连商户appid下的唯一标识。
     * 使用合单appid获取的对应用户openid。是用户在商户appid下的唯一标识。
     */
    private String openid;

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }
}