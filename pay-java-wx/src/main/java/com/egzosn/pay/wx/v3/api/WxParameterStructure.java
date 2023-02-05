package com.egzosn.pay.wx.v3.api;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.egzosn.pay.common.bean.AssistOrder;
import com.egzosn.pay.common.bean.OrderParaStructure;
import com.egzosn.pay.common.bean.PayOrder;
import com.egzosn.pay.common.util.MapGen;
import com.egzosn.pay.wx.v3.utils.WxConst;

/**
 * 微信参数构造器
 *
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
     * @param parameters 参数
     * @return 公共参数
     */
    public Map<String, Object> getPublicParameters(Map<String, Object> parameters) {
        if (payConfigStorage.isPartner()) {
            return parameters;
        }
        if (null == parameters) {
            parameters = new LinkedHashMap<>();
        }
        parameters.put(WxConst.APPID, payConfigStorage.getAppId());
        parameters.put(WxConst.MCH_ID, payConfigStorage.getMchId());
        return parameters;
    }


    /**
     * 加载结算信息
     *
     * @param parameters 订单参数
     * @param order      支付订单
     */
    public void loadSettleInfo(Map<String, Object> parameters, PayOrder order) {
        Object profitSharing = order.getAttr("profit_sharing");
        if (null != profitSharing) {
            Map<String, Object> settleInfo = new MapGen<>("profit_sharing", profitSharing).getAttr();
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
     */
    public void initNotifyUrl(Map<String, Object> parameters, AssistOrder order) {
        OrderParaStructure.loadParameters(parameters, WxConst.NOTIFY_URL, payConfigStorage.getNotifyUrl());
        OrderParaStructure.loadParameters(parameters, WxConst.NOTIFY_URL, order.getNotifyUrl());
        OrderParaStructure.loadParameters(parameters, WxConst.NOTIFY_URL, order);
    }


    /**
     * 获取商户相关信息
     *
     * @return 商户相关信息
     */
    public Map<String, Object> getMchParameters() {
        Map<String, Object> attr = initSubMchId(null);
        OrderParaStructure.loadParameters(attr, payConfigStorage.isPartner() ? WxConst.SP_MCH_ID : WxConst.MCH_ID, payConfigStorage.getMchId());
        return attr;
    }


    /**
     * 初始化商户相关信息
     *
     * @param parameters 参数信息
     * @return 参数信息
     */
    public Map<String, Object> initPartner(Map<String, Object> parameters) {
        if (null == parameters) {
            parameters = new LinkedHashMap<>();
        }
        if (payConfigStorage.isPartner()) {
            parameters.put("sp_appid", payConfigStorage.getAppId());
            parameters.put(WxConst.SP_MCH_ID, payConfigStorage.getMchId());
            OrderParaStructure.loadParameters(parameters, "sub_appid", payConfigStorage.getSubAppId());
            OrderParaStructure.loadParameters(parameters, WxConst.SUB_MCH_ID, payConfigStorage.getSubMchId());
            return parameters;
        }

        parameters.put(WxConst.APPID, payConfigStorage.getAppId());
        parameters.put(WxConst.MCH_ID, payConfigStorage.getMchId());
        return parameters;
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
        if (payConfigStorage.isPartner()) {
            OrderParaStructure.loadParameters(parameters, WxConst.SUB_MCH_ID, payConfigStorage.getSubMchId());
        }

        return parameters;

    }

}
