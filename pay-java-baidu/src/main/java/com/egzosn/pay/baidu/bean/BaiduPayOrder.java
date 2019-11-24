package com.egzosn.pay.baidu.bean;

import com.alibaba.fastjson.JSONObject;
import com.egzosn.pay.common.bean.PayOrder;
import com.egzosn.pay.common.bean.TransactionType;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

public class BaiduPayOrder extends PayOrder {
    
    /**
     * 需要隐藏的支付方式
     */
    private List<TransactionType> bannedChannels = Collections.emptyList();
    
    /**
     * 固定值
     */
    private String signFieldsRange;
    
    /**
     * 附加信息
     */
    private JSONObject bizInfo = new JSONObject();
    
    public BaiduPayOrder(String dealTitle,
                         BigDecimal totalAmount,
                         String tpOrderId,
                         String signFieldsRange) {
        this(dealTitle, totalAmount, tpOrderId, signFieldsRange, Collections.<TransactionType>emptyList());
    }
    
    public BaiduPayOrder(String dealTitle,
                         BigDecimal totalAmount,
                         String tpOrderId,
                         String signFieldsRange,
                         List<TransactionType> bannedChannels) {
        setPrice(totalAmount);
        setOutTradeNo(tpOrderId);
        setSubject(dealTitle);
        setSignFieldsRange(signFieldsRange);
        setBannedChannels(bannedChannels);
    }
    
    public JSONObject getBizInfo() {
        return bizInfo;
    }
    
    public void setBizInfo(JSONObject bizInfo) {
        this.bizInfo = bizInfo;
    }
    
    public List<TransactionType> getBannedChannels() {
        return bannedChannels;
    }
    
    public void setBannedChannels(List<TransactionType> bannedChannels) {
        this.bannedChannels = bannedChannels;
    }
    
    public String getSignFieldsRange() {
        return signFieldsRange;
    }
    
    public void setSignFieldsRange(String signFieldsRange) {
        this.signFieldsRange = signFieldsRange;
    }
    
}
