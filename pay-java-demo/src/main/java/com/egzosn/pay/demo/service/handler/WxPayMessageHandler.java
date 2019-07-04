package com.egzosn.pay.demo.service.handler;

import com.egzosn.pay.common.api.PayService;
import com.egzosn.pay.common.bean.PayOutMessage;
import com.egzosn.pay.common.exception.PayErrorException;
import com.egzosn.pay.wx.bean.WxPayMessage;

import java.util.Map;

/**
 * 微信支付回调处理器
 * Created by ZaoSheng on 2016/6/1.
 */
public class WxPayMessageHandler extends BasePayMessageHandler<WxPayMessage, PayService> {




    public WxPayMessageHandler(Integer payId) {
        super(payId);
    }

    @Override
    public PayOutMessage handle(WxPayMessage payMessage, Map<String, Object> context, PayService payService) throws PayErrorException {
        //交易状态
        if ("SUCCESS".equals(payMessage.getPayMessage().get("result_code"))){
            /////这里进行成功的处理

            return  payService.getPayOutMessage("SUCCESS", "OK");
        }

        return  payService.getPayOutMessage("FAIL", "失败");
    }
}
