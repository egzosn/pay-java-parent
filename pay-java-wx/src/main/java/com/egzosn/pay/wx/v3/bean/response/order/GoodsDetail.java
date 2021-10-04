package com.egzosn.pay.wx.v3.bean.response.order;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 单品列表信息
 * @author Egan
 * <pre>
 * email egan@egzosn.com
 * date 2021/10/4
 * </pre>
 */
public class GoodsDetail {

    /**
     * 商品编码
     */
    @JSONField(name = "goods_id")
    private String goodsId;
    /**
     * 商品数量
     */
    @JSONField(name = "quantity")
    private Long quantity;
    /**
     * 商品单价
     */
    @JSONField(name = "unit_price")
    private Long unitPrice;
    /**
     * 商品优惠金额，单位【分】
     */
    @JSONField(name = "discount_amount")
    private Long discountAmount;
    /**
     * 商品备注
     */
    @JSONField(name = "goods_remark")
    private String goodsRemark;


    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public Long getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Long unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Long getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(Long discountAmount) {
        this.discountAmount = discountAmount;
    }

    public String getGoodsRemark() {
        return goodsRemark;
    }

    public void setGoodsRemark(String goodsRemark) {
        this.goodsRemark = goodsRemark;
    }
}