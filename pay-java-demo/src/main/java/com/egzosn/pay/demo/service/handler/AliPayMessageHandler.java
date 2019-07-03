package com.egzosn.pay.demo.service.handler;

import com.egzosn.pay.ali.api.AliPayService;
import com.egzosn.pay.ali.bean.AliPayMessage;
import com.egzosn.pay.common.api.PayMessageHandler;
import com.egzosn.pay.common.api.PayService;
import com.egzosn.pay.common.bean.PayMessage;
import com.egzosn.pay.common.bean.PayOutMessage;
import com.egzosn.pay.common.bean.outbuilder.TextBuilder;
import com.egzosn.pay.common.exception.PayErrorException;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 支付宝支付回调处理器
 * Created by ZaoSheng on 2016/6/1.
 *
 */
@Component
public class AliPayMessageHandler implements PayMessageHandler<AliPayMessage, AliPayService> {


    /**
     * 处理支付回调消息的处理器接口
     *
     * @param payMessage 支付消息
     * @param context    上下文，如果handler或interceptor之间有信息要传递，可以用这个
     * @param payService 支付服务
     * @return xml, text格式的消息，如果在异步规则里处理的话，可以返回null
     * @throws PayErrorException 支付错误异常
     */
    @Override
    public PayOutMessage handle(AliPayMessage payMessage, Map<String, Object> context, AliPayService payService) throws PayErrorException {
        //com.egzosn.pay.demo.entity.PayType.getPayService()#48
        Object payId = payService.getPayConfigStorage().getAttach();

        Map<String, Object> message = payMessage.getPayMessage();
        //交易状态
        String trade_status = (String) message.get("trade_status");

        //上下文对象中获取账单
//        AmtApply amtApply = (AmtApply)context.get("amtApply");
        //日志存储
//        amtPaylogService.createAmtPaylogByCallBack(amtApply,  message.toString());
        //交易完成
        if ("TRADE_SUCCESS".equals(trade_status) || "TRADE_FINISHED".equals(trade_status)) {

            BigDecimal payAmount = new BigDecimal((String) message.get("total_fee"));

            return payService.getPayOutMessage("success", "成功");

        }/* else if ("WAIT_BUYER_PAY".equals(trade_status) || "TRADE_CLOSED".equals(trade_status)) {

        }*/

        return payService.getPayOutMessage("fail", "失败");
    }
}
