package com.egzosn.pay.wx.v3.bean.order;


import java.math.BigDecimal;

import com.egzosn.pay.common.bean.CurType;
import com.egzosn.pay.common.bean.DefaultCurType;
import com.egzosn.pay.common.util.Util;

/**
 * 订单金额信息
 * @author Egan
 * email egzosn@gmail.com
 * date 2021/8/1
 */
public class Amount {

    /**
     * 订单总金额，单位为分。
     */
    private Integer total;
    /**
     * 货币类型 CNY：人民币，境内商户号仅支持人民币。
     * {@link com.egzosn.pay.common.bean.CurType}
     */
    private String currency = DefaultCurType.CNY.getType();

    public Amount() {
    }

    public Amount(Integer total) {
        this.total = total;
    }

    public Amount(Integer total, String currency) {
        this.total = total;
        this.currency = currency;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
    /**
     * 订单金额信息
     *
     * @param order 支付订单
     * @return 订单金额信息
     */
    /**
     *  订单金额信息
     * @param total 金额，这里单位为元
     * @param curType 货币类型
     * @return 订单金额信息
     */
    public static Amount getAmount(BigDecimal total, CurType curType ) {
        // 总金额单位为分
        Amount amount = new Amount(Util.conversionCentAmount(total));
        if (null == curType) {
            curType = DefaultCurType.CNY;
        }
        amount.setCurrency(curType.getType());
        return amount;
    }
}
