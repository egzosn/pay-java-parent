package com.egzosn.pay.demo.controller;


import com.egzosn.pay.common.api.PayService;
import com.egzosn.pay.common.bean.*;
import com.egzosn.pay.common.http.HttpConfigStorage;
import com.egzosn.pay.paypal.api.PayPalConfigStorage;
import com.egzosn.pay.paypal.api.PayPalPayService;
import com.egzosn.pay.paypal.bean.PayPalTransactionType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

/**
 * 发起支付入口
 *
 * @author: egan
 * email egzosn@gmail.com
 * date 2018/05/06 10:30
 */
@RestController
@RequestMapping("payPal")
public class PayPalPayController {


    private PayService service = null;

    @PostConstruct
    public void init() {
        PayPalConfigStorage storage = new PayPalConfigStorage();
        storage.setClientID("AZ7HTcvrEAxYbzYx_iDZAi06GdqbjhqqQzFgPBFLxm2VUMzwlmiNUBk_y_5QNP4zWKblTuM6ZBAmxScd");
        storage.setClientSecret("EBMIjAag6NiRdXZxteTv0amEsmKN345xJv3bN7f_HRXSqcRJlW7PXhYXjI9sk5I4nKYOHgeqzhXCXKFo");
        storage.setTest(true);
        //发起付款后的页面转跳地址
        storage.setReturnUrl("http://www.egzosn.com/payPal/payBack.json");
        //取消按钮转跳地址,这里用异步通知地址的兼容的做法
        storage.setNotifyUrl("http://www.egzosn.com/pay/cancel");
        service = new PayPalPayService(storage);

        //请求连接池配置
        HttpConfigStorage httpConfigStorage = new HttpConfigStorage();
        //最大连接数
        httpConfigStorage.setMaxTotal(20);
        //默认的每个路由的最大连接数
        httpConfigStorage.setDefaultMaxPerRoute(10);
        service.setRequestTemplateConfigStorage(httpConfigStorage);
    }


    /**
     * 跳到支付页面
     * 针对实时支付,即时付款
     *
     * @param price 金额
     * @return 跳到支付页面
     */
    @RequestMapping(value = "toPay.html", produces = "text/html;charset=UTF-8")
    public String toPay(BigDecimal price) {
        //及时收款
        PayOrder order = new PayOrder("订单title", "摘要", null == price ? new BigDecimal(0.01) : price, UUID.randomUUID().toString().replace("-", ""), PayPalTransactionType.sale);

//        Map orderInfo = service.orderInfo(order);
//        return service.buildRequest(orderInfo, MethodType.POST);

        String toPayHtml = service.toPay(order);

        //某些支付下单时无法设置单号，通过下单后返回对应单号，如 paypal，友店。
        String outTradeNo = order.getOutTradeNo();
        System.out.println("支付订单号：" + outTradeNo + "  这里可以进行回存");

        return toPayHtml;
    }
    /**
     * 申请退款接口
     *
     * @return 返回支付方申请退款后的结果
     */
    @RequestMapping("refund")
    public Map<String, Object> refund() {
        // TODO 这里需要  refundAmount， curType， description， tradeNo
        RefundOrder order = new RefundOrder();
        order.setCurType(DefaultCurType.USD);
        order.setDescription(" description ");
        order.setTradeNo("paypal 平台的单号");
        order.setRefundAmount(new BigDecimal(0.01));
        return service.refund(order);
    }


    /**
     * return url
     * PayPal确认付款调用的接口
     * 用户确认付款后，paypal调用的这个方法执行付款
     * @param request 请求
     * @return 付款成功信息
     * @throws IOException IOException
     */
    @GetMapping(value = "payBackBefore.json")
    public String payBackBefore(HttpServletRequest request) throws IOException {
        try (InputStream is = request.getInputStream()) {
            if (service.verify(service.getParameter2Map(request.getParameterMap(), is))) {
                // TODO 这里进行成功后的订单业务处理
                // TODO 返回成功付款页面，这个到时候再做一个漂亮的页面显示，并使用前后端分离的模式
                return service.successPayOutMessage(null).toMessage();
            }
        }
        return "failure";
    }

    /**
     * 支付回调地址
     *
     * @param request  请求
     *
     * @return 结果
     * @throws IOException IOException
     * 业务处理在对应的PayMessageHandler里面处理，在哪里设置PayMessageHandler，详情查看{@link com.egzosn.pay.common.api.PayService#setPayMessageHandler(com.egzosn.pay.common.api.PayMessageHandler)}
     *
     * 如果未设置 {@link com.egzosn.pay.common.api.PayMessageHandler} 那么会使用默认的 {@link com.egzosn.pay.common.api.DefaultPayMessageHandler}
     *
     */
    @RequestMapping(value = "payBack.json")
    public String payBack(HttpServletRequest request) throws IOException {
        //业务处理在对应的PayMessageHandler里面处理，在哪里设置PayMessageHandler，详情查看com.egzosn.pay.common.api.PayService.setPayMessageHandler()
        return service.payBack(request.getParameterMap(), request.getInputStream()).toMessage();
    }


}
