package in.egan.pay.common.bean;

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
 * @email egzosn@gmail.com
 * @date 2017/3/7 16:37
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
    //////////////////支付宝
/*    public Date getNotifyTime(){
        return parseDate(payMessage.get("notify_time"));
    }

    public String getNotifyType(){
        return payMessage.get("notify_type");
    }
    public String getNotifyId(){
        return payMessage.get("notify_id");
    }

    public String getSignType(){
        return payMessage.get("sign_type");
    }





    public String getPaymentType(){
        return payMessage.get("payment_type");
    }

    public String getTradeNo(){
        return payMessage.get("trade_no");
    }

    public String getTradeStatus(){
        return payMessage.get("trade_status");
    }
    public String getSellerId(){
        return payMessage.get("seller_id");
    }
    public String getSellerEmail(){
        return payMessage.get("seller_email");
    }
    public String getBuyerId(){
        return payMessage.get("buyer_id");
    }

    public String getBuyerEmail(){
        return payMessage.get("buyer_email");
    }


    public Number getQuantity(){
        String quantity = payMessage.get("quantity");
        if (null == quantity || "".equals(quantity)){    return 1;      }
        if (isNumber(quantity)){
            return  Integer.parseInt(quantity);
        }
        return 1;
    }

    public Number getPrice(){
        String price = payMessage.get("price");
        if (null == price || "".equals(price)){    return 1;      }
        if (isNumber(price)){
            return  new BigDecimal(price);
        }
        return 1;
    }

    public String getBody(){
        return payMessage.get("body");
    }

    public Date getGmtCreate(){

        return parseDate(payMessage.get("gmt_create"));
    }

    public Date getGmtPayment(){

        return parseDate(payMessage.get("gmt_payment"));
    }
    public String getIsTotalFeeAdjust (){
        return payMessage.get("is_total_fee_adjust");

    }


    public String getUseCoupon(){
        return payMessage.get("use_coupon");

    }

    public String getRefundStatus(){
        return payMessage.get("refund_status");

    }
    public Date getGmtRefund(){
        return parseDate(payMessage.get("gmt_refund"));

    }*/
    /////////////////支付宝



    //////////////////微信
   /* public String getIsSubscribe(){
        return payMessage.get("is_subscribe");
    }
    public String getAppid(){
        return payMessage.get("appid");
    }

    public String getFeeType(){
        return payMessage.get("fee_type");
    }
    public String getNonceStr(){
        return payMessage.get("nonce_str");
    }
    public String getTransactionId(){
        return payMessage.get("transaction_id");
    }

    public String getTradeType(){
        return payMessage.get("trade_type");
    }

    public String getResultCode(){
        return payMessage.get("result_code");
    }

    public String getMchId(){
        return payMessage.get("mch_id");
    }

    public String getAttach(){
        return payMessage.get("attach");
    }

    public String getTimeEnd(){
        return payMessage.get("time_end");
    }
    public String getBankType(){
        return payMessage.get("bank_type");
    }
    public String getOpenid(){
        return payMessage.get("openid");
    }
    public String getReturnCode(){
        return payMessage.get("return_code");
    }
    public Number getCashFee(){
        String cashFee = payMessage.get("cash_fee");
        if (null == cashFee || "".equals(cashFee)){    return 0;      }
        if (isNumber(cashFee)){
            return  new BigDecimal(cashFee).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP);
        }
        return 0;
    }*/
    //////////////////微信



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
