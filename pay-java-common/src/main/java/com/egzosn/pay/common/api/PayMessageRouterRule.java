package com.egzosn.pay.common.api;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import com.egzosn.pay.common.bean.PayMessage;
import com.egzosn.pay.common.bean.PayOutMessage;
import com.egzosn.pay.common.exception.PayErrorException;


/**
 * Route规则 路由
 *
 * @author egan
 * <pre>
 *  email egzosn@gmail.com
 *  date 2016-6-1 11:28:01
 *
 *
 *  source chanjarster/weixin-java-tools
 *  </pre>
 */
public class PayMessageRouterRule {


    private final PayMessageRouter routerBuilder;
    /**
     * 默认同步
     */
    private boolean async = false;

    /**
     * 支付类型
     */
    private String payType;
    /**
     * 交易类型
     */
    private String[] transactionType;
    /**
     * 简介
     */
    private String subject;
    /**
     * 正则匹配
     */
    private String rSubject;
    /**
     * 匹配的键名称
     */
    private String key;
    /**
     * 匹配的键名称对应的值 正则
     */
    private String rValue;

    private boolean reEnter = false;

    private List<PayMessageHandler> handlers = new ArrayList<PayMessageHandler>();

    private List<PayMessageInterceptor> interceptors = new ArrayList<PayMessageInterceptor>();

    public PayMessageRouterRule(PayMessageRouter routerBuilder) {
        this.routerBuilder = routerBuilder;
    }

    /**
     * 设置是否异步执行，默认是true
     *
     * @param async 是否异步执行，默认是true
     * @return Route规则
     */
    public PayMessageRouterRule async(boolean async) {
        this.async = async;
        return this;
    }


    /**
     * 如果payType等于某值
     *
     * @param payType 支付类型
     * @return Route规则
     */
    public PayMessageRouterRule payType(String payType) {
        this.payType = payType;
        return this;
    }

    /**
     * 如果transactionType等于某值
     *
     * @param transactionType 交易类型
     * @return Route规则
     */
    public PayMessageRouterRule transactionType(String... transactionType) {
        this.transactionType = transactionType;
        return this;
    }


    /**
     * 如果subject等于某值
     *
     * @param subject 简介
     * @return Route规则
     */
    public PayMessageRouterRule subject(String subject) {
        this.subject = subject;
        return this;
    }

    /**
     * 如果subject匹配该正则表达式
     *
     * @param regex 简介正则
     * @return Route规则
     */
    public PayMessageRouterRule rSubject(String regex) {
        this.rSubject = regex;
        return this;
    }

    /**
     * 如果subject匹配该正则表达式
     *
     * @param key   需要匹配支付消息内键的名字
     * @param regex key值对应的正则
     * @return Route规则
     */
    public PayMessageRouterRule key2RValue(String key, String regex) {
        this.key = key;
        this.rValue = regex;
        return this;
    }


    /**
     * 设置消息拦截器
     *
     * @param interceptor 消息拦截器
     * @return Route规则
     */
    public PayMessageRouterRule interceptor(PayMessageInterceptor interceptor) {
        return interceptor(interceptor, (PayMessageInterceptor[]) null);
    }

    /**
     * 设置消息拦截器
     *
     * @param interceptor       消息拦截器
     * @param otherInterceptors 其他消息拦截器
     * @return Route规则
     */
    public PayMessageRouterRule interceptor(PayMessageInterceptor interceptor, PayMessageInterceptor... otherInterceptors) {
        this.interceptors.add(interceptor);
        if (otherInterceptors != null && otherInterceptors.length > 0) {
            for (PayMessageInterceptor i : otherInterceptors) {
                this.interceptors.add(i);
            }
        }
        return this;
    }

    /**
     * 设置消息处理器
     *
     * @param handler 消息处理器
     * @return Route规则
     */
    public PayMessageRouterRule handler(PayMessageHandler handler) {
        return handler(handler, (PayMessageHandler[]) null);
    }

