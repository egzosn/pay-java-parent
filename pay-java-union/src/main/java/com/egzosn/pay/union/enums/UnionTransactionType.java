package com.egzosn.pay.union.enums;

import com.egzosn.pay.common.bean.TransactionType;
import com.egzosn.pay.union.SDK.SDKConstants;

import java.util.Map;

/**
 * @author Actinia
 * @email hayesfu@qq.com
 *  <pre>
    create 2017 2017/11/4 0004
 * </pre>
 */
public enum UnionTransactionType implements TransactionType{
    //申码(主扫场景)
    APPLY_QR_CODE("01","07","000000","08"),
    //消费(被扫场景)
    CONSUME("01","06","000000","08"),
    //消费撤销
    CONSUME_UNDO("31","00","000000","08"),
    //退款
    REFUND("04","00","000000","08"),
    //查询
    QUERY("00","00","000201",""),
    //对账文件下载
    File_Transfer("00","00","000201","")
    ;

    /**
     * 交易类型
     */
    private String txnType;
    /**
     * 交易子类
     */
    private String txnSubType;

    /**
     * 业务类型
     */
    private String bizType;

    /**
     * 渠道类型
     */
    private String channelType;


    UnionTransactionType (String txnType, String txnSubType, String bizType, String channelType) {
        this.txnType = txnType;
        this.txnSubType = txnSubType;
        this.bizType = bizType;
        this.channelType = channelType;
    }

    public void convertMap(Map<String ,String> contentData){
        contentData.put(SDKConstants.param_txnType, this.getTxnType());
        contentData.put(SDKConstants.param_txnSubType,this.getTxnSubType());
        contentData.put(SDKConstants.param_bizType,this.getBizType());
        contentData.put(SDKConstants.param_channelType,this.getChannelType());
    }


    public String getTxnType () {
        return txnType;
    }

    public String getTxnSubType () {
        return txnSubType;
    }

    public String getBizType () {
        return bizType;
    }

    public String getChannelType () {
        return channelType;
    }

    /**
     *获取交易对类型枚举
     *
     * @return 交易类型
     */
    @Override
    public String getType () {
        return this.name();
    }

    /**
     *
     */
    @Override
    public String getMethod () {
        return null;
    }
}
