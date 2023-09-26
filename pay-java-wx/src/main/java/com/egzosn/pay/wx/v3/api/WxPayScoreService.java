package com.egzosn.pay.wx.v3.api;

import com.alibaba.fastjson.JSON;
import com.egzosn.pay.common.bean.AssistOrder;
import com.egzosn.pay.common.bean.Order;
import com.egzosn.pay.common.bean.OrderParaStructure;
import com.egzosn.pay.common.http.HttpConfigStorage;
import com.egzosn.pay.common.http.UriVariables;
import com.egzosn.pay.common.util.DateUtils;
import com.egzosn.pay.common.util.str.StringUtils;
import com.egzosn.pay.wx.v3.bean.WxPayScoreTransactionType;
import com.egzosn.pay.wx.v3.bean.payscore.*;
import com.egzosn.pay.wx.v3.utils.WxConst;

import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * 微信支付分API服务
 *
 * @author neon
 * date 2023/9/12
 */
public class WxPayScoreService extends WxPayService{

    public WxPayScoreService(WxPayConfigStorage payConfigStorage) {
        super(payConfigStorage);
    }

    public WxPayScoreService(WxPayConfigStorage payConfigStorage, HttpConfigStorage configStorage) {
        super(payConfigStorage, configStorage);
    }

    private Map<String, Object> getPublicParameters() {
        Map<String, Object> parameters = new TreeMap<>();
        parameters.put("appid", payConfigStorage.getAppId());
        parameters.put("mch_id", payConfigStorage.getPid());
        parameters.put("service_id", payConfigStorage.getServiceId());
        return parameters;
    }

    /**
     * 初始化通知URL必须为直接可访问的URL，不允许携带查询串，要求必须为https地址。
     *
     * @param parameters 订单参数
     * @param order      订单信息
     */
    public void initNotifyUrl(Map<String, Object> parameters, Order order) {
        OrderParaStructure.loadParameters(parameters, WxConst.NOTIFY_URL, payConfigStorage.getNotifyUrl());
        OrderParaStructure.loadParameters(parameters, WxConst.NOTIFY_URL, order);
    }

    /**
     * 商户预授权API
     */
    public Map<String, Object> permissions(String authorizationCode) {
        Map<String, Object> parameters = getPublicParameters();
        OrderParaStructure.loadParameters(parameters,WxConst.AUTHORIZATION_CODE,authorizationCode);
        OrderParaStructure.loadParameters(parameters, WxConst.NOTIFY_URL, payConfigStorage.getNotifyUrl());
        return getAssistService().doExecute(parameters, WxPayScoreTransactionType.PERMISSIONS);
    }

    public Map<String, Object> queryPermissionsByAuthorizationCode(String authorizationCode) {
        Map<String, Object> parameters = getPublicParameters();
        OrderParaStructure.loadParameters(parameters,WxConst.AUTHORIZATION_CODE,authorizationCode);
        String params = JSON.toJSONString(parameters);
        return getAssistService().doExecute(params, WxPayScoreTransactionType.QUERY_PERMISSIONS_AUTHORIZATION_CODE,authorizationCode);
    }

    public Map<String, Object> terminatePermissionsByAuthorizationCode(String authorizationCode,String reason) {
        Map<String, Object> parameters = getPublicParameters();
        OrderParaStructure.loadParameters(parameters,WxConst.AUTHORIZATION_CODE,authorizationCode);
        OrderParaStructure.loadParameters(parameters,"reason",reason);
        String params = JSON.toJSONString(parameters);
        return getAssistService().doExecute(params, WxPayScoreTransactionType.UNBIND_PERMISSIONS_AUTHORIZATION_CODE,authorizationCode);
    }

    public Map<String, Object> queryPermissionsByOpenId(String openId) {
        Map<String, Object> parameters = getPublicParameters();
        OrderParaStructure.loadParameters(parameters,"open_id",openId);
        String params = JSON.toJSONString(parameters);
        return getAssistService().doExecute(params, WxPayScoreTransactionType.QUERY_PERMISSIONS_OPENID,openId);
    }

    public Map<String, Object> terminatePermissionsByOpenId(String openId,String reason) {
        Map<String, Object> parameters = getPublicParameters();
        OrderParaStructure.loadParameters(parameters,"open_id",openId);
        OrderParaStructure.loadParameters(parameters,"reason",reason);
        String params = JSON.toJSONString(parameters);
        return getAssistService().doExecute(params, WxPayScoreTransactionType.UNBIND_PERMISSIONS_OPENID,openId);
    }

