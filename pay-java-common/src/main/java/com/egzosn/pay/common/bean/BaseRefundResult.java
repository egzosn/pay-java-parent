package com.egzosn.pay.common.bean;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 基础的退款结果对象
 *
 * @author Egan
 * <pre>
 * email egzosn@gmail.com
 * date 2020/8/16 19:29
 * </pre>
 */
public abstract class BaseRefundResult implements RefundResult {

    /**
     * 属性集，支付宝退款结果
     */
    private Map<String, Object> attrs;


    public BaseRefundResult() {
    }

    public BaseRefundResult(Map<String, Object> attrs) {
        this.attrs = attrs;

    }

    /**
     * 获取退款结果原信息集
     *
     * @return 属性
     */
    @Override
    public Map<String, Object> getAttrs() {
        return attrs;
    }

    public void setAttrs(Map<String, Object> attrs) {
        this.attrs = attrs;
    }

    /**
     * 获取退款结果属性值
     *
     * @param key 属性名
     * @return 属性
     */
    @Override
    public Object getAttr(String key) {
        return attrs.get(key);
    }

    /**
     * 获取退款结果属性值
     *
     * @param key 属性名
     * @return 属性值
     */
    @Override
    public String getAttrString(String key) {
        return attrs.get(key).toString();
    }

    /**
     * 获取退款结果属性值
     *
     * @param key 属性名
     * @return 属性值
     */
    @Override
    public BigDecimal getAttrDecimal(String key) {
        return new BigDecimal(getAttrString(key));
    }

}
