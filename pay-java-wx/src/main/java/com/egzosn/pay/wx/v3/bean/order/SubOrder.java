package com.egzosn.pay.wx.v3.bean.order;

import java.util.Date;
import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;
import com.egzosn.pay.wx.v3.bean.WxTransactionType;
import com.egzosn.pay.wx.v3.bean.combine.CombineAmount;
import com.egzosn.pay.wx.v3.bean.combine.CombineSubOrder;
import com.egzosn.pay.wx.v3.bean.response.order.PromotionDetail;
import com.egzosn.pay.wx.v3.bean.response.order.TradeState;

/**
 * 子单信息，最多50单.
 *
 * @author Egan
 * <pre>
 * email egan@egzosn.com
 * date 2021/10/5
 * </pre>
 */
public class SubOrder extends CombineSubOrder {
    /**
     * 合单支付订单金额信息，必填。
     */
    private CombineAmount amount;


    /**
     * 商品描述，必填，需传入应用市场上的APP名字-实际商品名称，例如：天天爱消除-游戏充值。
     */
    private String description;




    /**
     * 结算信息，选填
     */
    @JSONField(name = "settle_info")
    private SettleInfo settleInfo;


    /**
     * 银行类型，采用字符串类型的银行标识。
     * 银行标识请参考 <a target= "_blank" href= "https://pay.weixin.qq.com/wiki/doc/apiv3/terms_definition/chapter1_1_3.shtml#part-6">《银行类型对照表》</a>
     * 示例值：CMC
     */
    @JSONField(name = "bank_type")
    private String bankType;

    /**
     * 附加数据，在查询API和支付通知中原样返回，可作为自定义参数使用
     */
    private String attach;

    /**
     * 支付完成时间|| 退款完成时间，遵循rfc3339标准格式，
     * 格式为YYYY-MM-DDTHH:mm:ss+TIMEZONE，YYYY-MM-DD表示年月日，T出现在字符串中，表示time元素的开头，HH:mm:ss表示时分秒，
     * TIMEZONE表示时区（+08:00表示东八区时间，领先UTC 8小时，即北京时间）。
     * 例如：2015-05-20T13:29:35+08:00表示，北京时间2015年5月20日 13点29分35秒。
     * 示例值：2018-06-08T10:34:56+08:00
     */
    @JSONField(name = "success_time", format = "yyyy-MM-dd'T'HH:mm:ssXXX")
    private Date successTime;

    /**
     * 交易状态
     */
    @JSONField(name = "trade_state")
    private TradeState tradeState;

    /**
     * 交易类型
     */
    @JSONField(name = "trade_type")
    private WxTransactionType tradeType;

    /**
     * 微信支付侧订单号
     */
    @JSONField(name = "transaction_id")
    private String transactionId;

    /**
     * 优惠功能，子单有核销优惠券时有返回
     */
    @JSONField(name = "promotion_detail")
    private List<PromotionDetail> promotionDetail;

    public CombineAmount getAmount() {
        return amount;
    }

    public void setAmount(CombineAmount amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }



    public SettleInfo getSettleInfo() {
        return settleInfo;
    }

    public void setSettleInfo(SettleInfo settleInfo) {
        this.settleInfo = settleInfo;
    }

    public String getBankType() {
        return bankType;
    }

    public void setBankType(String bankType) {
        this.bankType = bankType;
    }

    public String getAttach() {
        return attach;
    }

    public void setAttach(String attach) {
        this.attach = attach;
    }

    public Date getSuccessTime() {
        return successTime;
    }

    public void setSuccessTime(Date successTime) {
        this.successTime = successTime;
    }

    public TradeState getTradeState() {
        return tradeState;
    }

    public void setTradeState(TradeState tradeState) {
        this.tradeState = tradeState;
    }

    public WxTransactionType getTradeType() {
        return tradeType;
    }

    public void setTradeType(WxTransactionType tradeType) {
        this.tradeType = tradeType;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public List<PromotionDetail> getPromotionDetail() {
        return promotionDetail;
    }

    public void setPromotionDetail(List<PromotionDetail> promotionDetail) {
        this.promotionDetail = promotionDetail;
    }
}
