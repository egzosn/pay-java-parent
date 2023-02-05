package com.egzosn.pay.wx.v3.bean.transfer;

import java.math.BigDecimal;
import java.util.List;

import com.egzosn.pay.common.bean.TransferOrder;
import com.egzosn.pay.wx.v3.utils.WxConst;

/**
 * 发起商家转账
 *
 * @author Egan
 * <pre>
 * email egan@egzosn.com
 * date 2023/1/8
 * </pre>
 */
public class WxTransferOrder extends TransferOrder {

    /**
     * 商家批次单号
     */
    private String outBatchNo;
    /**
     * 批次名称
     */
    private String batchName;
    /**
     * 批次备注
     */
    private String batchRemark;
    /**
     * 发起批量转账的明细列表，最多三千笔
     */
    private List<TransferDetail> transferDetailList;
    /**
     * 转账总金额,单位为“分”。转账总金额必须与批次内所有明细转账金额之和保持一致，否则无法发起转账操作
     */
    private BigDecimal totalAmount;
    /**
     * 转账总笔数,一个转账批次单最多发起三千笔转账。转账总笔数必须与批次内所有明细之和保持一致，否则无法发起转账操作
     */
    private Integer totalNum;

    /**
     * 必填，指定该笔转账使用的转账场景ID
     */
    private String transferSceneId;


    public String getOutBatchNo() {
        return outBatchNo;
    }

    public void setOutBatchNo(String outBatchNo) {
        setBatchNo(outBatchNo);
        this.outBatchNo = outBatchNo;
    }

    public String getBatchName() {
        return batchName;
    }

    public void setBatchName(String batchName) {
        addAttr(WxConst.BATCH_NAME, batchName);
        this.batchName = batchName;
    }

    public String getBatchRemark() {
        return batchRemark;
    }

    public void setBatchRemark(String batchRemark) {
        setRemark(batchRemark);
        this.batchRemark = batchRemark;
    }

    public List<TransferDetail> getTransferDetailList() {
        return transferDetailList;
    }

    public void setTransferDetailList(List<TransferDetail> transferDetailList) {
        addAttr(WxConst.TRANSFER_DETAIL_LIST, transferDetailList);
        this.transferDetailList = transferDetailList;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    /**
     * 转账金额单位为“元”。转账总金额必须与批次内所有明细转账金额之和保持一致，否则无法发起转账操作
     * @param totalAmount 元
     */
    public void setTotalAmount(BigDecimal totalAmount) {
        setAmount(totalAmount);
        this.totalAmount = totalAmount;
    }

    /**
     * 转账金额单位为“分”。转账总金额必须与批次内所有明细转账金额之和保持一致，否则无法发起转账操作
     *
     * @param totalAmount 分
     */
    public void setTotalAmount(Integer totalAmount) {
        setTotalAmount(new BigDecimal(totalAmount / 100));
    }

    public Integer getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(Integer totalNum) {
        addAttr(WxConst.TOTAL_NUM, totalNum);
        this.totalNum = totalNum;
    }

    public String getTransferSceneId() {
        return transferSceneId;
    }

    public void setTransferSceneId(String transferSceneId) {
        addAttr(WxConst.TRANSFER_SCENE_ID, transferSceneId);
        this.transferSceneId = transferSceneId;
    }
}
