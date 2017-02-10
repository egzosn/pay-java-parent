
package in.egan.pay.demo.service.interceptor;

import in.egan.pay.common.api.PayMessageHandler;
import in.egan.pay.common.api.PayMessageInterceptor;
import in.egan.pay.common.api.PayService;
import in.egan.pay.common.bean.PayMessage;
import in.egan.pay.common.exception.PayErrorException;

import java.util.Map;

/**
 * 支付宝回调信息拦截器
 * @author: egan
 * @email egzosn@gmail.com
 * @date 2017/1/18 19:28
 */
public class AliPayMessageInterceptor implements PayMessageInterceptor {

    //支付账户id
    private Integer payId;
    public AliPayMessageInterceptor(Integer payId) {

        this.payId = payId;
    }

    /**
     * 拦截支付消息
     *
     * @param payMessage     支付回调消息
     * @param context        上下文，如果handler或interceptor之间有信息要传递，可以用这个
     * @param payService
     * @return true代表OK，false代表不OK并直接中断对应的支付处理器
     * @see PayMessageHandler 支付处理器
     */
    @Override
    public boolean intercept(PayMessage payMessage, Map<String, Object> context, PayService payService) throws PayErrorException {

        //这里进行拦截器处理，自行实现
        return true;
    }
}
