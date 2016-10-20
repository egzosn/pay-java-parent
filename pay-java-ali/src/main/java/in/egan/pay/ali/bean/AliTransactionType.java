package in.egan.pay.ali.bean;

import in.egan.pay.common.bean.TransactionType;

/**
 * @author egan
 * @email egzosn@gmail.com
 * @date 2016/10/19 22:58
 */
public enum  AliTransactionType implements TransactionType {

     NATIVE("create_direct_pay_by_user"),APP("mobile.securitypay.pay");

    private String type;

    private AliTransactionType(String type) {
        this.type = type;
    }

    @Override
    public String getType() {
        return type;
    }
}
