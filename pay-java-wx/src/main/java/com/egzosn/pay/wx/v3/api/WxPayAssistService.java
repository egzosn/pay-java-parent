package com.egzosn.pay.wx.v3.api;

import java.security.cert.Certificate;
import java.util.Map;

import org.apache.http.HttpEntity;

import com.alibaba.fastjson.JSONObject;
import com.egzosn.pay.common.bean.PayOrder;
import com.egzosn.pay.common.bean.TransactionType;
import com.egzosn.pay.common.http.ResponseEntity;

/**
 * 微信支付辅助服务
 *
 * @author Egan
 * email egzosn@gmail.com
 * date 2021/8/7
 */
public interface WxPayAssistService {


    /**
     * 发起请求
     *
     * @param parameters      支付参数
     * @param transactionType 交易类型
     * @return 响应内容体
     */
    JSONObject doExecute(Map<String, Object> parameters, TransactionType transactionType);


    /**
     * 发起请求
     *
     * @param body            请求内容
     * @param transactionType 交易类型
     * @param uriVariables    用于匹配表达式
     * @return 响应内容体
     */
    JSONObject doExecute(String body, TransactionType transactionType, Object... uriVariables);

    /**
     * 发起请求
     *
     * @param body            请求内容
     * @param transactionType 交易类型
     * @param uriVariables    用于匹配表达式
     * @return 响应内容体
     */
    ResponseEntity<JSONObject> doExecuteEntity(String body, TransactionType transactionType, Object... uriVariables);

    /**
     * 发起请求
     *
     * @param parameters 支付参数
     * @param order      订单
     * @return 请求响应
     */
    JSONObject doExecute(Map<String, Object> parameters, PayOrder order);

    /**
     * 构建请求实体
     * 这里也做签名处理
     *
     * @param url   url
     * @param body   请求内容体
     * @param method 请求方法
     * @return 请求实体
     */
    HttpEntity buildHttpEntity(String url, String body, String method);

    /**
     *  当缓存中平台证书不存在事进行刷新重新获取平台证书
     * 调用/v3/certificates
     *
     */
    void refreshCertificate();

    /**
     * 通过证书序列获取平台证书
     * @param serialNo 证书序列
     * @return 平台证书
     */
    Certificate getCertificate(String serialNo);

}
