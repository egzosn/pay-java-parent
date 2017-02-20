package in.egan.pay.fuiou.bean;

import in.egan.pay.common.bean.TransactionType;

/**
 * 微信交易类型
 * @author egan
 * @email egzosn@gmail.com
 * @date 2016/10/19 22:58
 */
public enum FuiouTransactionType implements TransactionType {
    B2B,
    B2C
    ;

    @Override
    public String getType() {
        return this.name();
    }
}
