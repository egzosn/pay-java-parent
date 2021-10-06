package com.egzosn.pay.wx.v3.bean.sharing;

import com.egzosn.pay.common.bean.RefundOrder;
import com.egzosn.pay.wx.v3.utils.WxConst;

/**
 * 分账回退订单
 * @author Egan
 * <pre>
 * email egan@egzosn.com
 * date 2021/10/6
 * </pre>
 */
public class ProfitSharingReturnOrder extends RefundOrder {

    /**
     * 子商户号，选填，服务商必填
     */
    private String subMchid;

    public String getSubMchid() {
        return subMchid;
    }

    public void setSubMchid(String subMchid) {
        this.subMchid = subMchid;
        addAttr(WxConst.SUB_MCH_ID, subMchid);
    }
}
