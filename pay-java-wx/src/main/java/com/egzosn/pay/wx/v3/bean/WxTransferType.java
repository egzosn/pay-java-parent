package com.egzosn.pay.wx.v3.bean;

import java.util.Map;

import com.egzosn.pay.common.bean.MethodType;
import com.egzosn.pay.common.bean.TransferOrder;
import com.egzosn.pay.common.bean.TransferType;

/**
 * 微信转账类型
 *
 * @author egan
 * email egzosn@gmail.com
 * date 2018/9/28.19:56
 */
public enum WxTransferType implements TransferType {
    /**
     * 转账到零钱
     */
    TRANSFER_BATCHES("/v3/transfer/batches", MethodType.POST),
    /**
     * 查询转账到零钱的记录,通过微信批次单号查询批次单
     */
    QUERY_BATCH_BY_BATCH_ID("/v3/transfer/batches/batch-id/{batch_id}"),
    /**
     * 查询转账到零钱的记录,通过商家批次单号查询批次单
     */
    QUERY_BATCH_BY_OUT_BATCH_NO("/v3/transfer/batches/out-batch-no/{out_batch_no}"),
    /**
     * 通过微信明细单号查询明细单
     */
    QUERY_BATCH_DETAIL_BY_BATCH_ID("/v3/transfer/batches/batch-id/{batch_id}/details/detail-id/{detail_id}"),
    /**
     * 通过商家明细单号查询明细单
     */
    QUERY_BATCH_DETAIL_BY_OUT_BATCH_NO("/v3/transfer/batches/out-batch-no/{out_batch_no}/details/out-detail-no/{out_detail_no}"),
    /**
     * 转账账单电子回单申请受理接口
     */
    TRANSFER_BILL_RECEIPT("/v3/transfer/bill-receipt", MethodType.POST),
    /**
     * 查询转账账单电子回单接口
     */
    QUERY_TRANSFER_BILL_RECEIPT("/v3/transfer/bill-receipt/{out_batch_no}"),
    /**
     * 受理转账明细电子回单API
     */
    TRANSFER_DETAIL_ELECTRONIC_RECEIPTS("/v3/transfer-detail/electronic-receipts", MethodType.POST),
    /**
     * 查询转账明细电子回单受理结果API
     */
    QUERY_TRANSFER_DETAIL_ELECTRONIC_RECEIPTS("/v3/transfer-detail/electronic-receipts"),
    ;

    WxTransferType(String type, MethodType method) {
        this.type = type;
        this.method = method;
    }

    WxTransferType(String type) {
        this(type, MethodType.GET);
    }

    private String type;
    private MethodType method;


    @Override
    public String getType() {
        return type;
    }

    @Override
    public String getMethod() {
        return this.method.name();
    }

    /**
     * 设置属性
     *
     * @param attr  已有属性对象
     * @param order 转账订单
     * @return 属性对象
     */
    @Override
    public Map<String, Object> setAttr(Map<String, Object> attr, TransferOrder order) {
        return attr;
    }
}
