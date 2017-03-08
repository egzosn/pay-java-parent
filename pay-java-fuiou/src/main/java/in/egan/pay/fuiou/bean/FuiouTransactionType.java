package in.egan.pay.fuiou.bean;

import in.egan.pay.common.bean.TransactionType;

/**
 * 微信交易类型
 * @author egan
 * @email egzosn@gmail.com
 * @date 2016/10/19 22:58
 */
public enum FuiouTransactionType implements TransactionType {
    B2B(""),
    B2C("")
    ;

    private String method;

    private FuiouTransactionType(String method) {
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
