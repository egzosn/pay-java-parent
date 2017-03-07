package in.egan.pay.common.before.api;


import in.egan.pay.common.bean.PayMessage;
import in.egan.pay.common.exception.PayErrorException;

import java.util.Map;

/**
 * 支付消息拦截器，可以用来做验证
 * @author Daniel Qian
 */
@Deprecated
public interface PayMessageInterceptor {

    /**
     * 拦截支付消息
     *
     * @param wxMessage
     * @param context        上下文，如果handler或interceptor之间有信息要传递，可以用这个
     * @param payService
     * @return true代表OK，false代表不OK
     */
    public boolean intercept(PayMessage wxMessage,
                             Map<String, Object> context,
                             PayService payService
    ) throws PayErrorException;

}
