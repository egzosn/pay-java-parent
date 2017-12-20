package com.egzosn.pay.demo.service.handler;

import com.egzosn.pay.common.api.PayService;
import com.egzosn.pay.common.bean.PayMessage;
import com.egzosn.pay.common.bean.PayOutMessage;
import com.egzosn.pay.common.exception.PayErrorException;
import com.egzosn.pay.union.bean.SDKConstants;

import java.util.Map;

/**
 * @author Actinia
 * @email hayesfu@qq.com
 * <pre>
 * create 2017 2017/11/4 0004
 * </pre>
 */
public class UnionPayMessageHandler extends BasePayMessageHandler {


    public UnionPayMessageHandler(Integer payId) {
        super(payId);
    }

    @Override
    public PayOutMessage handle(PayMessage payMessage, Map<String, Object> context, PayService payService) throws PayErrorException {
        //交易状态
        if (SDKConstants.OK_RESP_CODE.equals(payMessage.getPayMessage().get(SDKConstants.param_respCode))) {
            /////这里进行成功的处理

            return payService.successPayOutMessage(payMessage);
        }

        return payService.getPayOutMessage("fail", "失败");
    }
}
