package com.egzosn.pay.wx.v3.bean.transfer;

import com.egzosn.pay.common.bean.AssistOrder;
import com.egzosn.pay.wx.v3.utils.WxConst;

/**
 * 微信转账查询订单
 *
 * @author Egan
 * <pre>
 * email egan@egzosn.com
 * date 2023/1/8
 * </pre>
 */
public class WxTransferQueryOrder extends AssistOrder {

    /**
     * 微信批次单号 或 商家批次单号
     */
    private String batchId;

    /**
     * 微信支付系统内部区分转账批次单下不同转账明细单的唯一标识
     */
    private String detailId;


    /**
     * 商户系统内部的商家批次单号，在商户系统内部唯一
     */
    private String outBatchNo;
    /**
     * 商户系统内部区分转账批次单下不同转账明细单的唯一标识
     */
    private String outDetailNo;

    /**
     * 是否查询转账明细单
     * true-是；false-否，默认否。商户可选择是否查询指定状态的转账明细单，当转账批次单状态为“FINISHED”（已完成）时，才会返回满足条件的转账明细单
     */
    private Boolean needQueryDetail;
    /**
     * 请求资源起始位置
     * 该次请求资源的起始位置。返回的明细是按照设置的明细条数进行分页展示的，一次查询可能无法返回所有明细，我们使用该参数标识查询开始位置，默认值为0
     */
    private Integer offset;
    /**
     * 最大资源条数
     * 该次请求可返回的最大明细条数，最小20条，最大100条，不传则默认20条。不足20条按实际条数返回
     */
    private Integer limit;
    /**
     * 明细状态
     * WAIT_PAY: 待确认。待商户确认, 符合免密条件时, 系统会自动扭转为转账中
     * ALL:全部。需要同时查询转账成功和转账失败的明细单
     * SUCCESS:转账成功
     * FAIL:转账失败。需要确认失败原因后，再决定是否重新发起对该笔明细单的转账（并非整个转账批次单）
     */
    private String detailStatus;

    public String getBatchId() {
        return batchId;
    }

    public void setBatchId(String batchId) {
        setTradeNo(batchId);
        this.batchId = batchId;
    }

    public String getOutBatchNo() {
        return outBatchNo;
    }

    public void setOutBatchNo(String outBatchNo) {
        setOutTradeNo(outBatchNo);
        this.outBatchNo = outBatchNo;
    }

    public String getDetailId() {
        return detailId;
    }

    public void setDetailId(String detailId) {
        addAttr(WxConst.DETAIL_ID, detailId);
        this.detailId = detailId;
    }

    public String getOutDetailNo() {
        return outDetailNo;
    }

    public void setOutDetailNo(String outDetailNo) {
        addAttr(WxConst.OUT_DETAIL_NO, detailId);
        this.outDetailNo = outDetailNo;
    }

    public Boolean getNeedQueryDetail() {
        return needQueryDetail;
    }

    public void setNeedQueryDetail(Boolean needQueryDetail) {
        addAttr(WxConst.NEED_QUERY_DETAIL, needQueryDetail);
        this.needQueryDetail = needQueryDetail;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        addAttr(WxConst.OFFSET, offset);
        this.offset = offset;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        addAttr(WxConst.LIMIT, limit);
        this.limit = limit;
    }


    public String getDetailStatus() {
        return detailStatus;
    }

    public void setDetailStatus(String detailStatus) {
        addAttr(WxConst.DETAIL_STATUS, detailStatus);
        this.detailStatus = detailStatus;
    }
}
