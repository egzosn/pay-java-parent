package in.egan.pay.common.api;

import in.egan.pay.common.bean.PayMessage;
import in.egan.pay.common.bean.PayOutMessage;
import in.egan.pay.common.exception.PayErrorException;

import java.util.Map;


/**
 * 处理支付回调消息的处理器接口
 * @source Daniel Qian
 * @author egan
 * @email egzosn@gmail.com
 * @date 2016-6-1 11:40:30
 */
public interface PayMessageHandler {

    /**
     * @param payMessage
     * @param context        上下文，如果handler或interceptor之间有信息要传递，可以用这个
     * @param payService
     * @return xml,text格式的消息，如果在异步规则里处理的话，可以返回null
     */
    public PayOutMessage handle(PayMessage payMessage,
                                Map<String, Object> context,
                                PayService payService
    ) throws PayErrorException;

}
