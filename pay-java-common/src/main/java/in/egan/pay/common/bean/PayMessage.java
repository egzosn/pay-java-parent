package in.egan.pay.common.bean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * 支付回调消息
 * @author egan
 * @email egzosn@gmail.com
 * @date 2016-6-1 14:2:3
 */
public class PayMessage implements Serializable {
    private Map<String, String> payMessage = null;
    private String msgType;
    private Short event;
    private String eventKey;
    private String fromPay;

    public PayMessage(Map<String, String> payMessage) {
        this.payMessage = payMessage;
    }

    public PayMessage(Map<String, String> payMessage, Short event, String msgType) {
        this(payMessage);
        this.event = event;
        this.msgType = msgType;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public Short getEvent() {
        return event;
    }

    public void setEvent(Short event) {
        this.event = event;
    }

    public String getEventKey() {
        return eventKey;
    }

    public void setEventKey(String eventKey) {
        this.eventKey = eventKey;
    }

    public String getFromPay() {
        return fromPay;
    }

    public void setFromPay(String fromPay) {
        this.fromPay = fromPay;
    }

    public Date getNotifyTime(){
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

    public String getSign(){
        return payMessage.get("sign");
    }
    public String getOutTradeNo(){
        return payMessage.get("out_trade_no");
    }

    public String getSubject(){
        return payMessage.get("subject");
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

    public Number getTotalFee(){
        String total_fee = payMessage.get("total_fee");
        if (null == total_fee || "".equals(total_fee)){    return 0;      }
        if (isNumber(total_fee)){
            return  new BigDecimal(total_fee);
        }
        return 0;
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
    public String getDiscount(){
        return payMessage.get("discount");

    }
    public String getRefundStatus(){
        return payMessage.get("refund_status");

    }
    public Date getGmtRefund(){

        return parseDate(payMessage.get("gmt_refund"));

    }




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
}
