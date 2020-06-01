package com.egzosn.pay.ali.bean;

import com.egzosn.pay.common.bean.TransferOrder;
import com.egzosn.pay.common.bean.TransferType;

import java.util.Map;

/**
 * 收款方账户类型
 *
 * @author egan
 * email egzosn@gmail.com
 * date 2018/9/28.20:32
 */
public enum AliTransferType implements TransferType {
    /**
     * 单笔无密转账到支付宝账户固定为
     */
    TRANS_ACCOUNT_NO_PWD("alipay.fund.trans.uni.transfer", "DIRECT_TRANSFER"),
    /**
     * 单笔无密转账到银行卡固定为
     */
    TRANS_BANKCARD_NO_PWD("alipay.fund.trans.uni.transfer", "DIRECT_TRANSFER"),
    /**
     * 收发现金红包固定为
     */
    STD_RED_PACKET("alipay.fund.trans.uni.transfer", "DIRECT_TRANSFER"),
    /**
     * 现金红包无线支付接口
     */
    STD_RED_PACKET_APP("alipay.fund.trans.app.pay", "PERSONAL_PAY") {
        /**
         * 获取转账类型
         *
         * @return 转账类型
         */
        @Override
        public String getType() {
            return STD_RED_PACKET.name();
        }
    },

    /**
     * 转账查询
     */
    TRANS_QUERY("alipay.fund.trans.order.query");
    /**
     * 接口名称
     */
    private String method;
    /**
     * 业务场景
     */
    private String bizScene;

    AliTransferType(String method) {
        this.method = method;
    }

    AliTransferType(String method, String bizScene) {
        this.method = method;
        this.bizScene = bizScene;
    }

    /**
     * 获取转账类型, product_code 业务产品码
     *
     * @return 转账类型
     */
    @Override
    public String getType() {
        return name();
    }

    public String getBizScene() {
        return bizScene;
    }

    /**
     * 获取接口
     *
     * @return 接口
     */
    @Override
    public String getMethod() {
        return method;
    }

    /**
     * 设置属性
     *
     * @param attr 已有属性对象
     * @param order 转账订单
     * @return 属性对象
     */
    @Override
    public Map<String, Object> setAttr(Map<String, Object> attr, TransferOrder order) {
        attr.put("product_code", getType());
        attr.put("biz_scene", getBizScene());
        return attr;
    }
}
