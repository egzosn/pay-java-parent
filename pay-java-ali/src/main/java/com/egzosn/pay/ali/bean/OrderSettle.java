package com.egzosn.pay.ali.bean;

import com.egzosn.pay.common.util.Util;
import com.egzosn.pay.common.util.str.StringUtils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author egan
 *         email egzosn@gmail.com
 *         date 2019/4/28.20:29
 */
public class OrderSettle {

    /**
     * 结算请求流水号 开发者自行生成并保证唯一性
     */
    private String outRequestNo;
    /**
     * 支付宝订单号
     */
    private String tradeNo;
    /**
     * 分账支出方账户，类型为userId，本参数为要分账的支付宝账号对应的支付宝唯一用户号。以2088开头的纯16位数字。
     */
    private String transOut;

    /**
     * 分账收入方账户，类型为userId，本参数为要分账的支付宝账号对应的支付宝唯一用户号。以2088开头的纯16位数字。
     */
    private String transIn;

    /**
     * 分账的金额，单位为元
     */
    private BigDecimal amount;
    /**
     * 分账信息中分账百分比。取值范围为大于0，少于或等于100的整数。
     */
    private Integer amountPercentage;

    /**
     * 分账描述
     */
    private String desc;

    /**
     * 操作员id
     */
    private String operatorId;

    public String getOutRequestNo() {
        return outRequestNo;
    }

    public void setOutRequestNo(String outRequestNo) {
        this.outRequestNo = outRequestNo;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getTransOut() {
        return transOut;
    }

    public void setTransOut(String transOut) {
        this.transOut = transOut;
    }

    public String getTransIn() {
        return transIn;
    }

    public void setTransIn(String transIn) {
        this.transIn = transIn;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getAmountPercentage() {
        return amountPercentage;
    }

    public void setAmountPercentage(Integer amountPercentage) {
        this.amountPercentage = amountPercentage;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    /**
     * 获取分账明细信息
     * @return 分账明细信息
     */
    public Map<String, Object> toRoyaltyParameters(){
        Map<String, Object> royalty = new TreeMap<String, Object>();

        if (StringUtils.isNotEmpty(transOut)){
            royalty.put("trans_out",  transOut);
        }

        if (StringUtils.isNotEmpty( transIn)){
            royalty.put("trans_in",  transIn);
        }
        if (null !=  amount){
            royalty.put("amount", Util.conversionAmount(amount));
        }
        if (null !=  amountPercentage){
            royalty.put("amount_percentage", amountPercentage);
        }

        if (StringUtils.isNotEmpty(  desc)){
            royalty.put(" desc",   desc);
        }
        return royalty;
    }

    /**
     * 请求参数的集合，最大长度不限，除公共参数外所有请求参数都必须放在这个参数中传递，具体参照各产品快速接入文档
     * @return 请求参数的集合
     */
    public Map<String, Object> toBizContent(){
        Map<String, Object> bizContent = new TreeMap<String, Object>();
        bizContent.put("out_request_no", outRequestNo);
        bizContent.put("trade_no", tradeNo);
        bizContent.put("royalty_parameters", toRoyaltyParameters());
        if (StringUtils.isNotEmpty(operatorId)){
            bizContent.put("operator_id", operatorId);
        }
        return bizContent;
    }



}
