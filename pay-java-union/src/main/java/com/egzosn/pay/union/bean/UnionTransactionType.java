package com.egzosn.pay.union.bean;

import com.egzosn.pay.common.bean.TransactionType;
import com.egzosn.pay.common.util.str.StringUtils;

import java.util.Map;

/**
 * @author Actinia
 * @email hayesfu@qq.com
 *  <pre>
    create 2017 2017/11/4 0004
 * </pre>
 */
public enum UnionTransactionType implements TransactionType{

    /**
     * 苹果支付
     */
    APPLE("01","01","000802","08"),
    /**
     * 手机控件
     */
    APP("01","01","000000","08"),
    /**
     * 手机网页支付（WAP支付）
     */
    WAP("01","01","000201","08"),
    /**
     * 网关支付
     */
    WEB("01","01","000201","07"),
    /**
     * 无跳转支付
     */
    NO_JUMP("01","01","000301","07"),
    /**
     * 企业网银支付（B2B支付）
     */
    B2B("01","01","000202","07"),
    /**
     *  申码(主扫场景)
     */
    APPLY_QR_CODE("01","07","000000","08"),
    /**
     * 消费(被扫场景)
     */
    CONSUME("01","06","000000","08"),
    //消费撤销
    CONSUME_UNDO("31","00","000000","08"),
    //退款
    REFUND("04","00","000000","08"),
    //查询
    QUERY("00","00","000201",""),
    //对账文件下载
    FILE_TRANSFER("00","00","000201","")
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
     * 渠道类型 05：语音07：互联网08：移动 16：数字机顶盒
     */
    private String channelType;


    UnionTransactionType (String txnType, String txnSubType, String bizType, String channelType) {
        this.txnType = txnType;
        this.txnSubType = txnSubType;
        this.bizType = bizType;
        this.channelType = channelType;
    }

    public void convertMap(Map<String ,Object> contentData){
        //交易类型
        contentData.put(SDKConstants.param_txnType, this.getTxnType());
        //交易子类
        contentData.put(SDKConstants.param_txnSubType,this.getTxnSubType());
        //业务类型
        contentData.put(SDKConstants.param_bizType,this.getBizType());
        //渠道类型
        if(StringUtils.isNotBlank(this.getChannelType())){
            contentData.put(SDKConstants.param_channelType,this.getChannelType());
        }
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
