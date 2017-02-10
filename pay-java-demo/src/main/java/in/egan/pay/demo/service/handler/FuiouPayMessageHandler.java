package in.egan.pay.demo.service.handler;

import in.egan.pay.common.api.PayService;
import in.egan.pay.common.bean.PayMessage;
import in.egan.pay.common.bean.PayOutMessage;
import in.egan.pay.common.exception.PayErrorException;

import java.util.Map;

/**
 * @author Fuzx
 * @create 2017 2017/1/24 0024
 */
public class FuiouPayMessageHandler extends BasePayMessageHandler {




    public FuiouPayMessageHandler(Integer payId) {
        super(payId);
    }

    @Override
    public PayOutMessage handle(PayMessage payMessage, Map<String, Object> context, PayService payService) throws PayErrorException {
        //交易状态
        if ("0000".equals(payMessage.getPayMessage().get("order_pay_code"))){
            /////这里进行成功的处理

            return PayOutMessage.JSON().content("order_pay_error","成功").build();
        }

        return PayOutMessage.JSON().content("order_pay_error",payMessage.getPayMessage().get("order_pay_error")).build();
    }
}
