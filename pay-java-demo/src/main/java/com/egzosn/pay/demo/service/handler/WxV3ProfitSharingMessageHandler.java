package com.egzosn.pay.demo.service.handler;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.egzosn.pay.common.api.DefaultPayMessageHandler;
import com.egzosn.pay.common.api.PayMessageHandler;
import com.egzosn.pay.common.api.PayService;
import com.egzosn.pay.common.bean.PayOutMessage;
import com.egzosn.pay.common.exception.PayErrorException;
import com.egzosn.pay.wx.v3.bean.sharing.ProfitSharingPayMessage;

/**
 * 微信合单支付回调处理器
 * Created by ZaoSheng on 2016/6/1.
 */
public class WxV3ProfitSharingMessageHandler implements PayMessageHandler<ProfitSharingPayMessage, PayService> {

    private final Logger LOG = LoggerFactory.getLogger(DefaultPayMessageHandler.class);

    @Override
    public PayOutMessage handle(ProfitSharingPayMessage payMessage, Map<String, Object> context, PayService payService) throws PayErrorException {
        LOG.info("回调支付消息处理器，回调消息：{}", JSON.toJSONString(payMessage));
        return payService.successPayOutMessage(payMessage);
    }
}
