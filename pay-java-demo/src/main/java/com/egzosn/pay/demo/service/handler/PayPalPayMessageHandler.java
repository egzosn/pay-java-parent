package com.egzosn.pay.demo.service.handler;

import com.egzosn.pay.common.api.PayMessageHandler;
import com.egzosn.pay.common.api.PayService;
import com.egzosn.pay.common.bean.PayMessage;
import com.egzosn.pay.common.bean.PayOutMessage;
import com.egzosn.pay.common.exception.PayErrorException;
import com.egzosn.pay.paypal.api.PayPalPayService;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;

/**
 * PayPal支付回调处理器
 * Created by ZaoSheng on 2016/6/1.
 *
 */
@Component
public class PayPalPayMessageHandler implements PayMessageHandler<PayMessage, PayPalPayService> {





    @Override
    public PayOutMessage handle(PayMessage payMessage, Map<String, Object> context, PayPalPayService payService) throws PayErrorException {


        return payService.getPayOutMessage("fail", "失败");
    }
}
