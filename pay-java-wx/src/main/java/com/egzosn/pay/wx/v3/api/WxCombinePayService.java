package com.egzosn.pay.wx.v3.api;

import java.util.LinkedHashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.egzosn.pay.common.bean.AssistOrder;
import com.egzosn.pay.common.bean.Order;
import com.egzosn.pay.common.bean.OrderParaStructure;
import com.egzosn.pay.common.bean.PayMessage;
import com.egzosn.pay.common.bean.PayOrder;
import com.egzosn.pay.common.bean.result.PayException;
import com.egzosn.pay.common.exception.PayErrorException;
import com.egzosn.pay.common.http.HttpConfigStorage;
import com.egzosn.pay.common.util.DateUtils;
import com.egzosn.pay.common.util.MapGen;
import com.egzosn.pay.common.util.str.StringUtils;
import com.egzosn.pay.wx.v3.bean.WxTransactionType;
import com.egzosn.pay.wx.v3.bean.combine.CombinePayMessage;
import com.egzosn.pay.wx.v3.utils.WxConst;

/**
 * 微信合单支付服务
 *
 * @author egan
 * <pre>
 * email egan@egzosn.com
 * date 2021/10/6
 * </pre>
 */
public class WxCombinePayService extends WxPayService {

    /**
     * 创建支付服务
     *
     * @param payConfigStorage 微信对应的支付配置
     */
    public WxCombinePayService(WxPayConfigStorage payConfigStorage) {
        super(payConfigStorage);
    }

    /**
     * 创建支付服务
     *
     * @param payConfigStorage 微信对应的支付配置
     * @param configStorage    微信对应的网络配置，包含代理配置、ssl证书配置
     */
    public WxCombinePayService(WxPayConfigStorage payConfigStorage, HttpConfigStorage configStorage) {
        super(payConfigStorage, configStorage);
    }


    /**
     * 获取公共参数
     *
     * @return 公共参数
     */
    public Map<String, Object> getPublicParameters() {
        Map<String, Object> parameters = new LinkedHashMap<>();
        parameters.put(WxConst.COMBINE_APPID, payConfigStorage.getAppId());
        parameters.put(WxConst.COMBINE_MCH_ID, payConfigStorage.getMchId());
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
     * 微信统一下单接口
     *
     * @param order 支付订单集
     * @return 下单结果
     */
    @Override
    public JSONObject unifiedOrder(PayOrder order) {

        //统一下单
        Map<String, Object> parameters = getPublicParameters();

        // 订单号
        parameters.put(WxConst.COMBINE_OUT_TRADE_NO, order.getOutTradeNo());

        OrderParaStructure.loadDateParameters(parameters, WxConst.TIME_START, order, DateUtils.YYYY_MM_DD_T_HH_MM_SS_XX);
        OrderParaStructure.loadDateParameters(parameters, WxConst.TIME_EXPIRE, order, DateUtils.YYYY_MM_DD_T_HH_MM_SS_XX);
        initNotifyUrl(parameters, order);
        //支付场景描述
        OrderParaStructure.loadParameters(parameters, WxConst.SCENE_INFO, order);
        //子单信息 最多支持子单条数：50
        OrderParaStructure.loadParameters(parameters, WxConst.SUB_ORDERS, order);
        //支付者信息
        if (StringUtils.isNotEmpty(order.getOpenid())) {
            parameters.put("combine_payer_info", new MapGen<>("openid", order.getOpenid()).getAttr());
        }

        return getAssistService().doExecute(parameters, order);
    }


    /**
     * 交易查询接口
     *
     * @param transactionId 微信支付平台订单号
     * @param outTradeNo    商户单号
     * @return 返回查询回来的结果集，支付方原值返回
     */
    @Override
    public Map<String, Object> query(String transactionId, String outTradeNo) {
        return query(new AssistOrder(outTradeNo));
    }
    /**
     * 交易查询接口
     *
     * @param assistOrder 查询条件
     * @return 返回查询回来的结果集，支付方原值返回
     */
    @Override
    public Map<String, Object> query(AssistOrder assistOrder) {
        return getAssistService().doExecute("", WxTransactionType.COMBINE_TRANSACTION, assistOrder.getOutTradeNo());
    }

    /**
     * 交易关闭接口
     *
     * @param transactionId 支付平台订单号
     * @param outTradeNo    商户单号
     * @return 返回支付方交易关闭后的结果
     */
    @Override
    public Map<String, Object> close(String transactionId, String outTradeNo) {
        throw new PayErrorException(new PayException("failure", "合单关闭必须要有子单"));
    }

    /**
     * 交易关闭接口
     *
     * @param assistOrder 关闭订单
     * @return 返回支付方交易关闭后的结果
     */
    @Override
    public Map<String, Object> close(AssistOrder assistOrder) {
        Map<String, Object> parameters = new MapGen<String, Object>(WxConst.COMBINE_APPID, payConfigStorage.getAppId())
                .keyValue(WxConst.SUB_ORDERS, assistOrder.getAttr(WxConst.SUB_ORDERS))
                .getAttr();
        String requestBody = JSON.toJSONString(parameters, SerializerFeature.WriteMapNullValue);
        return getAssistService().doExecute(requestBody, WxTransactionType.COMBINE_CLOSE, assistOrder.getOutTradeNo());
    }

    /**
     * 创建消息
     *
     * @param message 支付平台返回的消息
     * @return 支付消息对象
     */
    @Override
    public PayMessage createMessage(Map<String, Object> message) {
        return CombinePayMessage.create(message);
    }
}
