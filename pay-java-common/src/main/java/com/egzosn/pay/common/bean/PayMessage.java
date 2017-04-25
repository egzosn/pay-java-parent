package com.egzosn.pay.common.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

/**
 * 支付回调消息
 * 基础实现，具体可根据具体支付回调的消息去实现
 * @author egan
 * <pre>
 *     email egzosn@gmail.com
 *     date 2017/3/7 16:37
 *  </pre>
 */
public class PayMessage implements Serializable {
    private Map<String, String> payMessage = null;
    private String msgType;
    private String payType;
    private String transactionType;
    private String fromPay;
    private String describe;

    public PayMessage(Map<String, String> payMessage) {
        this.payMessage = payMessage;
    }

    public PayMessage(Map<String, String> payMessage, String payType, String msgType) {
        this(payMessage);
        this.payType = payType;
        this.msgType = msgType;
    }


    public PayMessage(Map<String, String> payMessage, String msgType, String payType, String transactionType) {
        this.payMessage = payMessage;
        this.msgType = msgType;
        this.payType = payType;
        this.transactionType = transactionType;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }


    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
            this.transactionType = transactionType;
    }

    public String getFromPay() {
        return fromPay;
    }

    public void setFromPay(String fromPay) {
        this.fromPay = fromPay;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }
    public String getDiscount(){
        return payMessage.get("discount");
    }
    public String getSubject(){
        return payMessage.get("subject");
    }



    /////////微信与支付宝共用
    public String getOutTradeNo(){
        return payMessage.get("out_trade_no");
    }

    public String getSign(){
        return payMessage.get("sign");
    }

    public Number getTotalFee(){
        String total_fee = payMessage.get("total_fee");
        if (null == total_fee || "".equals(total_fee)){    return 0;      }
        if (isNumber(total_fee)){
            BigDecimal totalFee = new BigDecimal(total_fee);
            return totalFee;
        }
        return 0;
    }

    /////////微信与支付宝共用



    public boolean isNumber(String str){
        return str.matches("^(-?[1-9]\\d*\\.?\\d*)|(-?0\\.\\d*[1-9])|(-?[0])|(-?[0]\\.\\d*)$");
    }

    public Date parseDate(String str){

        if (null == str || "".equals(str)){
            return null;
        }
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return format.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String toString() {
        return payMessage.toString();
    }

    public Map<String, String> getPayMessage() {
        return payMessage;
    }



}
