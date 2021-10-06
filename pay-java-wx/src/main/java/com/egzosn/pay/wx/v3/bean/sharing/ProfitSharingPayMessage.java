package com.egzosn.pay.wx.v3.bean.sharing;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.egzosn.pay.common.bean.PayMessage;

/**
 * 分账动账通知
 * @author Egan
 * <pre>
 * email egan@egzosn.com
 * date 2021/10/4
 * </pre>
 */
public class ProfitSharingPayMessage extends PayMessage {

    /**
     * 直连商户号.
     * <p>
     * 直连模式分账发起和出资商户
     */
    private String mchid;

    /**
     * 微信支付订单号
     */
    @JSONField(name = "transaction_id")
    private String transactionId;

    /**
     * 微信分账/回退单号.
     */
    @JSONField(name = "order_id")
    private String orderId;

    /**
     * 商户分账/回退单号.
     * 分账方系统内部的分账/回退单号
     */
    @JSONField(name = "out_order_no")
    private String outOrderNo;

    /**
     * 分账接收方.
     * <p>
     * 分账接收方对象
     */
    private List<Receiver> receivers;

    /**
     * 成功时间.
     * <p>
     * Rfc3339标准
     */
    @JSONField(name = "success_time", format = "yyyy-MM-dd'T'HH:mm:ssXXX")
    private Date successTime;


    public String getMchid() {
        return mchid;
    }

    public void setMchid(String mchid) {
        this.mchid = mchid;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOutOrderNo() {
        return outOrderNo;
    }

    public void setOutOrderNo(String outOrderNo) {
        this.outOrderNo = outOrderNo;
    }

    public List<Receiver> getReceivers() {
        return receivers;
    }

    public void setReceivers(List<Receiver> receivers) {
        this.receivers = receivers;
    }

    public Date getSuccessTime() {
        return successTime;
    }

    public void setSuccessTime(Date successTime) {
        this.successTime = successTime;
    }

    public static ProfitSharingPayMessage create(Map<String, Object> message) {
        ProfitSharingPayMessage payMessage = new JSONObject(message).toJavaObject(ProfitSharingPayMessage.class);
//        payMessage.setPayType("");
        payMessage.setPayMessage(message);
        return payMessage;
    }


}
