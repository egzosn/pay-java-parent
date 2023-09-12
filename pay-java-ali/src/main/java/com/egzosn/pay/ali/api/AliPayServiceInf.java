package com.egzosn.pay.ali.api;

import com.egzosn.pay.common.bean.RefundOrder;

/**
 * 支付宝定制化服务接口
 * @author Egan
 * <pre>
 * email egan@egzosn.com
 * date 2021/12/4
 * </pre>
 */
public interface AliPayServiceInf {

    /**
     * 收单退款冲退完成通知
     * 退款存在退到银行卡场景下时，收单会根据银行回执消息发送退款完成信息
     * @param refundOrder 退款订单
     * @return fail    消息获取失败	是  success	消息获取成功	否
     */
    String refundDepositBackCompleted(RefundOrder refundOrder);


    /**
     * 设置api服务器地址
     *
     * @param apiServerUrl api服务器地址
     * @return 自身
     */
    AliPayServiceInf setApiServerUrl(String apiServerUrl);
}
