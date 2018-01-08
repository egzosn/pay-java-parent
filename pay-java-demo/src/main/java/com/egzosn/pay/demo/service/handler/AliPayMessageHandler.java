package com.egzosn.pay.demo.service.handler;

import com.egzosn.pay.common.api.PayService;
import com.egzosn.pay.common.bean.PayMessage;
import com.egzosn.pay.common.bean.PayOutMessage;
import com.egzosn.pay.common.bean.outbuilder.TextBuilder;
import com.egzosn.pay.common.exception.PayErrorException;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 支付宝支付回调处理器
 * Created by ZaoSheng on 2016/6/1.
 *
 */
public class AliPayMessageHandler extends BasePayMessageHandler {


    public AliPayMessageHandler(Integer payId) {
        super(payId);
    }


    @Override
    public PayOutMessage handle(PayMessage payMessage, Map<String, Object> context, PayService payService) throws PayErrorException {

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
