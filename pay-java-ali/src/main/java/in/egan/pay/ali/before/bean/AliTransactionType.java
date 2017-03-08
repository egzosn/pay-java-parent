package in.egan.pay.ali.before.bean;

import in.egan.pay.common.bean.TransactionType;

/**
 * 阿里交易类型
 * <pre>
 * 说明交易类型主要用于支付接口调用参数所需
 * {@link #APP 新版app支付}
 * {@link #UNAWARE  不知道交易类型，主要用于回调通知，回调后不清楚交易类型，以此定义}
 *
 *
 *
 * </pre>
 *
 * @author egan
 * @email egzosn@gmail.com
 * @date 2016/10/19 22:58
 * @see in.egan.pay.ali.bean.AliTransactionType
 */
@Deprecated
public enum  AliTransactionType implements TransactionType {
    //即时到帐                                //移动支付                      //手机网站支付
    DIRECT("create_direct_pay_by_user"),APP("mobile.securitypay.pay"),WAP("alipay.wap.create.direct.pay.by.user"),
    //交易辅助接口
    QUERY("alipay.trade.query"),CLOSE("alipay.trade.close"),REFUND("alipay.trade.refund"),REFUNDQUERY("alipay.trade.fastpay.refund.query"),DOWNLOADBILL("alipay.data.dataservice.bill.downloadurl.query")

    //不知道交易类型，主要用于回调通知，回调后不清楚交易类型，以此定义
    ,UNAWARE("UNAWARE")
    ;

    private String method;

    private AliTransactionType(String method) {
        this.method = method;
    }

    @Override
    public String getType() {
        return this.name();
    }

    /**
     * 获取接口名称
     * @return
     */
    @Override
    public String getMethod() {
        return this.method;
    }
}
