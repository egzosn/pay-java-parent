

package in.egan.pay.demo.service;

import in.egan.pay.ali.bean.AliTransactionType;
import in.egan.pay.demo.entity.ApyAccount;
import in.egan.pay.demo.entity.PayType;
import in.egan.pay.demo.service.handler.AliPayMessageHandler;
import in.egan.pay.demo.service.handler.WxPayMessageHandler;
import in.egan.pay.demo.service.handler.YouDianPayMessageHandler;
import in.egan.pay.demo.service.interceptor.AliPayMessageInterceptor;
import in.egan.pay.common.api.PayConfigStorage;
import in.egan.pay.common.api.PayMessageHandler;
import in.egan.pay.common.api.PayMessageRouter;
import in.egan.pay.common.api.PayService;
import in.egan.pay.common.bean.MsgType;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;

import javax.annotation.Resource;

/**
 * 支付响应对象
 * @author: egan
 * @email egzosn@gmail.com
 * @date 2016/11/18 0:34
 */
public class PayResponse {

    @Resource
    private AutowireCapableBeanFactory spring;

    private PayConfigStorage storage;

    private PayService service;

    private PayMessageRouter router;

    public PayResponse() {

    }

    /**
     * 初始化支付配置
     * @param apyAccount 账户信息
     * @see ApyAccount 对应表结构详情--》 /pay-java-demo/resources/apy_account.sql
     */
    public void init(ApyAccount apyAccount) {
        //根据不同的账户类型 初始化支付配置
        this.service = apyAccount.getPayType().getPayService(apyAccount);
        this.storage = service.getPayConfigStorage();

        buildRouter(apyAccount.getPayId());
    }



    /**
     * 配置路由
     * @param payId 指定账户id，用户多微信支付多支付宝支付
     */
    private void buildRouter(Integer payId) {
        router = new PayMessageRouter(this.service);
        router
                .rule()
                .async(false)
                .msgType(MsgType.text.name()) //消息类型
                .payType(PayType.aliPay.name()) //支付账户事件类型
                .transactionType(AliTransactionType.UNAWARE.name())//交易类型，有关回调的可在这处理
                .interceptor(new AliPayMessageInterceptor(payId)) //拦截器
                .handler(autowire(new AliPayMessageHandler(payId))) //处理器
                .end()
                .rule()
                .async(false)
                .msgType(MsgType.xml.name())
                .payType(PayType.wxPay.name())
                .handler(autowire(new WxPayMessageHandler(payId)))
                .end()
                .rule()
                .async(false)
                .msgType(MsgType.json.name())
                .payType(PayType.youdianPay.name())
                .handler(autowire(new YouDianPayMessageHandler(payId)))
                .end()

        ;
    }


    private PayMessageHandler autowire(PayMessageHandler handler) {
        spring.autowireBean(handler);
        return handler;
    }

    public PayConfigStorage getStorage() {
        return storage;
    }

    public PayService getService() {
        return service;
    }

    public PayMessageRouter getRouter() {
        return router;
    }
}
