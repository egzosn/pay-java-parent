package com.egzosn.pay.wx.v3.api;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;


import com.egzosn.pay.common.bean.Order;
import com.egzosn.pay.common.bean.OrderParaStructure;
import com.egzosn.pay.common.bean.PayOrder;
import com.egzosn.pay.common.http.UriVariables;
import com.egzosn.pay.common.util.MapGen;
import com.egzosn.pay.common.util.str.StringUtils;
import com.egzosn.pay.wx.v3.utils.WxConst;

/**
 * 微信参数构造器
 * @author Egan
 * <pre>
 * email egzosn@gmail.com
 * date 2021/8/16
 * </pre>
 */
public class WxParameterStructure {
    private final WxPayConfigStorage payConfigStorage;

    public WxParameterStructure(WxPayConfigStorage payConfigStorage) {
        this.payConfigStorage = payConfigStorage;
    }

    /**
     * 获取公共参数
     *
     * @return 公共参数
     */
    public Map<String, Object> getPublicParameters() {

        Map<String, Object> parameters = new LinkedHashMap<>();
        parameters.put(WxConst.APPID, payConfigStorage.getAppId());
        parameters.put(WxConst.MCH_ID, payConfigStorage.getMchId());
        return parameters;
    }






    /**
     * 加载结算信息
     *
     * @param parameters 订单参数
     * @param order      支付订单
     * @return 订单参数
     */
    public void loadSettleInfo(Map<String, Object> parameters, PayOrder order) {
        Object profitSharing = order.getAttr("profit_sharing");
        if (null != profitSharing) {
            Map<String, Object> settleInfo = new MapGen<String, Object>("profit_sharing", profitSharing).getAttr();
            parameters.put("settle_info", settleInfo);
            return;
        }
        //结算信息
        OrderParaStructure.loadParameters(parameters, "settle_info", order);


    }



    /**
     * 初始化通知URL必须为直接可访问的URL，不允许携带查询串，要求必须为https地址。
     *
     * @param parameters 订单参数
     * @param order      订单信息
     * @return 订单参数
     */
    public void initNotifyUrl(Map<String, Object> parameters, Order order) {
        OrderParaStructure.loadParameters(parameters, WxConst.NOTIFY_URL, payConfigStorage.getNotifyUrl());
        OrderParaStructure.loadParameters(parameters, WxConst.NOTIFY_URL, order);
    }

    /**
     * 获取服务商相关信息
     *
     * @return 服务商相关信息
     */
    public String getSpParameters() {
        Map<String, Object> attr = initSubMchId(null);
        OrderParaStructure.loadParameters(attr, WxConst.SP_MCH_ID, payConfigStorage.getSpMchId());
        return UriVariables.getMapToParameters(attr);
    }
    /**
     * 初始化商户相关信息
     *
     * @param parameters 参数信息
     * @return 参数信息
     */
    public void initPartner(Map<String, Object> parameters) {
        if (null == parameters) {
            parameters = new HashMap<>();
        }
        if (StringUtils.isNotEmpty(payConfigStorage.getSpAppId()) && StringUtils.isNotEmpty(payConfigStorage.getSpMchId())) {
            payConfigStorage.setPartner(true);
            parameters.put("sp_appid", payConfigStorage.getSpAppId());
            parameters.put(WxConst.SP_MCH_ID, payConfigStorage.getSpMchId());
        }
        OrderParaStructure.loadParameters(parameters, "sub_appid", payConfigStorage.getSubAppId());
        initSubMchId(parameters);
    }


    /**
     * 初始化商户相关信息
     *
     * @param parameters 参数信息
     * @return 参数信息
     */
    public Map<String, Object> initSubMchId(Map<String, Object> parameters) {
        if (null == parameters) {
            parameters = new HashMap<>();
        }
        if (StringUtils.isNotEmpty(payConfigStorage.getSubMchId())) {
            payConfigStorage.setPartner(true);
            parameters.put(WxConst.SUB_MCH_ID, payConfigStorage.getSubMchId());
        }
        return parameters;

    }

}
