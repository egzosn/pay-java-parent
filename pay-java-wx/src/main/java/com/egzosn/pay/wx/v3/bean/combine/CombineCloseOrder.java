package com.egzosn.pay.wx.v3.bean.combine;

import java.util.List;

import com.egzosn.pay.common.bean.AssistOrder;

import com.egzosn.pay.wx.v3.utils.WxConst;

/**
 * 微信合单关闭订单
 * @author Egan
 * <pre>
 * email egan@egzosn.com
 * date 2021/10/6
 * </pre>
 */
public class CombineCloseOrder extends AssistOrder {

    /**
     * 子单信息，必填，最多50单
     */
    private List<CombineSubOrder> subOrders;


    public List<CombineSubOrder> getSubOrders() {
        return subOrders;
    }

    public void setSubOrders(List<CombineSubOrder> subOrders) {
        this.subOrders = subOrders;
        addAttr(WxConst.SUB_ORDERS, subOrders);
    }
}
