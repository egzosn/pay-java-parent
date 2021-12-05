package com.egzosn.pay.ali.bean;

import com.egzosn.pay.common.bean.RefundOrder;
import com.egzosn.pay.common.bean.SignType;

/**
 * 收单退款冲退完成通知
 *
 * @author Egan
 * <pre>
 * email egan@egzosn.com
 * date 2021/12/4
 * </pre>
 */
public class RefundDepositBackCompletedNotify extends RefundOrder {

    /**
     * 通知
     */
    private String notifyId;
    /**
     * 消息类型。目前支持类型：sys：系统消息；usr，用户消息；app，应用消息
     */
    private String msgType;

    /**
     * 消息归属的商户支付宝uid。用户消息和应用消息时非空
     */
    private String msgUid;

    /**
     * 消息归属方的应用id。应用消息时非空
     */
    private String msgAppId;
    /**
     * 加密算法
     */
    private SignType encryptType;


    /**
     * 银行卡冲退状态。S-成功，F-失败。银行卡冲退失败，资金自动转入用户支付宝余额。
     */
    private String dbackStatus;
    /**
     * 银行卡冲退金额
     */
    private String dbackAmount;
    /**
     * 银行响应时间，格式为yyyy-MM-dd HH:mm:ss
     */
    private String bankAckTime;
    /**
     * 预估银行入账时间，格式为yyyy-MM-dd HH:mm:ss
     */
    private String estBankReceiptTime;

    public String getNotifyId() {
        return notifyId;
    }

    public void setNotifyId(String notifyId) {
        this.notifyId = notifyId;
        addAttr("notify_id", notifyId);
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
        addAttr("msg_type", msgType);
    }

    public String getMsgUid() {
        return msgUid;
    }

    public void setMsgUid(String msgUid) {
        this.msgUid = msgUid;
        addAttr("msg_uid", msgUid);
    }

    public String getMsgAppId() {
        return msgAppId;
    }

    public void setMsgAppId(String msgAppId) {
        this.msgAppId = msgAppId;
        addAttr("msg_app_id", msgAppId);
    }

    public SignType getEncryptType() {
        return encryptType;
    }

    public void setEncryptType(SignType encryptType) {
        this.encryptType = encryptType;
        addAttr("encrypt_type", encryptType);
    }


    public String getDbackStatus() {
        return dbackStatus;
    }

    public void setDbackStatus(String dbackStatus) {
        this.dbackStatus = dbackStatus;
        addAttr("dback_status", dbackStatus);
    }

    public String getDbackAmount() {
        return dbackAmount;
    }

    public void setDbackAmount(String dbackAmount) {
        this.dbackAmount = dbackAmount;
        addAttr("dback_amount", dbackAmount);
    }

    public String getBankAckTime() {
        return bankAckTime;
    }

    public void setBankAckTime(String bankAckTime) {
        this.bankAckTime = bankAckTime;
        addAttr("bank_ack_time", bankAckTime);
    }

    public String getEstBankReceiptTime() {
        return estBankReceiptTime;
    }

    public void setEstBankReceiptTime(String estBankReceiptTime) {
        this.estBankReceiptTime = estBankReceiptTime;
        addAttr("est_bank_receipt_time", estBankReceiptTime);
    }
}
