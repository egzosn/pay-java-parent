package in.egan.pay.wx.bean;

import in.egan.pay.common.bean.TransactionType;

/**
 * 微信交易类型
 * @author egan
 * @email egzosn@gmail.com
 * @date 2016/10/19 22:58
 */
public enum  WxTransactionType implements TransactionType {
    //公众号支付
    JSAPI,//暂未接触
    //扫码付
    NATIVE,
    //移动支付
    APP,
    //刷卡付
    MICROPAY;//暂未接触

    @Override
    public String getType() {
        return this.name();
    }
}
