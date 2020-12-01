package com.egzosn.pay.ali.bean;

import java.math.BigDecimal;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 退回的前置资产列表
 *
 * @author Egan
 * <pre>
 * email egzosn@gmail.com
 * date 2020/8/16 18:58
 * </pre>
 */
public class PresetPayToolInfo {

    /**
     * 必填 	32前置资产金额 	12.21
     */
    private BigDecimal[] amount;

    /**
     * 前置资产类型编码，和收单支付传入的preset_pay_tool里面的类型编码保持一致。盒马礼品卡:HEMA；抓猫猫红包:T_CAT_COUPON
     */
    @JSONField(name = "assert_type_code")
    private String assertTypeCode;

    public BigDecimal[] getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal[] amount) {
        this.amount = amount;
    }

    public String getAssertTypeCode() {
        return assertTypeCode;
    }

    public void setAssertTypeCode(String assertTypeCode) {
        this.assertTypeCode = assertTypeCode;
    }
}
