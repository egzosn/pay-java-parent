package com.egzosn.pay.paypal.api;

/**
 * Created by egzosn on 2018/4/8.
 */

import com.egzosn.pay.common.api.BasePayService;
import com.egzosn.pay.common.api.PayConfigStorage;
import com.egzosn.pay.common.bean.*;

import java.awt.image.BufferedImage;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * 贝宝支付配置存储
 * @author  egan
 *
 * email egzosn@gmail.com
 * date 2018-4-8 ‏‎22:15:09
 */
public class PayPalPayService extends BasePayService{
    public PayPalPayService(PayConfigStorage payConfigStorage) {
        super(payConfigStorage);
    }

    @Override
    public boolean verify(Map<String, Object> params) {
        return false;
    }

    @Override
    public boolean signVerify(Map<String, Object> params, String sign) {
        return false;
    }

    @Override
    public boolean verifySource(String id) {
        return false;
    }

    @Override
    public Map<String, Object> orderInfo(PayOrder order) {
        return null;
    }

    @Override
    public PayOutMessage getPayOutMessage(String code, String message) {
        return null;
    }

    @Override
    public PayOutMessage successPayOutMessage(PayMessage payMessage) {
        return null;
    }

    @Override
    public String buildRequest(Map<String, Object> orderInfo, MethodType method) {
        return null;
    }

    @Override
    public BufferedImage genQrPay(PayOrder order) {
        return null;
    }

    @Override
    public Map<String, Object> microPay(PayOrder order) {
        return null;
    }

    @Override
    public Map<String, Object> query(String tradeNo, String outTradeNo) {
        return null;
    }

    @Override
    public Map<String, Object> close(String tradeNo, String outTradeNo) {
        return null;
    }

    @Override
    public Map<String, Object> refund(String tradeNo, String outTradeNo, BigDecimal refundAmount, BigDecimal totalAmount) {
        return null;
    }

    @Override
    public Map<String, Object> refund(RefundOrder refundOrder) {
        return null;
    }

    @Override
    public Map<String, Object> refundquery(String tradeNo, String outTradeNo) {
        return null;
    }

    @Override
    public Map<String, Object> downloadbill(Date billDate, String billType) {
        return null;
    }

    @Override
    public Map<String, Object> secondaryInterface(Object tradeNoOrBillDate, String outTradeNoBillType, TransactionType transactionType) {
        return null;
    }
}
