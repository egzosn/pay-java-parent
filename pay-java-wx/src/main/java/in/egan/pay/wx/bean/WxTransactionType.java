package in.egan.pay.wx.bean;

import in.egan.pay.common.bean.TransactionType;

/**
 * 阿里交易类型
 * @author egan
 * @email egzosn@gmail.com
 * @date 2016/10/19 22:58
 */
public enum  WxTransactionType implements TransactionType {
    JSAPI, NATIVE,APP,MICROPAY;

    @Override
    public String getType() {
        return this.name();
    }
}
