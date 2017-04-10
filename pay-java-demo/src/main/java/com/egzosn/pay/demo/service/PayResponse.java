

package com.egzosn.pay.demo.service;

import com.egzosn.pay.ali.bean.AliTransactionType;
import com.egzosn.pay.common.http.HttpConfigStorage;
import com.egzosn.pay.demo.service.handler.YouDianPayMessageHandler;
import com.egzosn.pay.demo.service.interceptor.AliPayMessageInterceptor;
import com.egzosn.pay.demo.entity.ApyAccount;
import com.egzosn.pay.demo.entity.PayType;
import com.egzosn.pay.demo.service.handler.AliPayMessageHandler;
import com.egzosn.pay.demo.service.handler.WxPayMessageHandler;
import com.egzosn.pay.common.api.PayConfigStorage;
import com.egzosn.pay.common.api.PayMessageHandler;
import com.egzosn.pay.common.api.PayMessageRouter;
import com.egzosn.pay.common.api.PayService;
import com.egzosn.pay.common.bean.MsgType;
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
        //这里设置代理配置
//        service.setRequestTemplateConfigStorage(getHttpConfigStorage());
        buildRouter(apyAccount.getPayId());
    }

    /**
     * 获取http配置，如果配置为null则为默认配置，无代理。
     * 此处非必需
     * @return
     */
    public HttpConfigStorage getHttpConfigStorage(){
        HttpConfigStorage httpConfigStorage = new HttpConfigStorage();
        //http代理地址
        httpConfigStorage.setHttpProxyHost("192.168.1.69");
        //代理端口
        httpConfigStorage.setHttpProxyPort(3308);
        //代理用户名
        httpConfigStorage.setHttpProxyUsername("user");
        //代理密码
        httpConfigStorage.setHttpProxyPassword("password");
        return httpConfigStorage;
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
