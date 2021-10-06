package com.egzosn.pay.wx.v3.bean.response.order;

import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 优惠功能
 *
 * <pre>
 * email egan@egzosn.com
 * date 2021/10/6
 * </pre>
 */

public class PromotionDetail {
    /**
     * 券ID
     */
    @JSONField(name = "coupon_id")
    private String couponId;

    /**
     * 优惠名称
     */
    private String name;

    /**
     * 优惠范围
     * <ul>
     * <li> GLOBAL：全场代金券</li>
     * <li>SINGLE：单品优惠</li>
     * </ul>
     * 示例值：GLOBAL
     */
    private String scope;

    /**
     * 优惠类型
     * <ul>
     * <li>CASH：充值</li>
     * <li>NOCASH：预充值</li>
     * </ul>
     * 示例值：CASH
     */
    private String type;
    /**
     * 优惠券面额，单位【分】
     */
    private Long amount;



    /**
     * 活动ID
     */
    @JSONField(name = "stock_id")
    private String stockId;

    /**
     * 微信出资，单位为分
     */
    @JSONField(name = "wechatpay_contribute")
    private Long wechatpayContribute;

    /**
     * 商户出资，单位为分
     */
    @JSONField(name = "merchant_contribute")
    private Long merchantContribute;

    /**
     * 其他出资，单位为分
     */
    @JSONField(name = "other_contribute")
    private Long otherContribute;

    /**
     * 优惠币种，
     * CNY：人民币，境内商户号仅支持人民币。
     * 示例值：CNY
     */
    private String currency;
    /**
     * 单品列表信息
     */
    @JSONField(name = "goods_detail")
    private List<GoodsDetail> goodsDetail;


    public String getCouponId() {
        return couponId;
    }

    public void setCouponId(String couponId) {
        this.couponId = couponId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getStockId() {
        return stockId;
    }

    public void setStockId(String stockId) {
        this.stockId = stockId;
    }

    public Long getWechatpayContribute() {
        return wechatpayContribute;
    }

    public void setWechatpayContribute(Long wechatpayContribute) {
        this.wechatpayContribute = wechatpayContribute;
    }

    public Long getMerchantContribute() {
        return merchantContribute;
    }

    public void setMerchantContribute(Long merchantContribute) {
        this.merchantContribute = merchantContribute;
    }

    public Long getOtherContribute() {
        return otherContribute;
    }

    public void setOtherContribute(Long otherContribute) {
        this.otherContribute = otherContribute;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public List<GoodsDetail> getGoodsDetail() {
        return goodsDetail;
    }

    public void setGoodsDetail(List<GoodsDetail> goodsDetail) {
        this.goodsDetail = goodsDetail;
    }
}