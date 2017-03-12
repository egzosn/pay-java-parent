package in.egan.pay.demo.service.handler;

import in.egan.pay.common.api.PayService;
import in.egan.pay.common.bean.PayMessage;
import in.egan.pay.common.bean.PayOutMessage;
import in.egan.pay.common.exception.PayErrorException;

import java.io.UnsupportedEncodingException;
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
        //交易状态
       String  trade_status =payMessage.getPayMessage().get("trade_status");

        if(trade_status.equals("TRADE_FINISHED")){

            //判断该笔订单是否在商户网站中已经做过处理
            //如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
            //如果有做过处理，不执行商户的业务程序
            //注意：
            //退款日期超过可退款期限后（如三个月可退款），支付宝系统发送该交易状态通知
        } else if (trade_status.equals("TRADE_SUCCESS")){

            //判断该笔订单是否在商户网站中已经做过处理
            //如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
            //如果有做过处理，不执行商户的业务程序
            //注意：
            //付款完成后，支付宝系统发送该交易状态通知


        }else if (trade_status.equals("TRADE_SUCCESS")){
            // 	交易创建
        }

        return payService.getPayOutMessage("success", "成功");
    }
}