    /**
     * 微信创建支付分订单
     */
    public Map<String,Object> create(CreateOrder createOrder) {
        Map<String, Object> parameters = getPublicParameters();
        OrderParaStructure.loadParameters(parameters,"out_order_no", createOrder.getOutTradeNo());
        OrderParaStructure.loadParameters(parameters,"service_introduction", createOrder.getServiceIntroduction());
        RiskFund riskFund = new RiskFund();
        riskFund.setName(createOrder.getRiskFundName());
        riskFund.setAmount(createOrder.getRiskFundAmount());
        parameters.put("risk_fund",riskFund);

        String attach = createOrder.getAttach();
        if (StringUtils.isNotBlank(attach)) {
            String attachEncode = URLEncoder.encode(attach);
            OrderParaStructure.loadParameters(parameters,"attach",attachEncode.length() <= 256 ? attachEncode : attachEncode.substring(0, 255));
        }
        TimeRange timeRange = new TimeRange();
        timeRange.setStartTime(createOrder.getStartTime());
        timeRange.setEndTime(createOrder.getEndTime());
        parameters.put("time_range", timeRange);
        initNotifyUrl(parameters, createOrder);
        OrderParaStructure.loadParameters(parameters,"openid", createOrder.getOpenId());
        if (null != createOrder.getNeedUserConfirm()) {
            OrderParaStructure.loadParameters(parameters,"need_user_confirm", createOrder.getNeedUserConfirm().toString());
        }
        return getAssistService().doExecute(parameters, WxPayScoreTransactionType.CREATE);
    }

    /**
     * 支付分订单撤销
     */
    @Override
    public Map<String,Object>  cancel(String orderNo, String reason) {
        Map<String, Object> parameters = getPublicParameters();
        reason = reason.length() <= 50 ? reason : reason.substring(0, 50);
        parameters.put("reason", reason);
        String params = JSON.toJSONString(parameters);
        return getAssistService().doExecute(params, WxPayScoreTransactionType.CANCEL, orderNo);
    }


    public Map<String,Object> modify(ModifyOrder modifyOrder) {
        Map<String, Object> parameters = getPublicParameters();

        parameters.put("post_payments", modifyOrder.getPostPayments());
        parameters.put("total_amount", modifyOrder.getTotalAmount());
        OrderParaStructure.loadParameters(parameters,"reason", modifyOrder.getReason());
        String params = JSON.toJSONString(parameters);
        return  getAssistService().doExecute(params, WxPayScoreTransactionType.MODIFY, modifyOrder.getOutTradeNo());
    }


    public Map<String,Object> complete(CompleteOrder completeOrder){
        Map<String, Object> parameters = getPublicParameters();
        parameters.put("post_payments", completeOrder.getPostPayments());
        parameters.put("total_amount", completeOrder.getTotalAmount());
        String params = JSON.toJSONString(parameters);
        return getAssistService().doExecute(params, WxPayScoreTransactionType.COMPLETE, completeOrder.getOutTradeNo());
    }


    public Map<String,Object> sync(String outOrderNo, Date payTime){
        Map<String, Object> parameters = getPublicParameters();
        parameters.put("type", "Order_Paid");
        Map<String,Object> detail = new HashMap<>();
        detail.put("paid_time", DateUtils.formatDate(payTime,DateUtils.YYYYMMDDHHMMSS));
        parameters.put("detail", detail);
        String params = JSON.toJSONString(parameters);
        return getAssistService().doExecute(params, WxPayScoreTransactionType.SYNC,outOrderNo);
    }


    @Override
    public Map<String, Object> query(AssistOrder assistOrder){
        String outTradeNo = assistOrder.getOutTradeNo();
        Map<String, Object> publicParameters = getPublicParameters();
        if (StringUtils.isNotBlank(outTradeNo)) {
            OrderParaStructure.loadParameters(publicParameters,"out_order_no",outTradeNo);
        }
        String parameters = UriVariables.getMapToParameters(publicParameters);
        return getAssistService().doExecute(parameters, WxPayScoreTransactionType.QUERY);
    }
}
