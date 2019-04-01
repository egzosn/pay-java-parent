package com.egzosn.pay.common.bean;

import java.math.BigDecimal;

/**
 * 转账订单
 * @author egan
 * <pre>
 * email egzosn@gmail.com
 * date 2018/1/31
 * </pre>
 */
public class TransferOrder {

    /**
     * 转账订单单号
     */
    private String outNo;

    /**
     * 收款方账户, 用户openid
     */
    private String  payeeAccount ;

    /**
     * 转账金额
     */
    private BigDecimal amount ;

    /**
     * 付款人名称
     */
    private String payerName;

    /**
     * 收款人名称
     */
    private String payeeName;
    /**
     * 备注
     */
    private String remark;

    /**
     * 收款开户行
      */
    private Bank bank;

    /**
     * 币种
     */
    private CurType curType;
    /**
     * 转账类型，收款方账户类型，比如支付宝账户或者银行卡
     */
    private TransferType transferType;

    /**
     * 操作者ip，根据支付平台所需进行设置
     */
    private String ip;

    public String getOutNo() {
        return outNo;
    }

    public void setOutNo(String outNo) {
        this.outNo = outNo;
    }

    public String getPayeeAccount() {
        return payeeAccount;
    }

    public void setPayeeAccount(String payeeAccount) {
        this.payeeAccount = payeeAccount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getPayerName() {
        return payerName;
    }

    public void setPayerName(String payerName) {
        this.payerName = payerName;
    }

    public String getPayeeName() {
        return payeeName;
    }

    public void setPayeeName(String payeeName) {
        this.payeeName = payeeName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Bank getBank() {
        return bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    public CurType getCurType() {
        return curType;
    }

    public void setCurType(CurType curType) {
        this.curType = curType;
    }

    public TransferType getTransferType() {
        return transferType;
    }

    public void setTransferType(TransferType transferType) {
        this.transferType = transferType;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
