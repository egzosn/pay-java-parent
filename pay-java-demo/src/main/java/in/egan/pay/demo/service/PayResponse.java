/*
 * Copyright 2002-2017 the original huodull or egan.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package in.egan.pay.demo.service;
import in.egan.pay.common.api.*;
import in.egan.pay.common.bean.MsgType;
import in.egan.pay.demo.entity.ApyAccount;
import in.egan.pay.demo.service.handler.AliPayMessageHandler;
import in.egan.pay.demo.service.handler.WxPayMessageHandler;
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


   /* *//**
     * 根据不同的账户类型 初始化支付配置
     * 一个账户类型可支持多个账户
     * @param apyAccount 账户信息
     * @describe 还需要优化
     *//*
    public PayService getPayService(ApyAccount apyAccount){

        switch (apyAccount.getPayType()){
            case 0:
                AliPayConfigStorage aliPayConfigStorage = new AliPayConfigStorage();
                aliPayConfigStorage.setPartner(apyAccount.getPartner());
                aliPayConfigStorage.setAli_public_key(apyAccount.getPublicKey());
                aliPayConfigStorage.setKeyPrivate(apyAccount.getPrivateKey());
                aliPayConfigStorage.setNotifyUrl(apyAccount.getNotifyUrl());
                aliPayConfigStorage.setSignType(apyAccount.getSignType());
                aliPayConfigStorage.setSeller(apyAccount.getSeller());
                aliPayConfigStorage.setPayType(apyAccount.getPayType());
                aliPayConfigStorage.setMsgType(apyAccount.getMsgType());
                return new AliPayService(aliPayConfigStorage);
            case 1:
                WxPayConfigStorage wxPayConfigStorage = new WxPayConfigStorage();
                wxPayConfigStorage.setMchId(apyAccount.getPartner());
                wxPayConfigStorage.setAppSecret(apyAccount.getPublicKey());
                wxPayConfigStorage.setAppid(apyAccount.getAppid());
                wxPayConfigStorage.setKeyPrivate(apyAccount.getPrivateKey());
                wxPayConfigStorage.setNotifyUrl(apyAccount.getNotifyUrl());
                wxPayConfigStorage.setSignType(apyAccount.getSignType());
                wxPayConfigStorage.setPayType(apyAccount.getPayType());
                wxPayConfigStorage.setMsgType(apyAccount.getMsgType());
                return  new WxPayService(wxPayConfigStorage);
            default:

        }
        return null;
    }*/


    /**
     * 配置路由
     * @param payId 指定账户id，用户多微信支付多支付宝支付
     */
    private void buildRouter(Integer payId) {
        router = new PayMessageRouter(this.service);
        router
                .rule()
                .async(false)
                .msgType(MsgType.text.name())
                .event("aliPay")
                .handler(autowire(new AliPayMessageHandler(payId)))
                .end()
                .rule()
                .async(false)
                .msgType(MsgType.xml.name())
                .event("wxPay")
                .handler(autowire(new WxPayMessageHandler(payId)))
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
