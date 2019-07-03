package com.egzosn.pay.common.api;


import com.egzosn.pay.common.bean.PayMessage;
import com.egzosn.pay.common.exception.PayErrorException;

import java.util.Map;

/**
 * 支付消息拦截器，可以用来做验证等等，使用者想怎么用就怎么用吧，你也可以选择不用
 *
 * @author egan
 *         <pre>
 *             email egzosn@gmail.com
 *             date 2016-6-1 11:40:30
 *
 *
 *             source Daniel Qian
 *          </pre>
 */
public interface PayMessageInterceptor<M extends PayMessage, S extends PayService> {

    /**
     * 拦截微信消息
     *
     * @param payMessage 支付消息
     * @param context    上下文，如果handler或interceptor之间有信息要传递，可以用这个
     * @param payService 支付服务
     * @return true代表OK，false代表不OK
     */
    boolean intercept(M payMessage,
                      Map<String, Object> context,
                      S payService
    ) throws PayErrorException;

}
