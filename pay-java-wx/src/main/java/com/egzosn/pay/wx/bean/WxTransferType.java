package com.egzosn.pay.wx.bean;

import com.egzosn.pay.common.bean.TransferOrder;
import com.egzosn.pay.common.bean.TransferType;

import java.util.Map;

/**
 * 微信转账类型
 * @author egan
 *         email egzosn@gmail.com
 *         date 2018/9/28.19:56
 */
public enum WxTransferType implements TransferType{
    /**
     * 转账到零钱
     */
    TRANSFERS("mmpaymkttransfers/promotion/transfers"),
    /**
     * 查询转账到零钱的记录
     */
    GETTRANSFERINFO("mmpaymkttransfers/gettransferinfo"),
    /**
     * 转账到银行卡
     */
    PAY_BANK("mmpaysptrans/pay_bank"),
    /**
     * 查询转账到银行卡的记录
     */
    QUERY_BANK("mmpaysptrans/query_bank"),

    ;

    WxTransferType(String method) {
        this.method = method;
    }

    private String method;
    @Override
    public String getType() {
        return this.name();
    }
    @Override
    public String getMethod() {
        return this.method;
    }


    @Override
    public Map<String, Object> setAttr(Map<String, Object> attr, TransferOrder order) {
        return attr;
    }
}
