package com.egzosn.pay.wx.v3.bean.combine;

import com.alibaba.fastjson.annotation.JSONField;
import com.egzosn.pay.wx.v3.bean.response.order.Amount;

/**
 * 合单支付订单金额信息.
 *
 * @author Egan
 * <pre>
 * email egan@egzosn.com
 * date 2021/10/5
 * </pre>
 */
public class CombineAmount extends Amount {

    /**
     * 子单金额，单位为分，必填
     * 境外场景下，标价金额要超过商户结算币种的最小单位金额，例如结算币种为美元，则标价金额必须大于1美分
     */
    @JSONField(name = "total_amount")
    private Integer totalAmount;

    public CombineAmount() {
    }

    public CombineAmount(Integer totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Integer getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Integer totalAmount) {
        this.totalAmount = totalAmount;
    }


}