package com.egzosn.pay.union.api;

import com.egzosn.pay.common.api.BasePayService;
import com.egzosn.pay.common.api.Callback;
import com.egzosn.pay.common.api.PayConfigStorage;
import com.egzosn.pay.common.bean.*;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * @author egan
 *
 *
 * email egzosn@gmail.com
 * date 2017/6/15
 */
public class UnionPayServicer extends BasePayService {
    public UnionPayServicer(PayConfigStorage payConfigStorage) {
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
    public Map orderInfo(PayOrder order) {
        return null;
    }

    @Override
    public Map<String, Object> getParameter2Map(Map<String, String[]> parameterMap, InputStream is) {
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
    public <T> T query(String tradeNo, String outTradeNo, Callback<T> callback) {
        return null;
    }

    @Override
    public Map<String, Object> close(String tradeNo, String outTradeNo) {
        return null;
    }

    @Override
    public <T> T close(String tradeNo, String outTradeNo, Callback<T> callback) {
        return null;
    }

    @Override
    public Map<String, Object> refund(String tradeNo, String outTradeNo, BigDecimal refundAmount, BigDecimal totalAmount) {
        return null;
    }

    @Override
    public <T> T refund(String tradeNo, String outTradeNo, BigDecimal refundAmount, BigDecimal totalAmount, Callback<T> callback) {
        return null;
    }

    @Override
    public Map<String, Object> refundquery(String tradeNo, String outTradeNo) {
        return null;
    }

    @Override
    public <T> T refundquery(String tradeNo, String outTradeNo, Callback<T> callback) {
        return null;
    }

    @Override
    public Object downloadbill(Date billDate, String billType) {
        return null;
    }

    @Override
    public <T> T downloadbill(Date billDate, String billType, Callback<T> callback) {
        return null;
    }

    @Override
    public <T> T secondaryInterface(Object tradeNoOrBillDate, String outTradeNoBillType, TransactionType transactionType, Callback<T> callback) {
        return null;
    }
}
