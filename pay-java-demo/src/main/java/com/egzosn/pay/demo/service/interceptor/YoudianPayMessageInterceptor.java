
package com.egzosn.pay.demo.service.interceptor;

import com.egzosn.pay.common.api.PayMessageHandler;
import com.egzosn.pay.common.api.PayMessageInterceptor;
import com.egzosn.pay.common.api.PayService;
import com.egzosn.pay.common.bean.PayMessage;
import com.egzosn.pay.common.exception.PayErrorException;
import com.egzosn.pay.wx.youdian.bean.WxYoudianPayMessage;

import java.util.Map;

/**
 * 回调信息拦截器
 * @author: egan
 * email egzosn@gmail.com
 * date 2017/1/18 19:28
 */
public class YoudianPayMessageInterceptor implements PayMessageInterceptor<WxYoudianPayMessage, PayService> {

//    @Autowired
//    private AmtApplyService amtApplyService;

    /**
     * 拦截支付消息
     *
     * @param payMessage     支付回调消息
     * @param context        上下文，如果handler或interceptor之间有信息要传递，可以用这个
     * @param payService 支付服务
     * @return true代表OK，false代表不OK并直接中断对应的支付处理器
     * @see PayMessageHandler 支付处理器
     * @throws PayErrorException PayErrorException
     */
    @Override
    public boolean intercept(WxYoudianPayMessage payMessage, Map<String, Object> context, PayService payService) throws PayErrorException {

        //这里进行拦截器处理，自行实现

        Map<String, Object> message = payMessage.getPayMessage();

        // 设置外部单号
        String outId = payMessage.getOutTradeNo();

        //根据outId获取账单
//        AmtApply amtApply = amtApplyService.getAmtApplyByApplyOutid(outId);
//        if (null == amtApply){
//            Log4jUtil.info("友店outId：" + outId);
//
//            return false;
//        }

        //重复回调不进行处理
//        if(amtApply.getApplyState().shortValue()== ApplyStateEnum.success.getCode()){
//            return false;
//        }
        //将账单存储至上下文对象中
//        context.put("amtApply", amtApply);


//        amtPaylogService.createAmtPaylogByCheckFail(amtApply, payMessage.getPayMessage());
        return true;
    }
}
