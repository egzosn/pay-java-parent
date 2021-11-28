package com.egzosn.pay.wx.v3.bean.order;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 单品列表信息
 * @author Egan
 *  email egzosn@gmail.com
 *  date 2021/8/1
 */
public class GoodsDetail {


    /**
     * 商户侧商品编码
     */
    @JSONField(name = "merchant_goods_id")
    private String merchantGoodsId;

    /**
     * 微信侧商品编码
     */
    @JSONField(name = "wechatpay_goods_id")
    private String wechatpayGoodsId ;


    /**
     * 商品名称
     */
    @JSONField(name = "goods_name")
    private String goodsName;
    /**
     * 商品数量
     */

    private Integer quantity;
    /**
     * 商品单价
     * 商品单价，单位为分
     */
    @JSONField(name = "unit_price")
    private Integer unitPrice ;


    public String getMerchantGoodsId() {
        return merchantGoodsId;
    }

    public void setMerchantGoodsId(String merchantGoodsId) {
        this.merchantGoodsId = merchantGoodsId;
    }

    public String getWechatpayGoodsId() {
        return wechatpayGoodsId;
    }

    public void setWechatpayGoodsId(String wechatpayGoodsId) {
        this.wechatpayGoodsId = wechatpayGoodsId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(Integer unitPrice) {
        this.unitPrice = unitPrice;
    }
}