    /**
     * 设置消息处理器
     *
     * @param handler       消息处理器
     * @param otherHandlers 其他消息处理器
     * @return Route规则
     */
    public PayMessageRouterRule handler(PayMessageHandler handler, PayMessageHandler... otherHandlers) {
        this.handlers.add(handler);
        if (otherHandlers != null && otherHandlers.length > 0) {
            for (PayMessageHandler i : otherHandlers) {
                this.handlers.add(i);
            }
        }
        return this;
    }

    /**
     * 规则结束，代表如果一个消息匹配该规则，那么它将不再会进入其他规则
     *
     * @return Route规则
     */
    public PayMessageRouter end() {
        this.routerBuilder.getRules().add(this);
        return this.routerBuilder;
    }

    /**
     * 规则结束，但是消息还会进入其他规则
     *
     * @return Route规则
     */
    public PayMessageRouter next() {
        this.reEnter = true;
        return end();
    }

    /**
     * 将支付事件修正为不区分大小写,
     * 比如框架定义的事件常量为
     *
     * @param payMessage 支付消息
     * @return 是否匹配通过
     */
    protected boolean test(PayMessage payMessage) {
        return (
                (this.payType == null || this.payType.equals((payMessage.getPayType() == null ? null : payMessage.getPayType())))
                        &&
                        (this.transactionType == null || equalsTransactionType(payMessage.getTransactionType()))
                        &&
                        (this.key == null || this.rValue == null || Pattern
                                .matches(this.rValue, payMessage.getPayMessage().get(key) == null ? "" : payMessage.getPayMessage().get(key).toString().trim()))
                        &&
                        (this.subject == null || this.subject
                                .equals(payMessage.getSubject() == null ? null : payMessage.getSubject().trim()))
                        &&
                        (this.rSubject == null || Pattern
                                .matches(this.rSubject, payMessage.getSubject() == null ? "" : payMessage.getSubject().trim()))
        )
                ;
    }

    /**
     * 匹配交易类型
     *
     * @param transactionType 交易类型
     * @return 匹配交易类型
     */
    public boolean equalsTransactionType(String transactionType) {
        if (null == transactionType) {
            return false;
        }

        for (String type : this.getTransactionType()) {
            if (type.toLowerCase().equals((transactionType.toLowerCase()))) {
                return true;
            }
        }
        return false;

    }


    /**
     * 返回支付响应消息
     *
     * @param payMessage       支付消息
     * @param payService       支付服务
     * @param exceptionHandler 异常处理器
     * @return 支付响应消息
     */
    protected PayOutMessage service(PayMessage payMessage,
                                    PayService payService,
                                    PayErrorExceptionHandler exceptionHandler) {

        try {

            Map<String, Object> context = new HashMap<String, Object>();
            // 如果拦截器不通过
            for (PayMessageInterceptor interceptor : this.interceptors) {
                if (!interceptor.intercept(payMessage, context, payService)) {
                    //这里直接返回成功，解决外层判断问题
                    return payService.successPayOutMessage(payMessage);
                }
            }

            // 交给handler处理
            PayOutMessage res = null;
            for (PayMessageHandler handler : this.handlers) {
                // 返回最后handler的结果
                res = handler.handle(payMessage, context, payService);
            }
            return res;
        }
        catch (PayErrorException e) {
            exceptionHandler.handle(e);
        }
        return null;

    }

    public PayMessageRouter getRouterBuilder() {
        return routerBuilder;
    }

    public boolean isAsync() {
        return async;
    }

    public void setAsync(boolean async) {
        this.async = async;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String[] getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String[] transactionType) {
        this.transactionType = transactionType;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getrValue() {
        return rValue;
    }

    public void setrValue(String rValue) {
        this.rValue = rValue;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getrSubject() {
        return rSubject;
    }

    public void setrSubject(String rSubject) {
        this.rSubject = rSubject;
    }

    public boolean isReEnter() {
        return reEnter;
    }

    public void setReEnter(boolean reEnter) {
        this.reEnter = reEnter;
    }

    public List<PayMessageHandler> getHandlers() {
        return handlers;
    }

    public void setHandlers(List<PayMessageHandler> handlers) {
        this.handlers = handlers;
    }

    public List<PayMessageInterceptor> getInterceptors() {
        return interceptors;
    }

    public void setInterceptors(List<PayMessageInterceptor> interceptors) {
        this.interceptors = interceptors;
    }
}
