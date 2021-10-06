package com.egzosn.pay.wx.v3.bean.combine;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 子单信息，最多50单.
 * @author Egan
 * <pre>
 * email egan@egzosn.com
 * date 2021/10/5
 * </pre>
 */
public class CombineSubOrder {

    /**
     * 子单发起方商户号，必填，必须与发起方appid有绑定关系。
     */
    private String mchid;

    /**
     * 商户系统内部订单号，要求32个字符内，只能是数字、大小写字母_-|*@ ，且在同一个商户号下唯一。
     * 示例值：20150806125346
     */
    @JSONField(name = "out_trade_no")
    private String outTradeNo;

    public String getMchid() {
        return mchid;
    }

    public void setMchid(String mchid) {
        this.mchid = mchid;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }
}
