package com.egzosn.pay.wx.v3.bean.order;

import java.util.List;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 优惠功能
 * @author Egan
 * email egzosn@gmail.com
 * date 2021/8/1
 */
public class Detail {
    /**
     * 订单原价
     */
    @JSONField(name = "cost_price")
    private Integer costPrice;
    /**
     * 商家小票
     */
    @JSONField(name = "invoice_id")
    private String invoiceId;

    @JSONField(name = "goods_detail")
    private List<GoodsDetail> goodsDetail;

    public Integer getCostPrice() {
        return costPrice;
    }

    public void setCostPrice(Integer costPrice) {
        this.costPrice = costPrice;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public List<GoodsDetail> getGoodsDetail() {
        return goodsDetail;
    }

    public void setGoodsDetail(List<GoodsDetail> goodsDetail) {
        this.goodsDetail = goodsDetail;
    }
}
