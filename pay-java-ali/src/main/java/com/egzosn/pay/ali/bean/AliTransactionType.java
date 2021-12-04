package com.egzosn.pay.ali.bean;

import com.egzosn.pay.common.bean.TransactionType;

/**
 * 阿里交易类型
 * <pre>
 * 说明交易类型主要用于支付接口调用参数所需
 * {@link #APP 新版app支付}
 *
 *
 *
 * </pre>
 *
 * @author egan
 *
 * email egzosn@gmail.com
 * date 2016/10/19 22:58
 */
public enum  AliTransactionType implements TransactionType {

    /**
     * 网页支付
     */
    PAGE("alipay.trade.page.pay"),
    /**
     * APP支付
     */
    APP("alipay.trade.app.pay"),
    /**
     * 手机网站支付
     */
    WAP("alipay.trade.wap.pay"),

    /**
     *  扫码付
     */
    SWEEPPAY("alipay.trade.precreate"),
    /**
     * 条码付
     */
    BAR_CODE("alipay.trade.pay"),
    /**
     * 声波付
     */
    WAVE_CODE("alipay.trade.pay"),
    /**
     * 小程序
     */
    MINAPP("alipay.trade.create"),
    /**
     * 刷脸付
     */
    SECURITY_CODE("alipay.trade.pay"),
    /**
     * 人脸初始化刷脸付
     * 暂时未接入
     *
     */
    SMILEPAY("zoloz.authentication.customer.smilepay.initialize"),
    //交易辅助接口

    /**
     *  统一收单交易结算接口
     */
    SETTLE("alipay.trade.order.settle"),
    /**
     * 交易订单查询
     */
    QUERY("alipay.trade.query"),
    /**
     * 交易订单关闭
     */
    CLOSE("alipay.trade.close"),
    /**
     * 交易订单撤销
     */
    CANCEL("alipay.trade.cancel"),
    /**
     * 退款
     */
    REFUND("alipay.trade.refund"),
    /**
     * 退款查询
     */
    REFUNDQUERY("alipay.trade.fastpay.refund.query"),
    /**
     * 收单退款冲退完成通知
     * 退款存在退到银行卡场景下时，收单会根据银行回执消息发送退款完成信息
     */
    REFUND_DEPOSITBACK_COMPLETED ("alipay.trade.refund.depositback.completed"),
    /**
     * 下载对账单
     */
    DOWNLOADBILL("alipay.data.dataservice.bill.downloadurl.query"),
    /**
     * 查询刷脸结果信息
     * 暂时未接入
     */
    FTOKEN_QUERY("zoloz.authentication.customer.ftoken.query")
    ;



    private String method;

    AliTransactionType(String method) {
        this.method = method;
    }

    @Override
    public String getType() {
        return this.name();
    }

    /**
     * 获取接口名称
     * @return 接口名称
     */
    @Override
    public String getMethod() {
        return this.method;
    }

}
