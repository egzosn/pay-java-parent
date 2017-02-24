package in.egan.pay.ali.bean;

import in.egan.pay.common.bean.TransactionType;

/**
 * 阿里交易类型
 * @author egan
 * @email egzosn@gmail.com
 * @date 2016/10/19 22:58
 */
public enum  AliTransactionType implements TransactionType {
     //即时到帐                                   //移动支付                      //手机网站支付
     DIRECT("create_direct_pay_by_user"),APP("alipay.trade.app.pay"),WAP("alipay.trade.wap.pay")
    // TODO 2017/2/23 20:26 author: egan 以下三个为主动交易类型 暂未测试，
    //扫码付                                   //条码付                        // 声波付
    ,SWEEPPAY("alipay.trade.precreate"),BAR_CODE("alipay.trade.pay"),WAVE_CODE("alipay.trade.pay"),;

    private String type;

    private AliTransactionType(String type) {
        this.type = type;
    }

    @Override
    public String getType() {
        return type;
    }
}
