package com.egzosn.pay.wx.v3.bean.combine;

import java.util.Date;
import java.util.List;

import com.egzosn.pay.common.bean.PayOrder;
import com.egzosn.pay.wx.v3.bean.order.SceneInfo;
import com.egzosn.pay.wx.v3.bean.order.SubOrder;
import com.egzosn.pay.wx.v3.utils.WxConst;

/**
 * 合并支付订单
 *
 * @author Egan
 * <pre>
 * email egan@egzosn.com
 * date 2021/10/5
 * </pre>
 */
public class CombinePayOrder extends PayOrder {

    /**
     * 子单信息，必填，最多50单
     */
    private List<SubOrder> subOrders;
    /**
     * 交易起始时间，选填
     */
    private Date timeStart;

    /**
     * 交易结束时间，选填
     */
    private Date timeExpire;

    /**
     * 支付场景信息描述
     */
    private SceneInfo sceneInfo;

    public List<SubOrder> getSubOrders() {
        return subOrders;
    }

    public void setSubOrders(List<SubOrder> subOrders) {
        this.subOrders = subOrders;
        addAttr(WxConst.SUB_ORDERS, subOrders);
    }

    public Date getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(Date timeStart) {
        this.timeStart = timeStart;
        addAttr(WxConst.TIME_START, timeStart);
    }

    public Date getTimeExpire() {
        return timeExpire;
    }

    public void setTimeExpire(Date timeExpire) {
        this.timeExpire = timeExpire;
        addAttr(WxConst.TIME_EXPIRE, timeExpire);
    }

    /**
     * 合单支付总订单号，要求32个字符内，只能是数字、大小写字母_-|*@ ，且在同一个商户号下唯一 。
     * @return 合单支付总订单号
     */
    public String getCombineOutTradeNo() {
        return getOutTradeNo();
    }

    /**
     * 合单支付总订单号，要求32个字符内，只能是数字、大小写字母_-|*@ ，且在同一个商户号下唯一 。
     * @param combineOutTradeNo 合单支付总订单号
     */
    public void setCombineOutTradeNo(String combineOutTradeNo) {
        setOutTradeNo(combineOutTradeNo);
    }

    public SceneInfo getSceneInfo() {
        return sceneInfo;
    }

    public void setSceneInfo(SceneInfo sceneInfo) {
        this.sceneInfo = sceneInfo;
        addAttr(WxConst.SCENE_INFO, sceneInfo);
    }
}
