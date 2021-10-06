package com.egzosn.pay.wx.v3.bean.combine;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.egzosn.pay.common.bean.PayMessage;
import com.egzosn.pay.wx.v3.bean.order.SceneInfo;
import com.egzosn.pay.wx.v3.bean.response.order.Payer;
import com.egzosn.pay.wx.v3.utils.WxConst;

/**
 * 合单支付回调消息,兼容退款回调
 * @author Egan
 * <pre>
 * email egan@egzosn.com
 * date 2021/10/4
 * </pre>
 */
public class CombinePayMessage extends PayMessage {


    /**
     * 合单商户appid，即合单发起方的appid
     */
    @JSONField(name = WxConst.COMBINE_APPID)
    private String combineAppid;

    /**
     * 合单商户号.
     */
    @JSONField(name = WxConst.COMBINE_MCH_ID)
    private String combineMchid;

    /**
     * 合单商户订单号.
     */
    @JSONField(name =  WxConst.COMBINE_OUT_TRADE_NO)
    private String combineOutTradeNo;


    /**
     * 支付者信息
     */
    @JSONField(name = "combine_payer_info")
    private Payer combinePayerInfo;

    /**
     * 场景信息，合单支付回调只返回device_id
     */
    @JSONField(name = WxConst.SCENE_INFO)
    private SceneInfo sceneInfo;

    /**
     * 合单支付回调子订单.
     */
    @JSONField(name = WxConst.SUB_ORDERS)
    private List<CombineSubOrder> subOrders;

    public String getCombineAppid() {
        return combineAppid;
    }

    public void setCombineAppid(String combineAppid) {
        this.combineAppid = combineAppid;
    }

    public String getCombineMchid() {
        return combineMchid;
    }

    public void setCombineMchid(String combineMchid) {
        this.combineMchid = combineMchid;
    }

    public String getCombineOutTradeNo() {
        return combineOutTradeNo;
    }

    public void setCombineOutTradeNo(String combineOutTradeNo) {
        this.combineOutTradeNo = combineOutTradeNo;
    }

    public Payer getCombinePayerInfo() {
        return combinePayerInfo;
    }

    public void setCombinePayerInfo(Payer combinePayerInfo) {
        this.combinePayerInfo = combinePayerInfo;
    }

    public SceneInfo getSceneInfo() {
        return sceneInfo;
    }

    public void setSceneInfo(SceneInfo sceneInfo) {
        this.sceneInfo = sceneInfo;
    }

    public List<CombineSubOrder> getSubOrders() {
        return subOrders;
    }

    public void setSubOrders(List<CombineSubOrder> subOrders) {
        this.subOrders = subOrders;
    }

    public static final CombinePayMessage create(Map<String, Object> message) {
        CombinePayMessage payMessage = new JSONObject(message).toJavaObject(CombinePayMessage.class);
//        payMessage.setPayType("");
        payMessage.setPayMessage(message);
        return payMessage;
    }
}
