package in.egan.pay.ali.bean;

import in.egan.pay.common.bean.TransactionType;

/**
 * 阿里交易类型
 * @author egan
 * @email egzosn@gmail.com
 * @date 2016/10/19 22:58
 */
public enum  AliTransactionType implements TransactionType {
    //即时到帐                          //移动支付                      //手机网站支付
    DIRECT("create_direct_pay_by_user"),APP("mobile.securitypay.pay"),WAP("alipay.wap.create.direct.pay.by.user");

    private String type;

    private AliTransactionType(String type) {
        this.type = type;
    }

    @Override
    public String getType() {
        return type;
    }
}
