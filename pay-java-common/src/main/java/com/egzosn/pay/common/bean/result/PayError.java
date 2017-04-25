package com.egzosn.pay.common.bean.result;

/**
 * 支付错误码说明
 *
 * @author egan
 * <pre>
 *     email egzosn@gmail.com
 *     date 2017-03-02 22:28:01
 *  </pre>
 */
public interface PayError {

    /**
     * 获取错误码
     *
     * @return 错误码
     */
    String getErrorCode();

    /**
     * 获取错误消息
     *
     * @return 错误消息
     */
    String getErrorMsg();

    /**
     * 获取异常信息
     * @return 异常信息
     */
    String getString();

}
