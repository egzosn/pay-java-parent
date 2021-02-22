package com.egzosn.pay.demo.controller;


import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.egzosn.pay.common.api.PayService;
import com.egzosn.pay.common.bean.DefaultCurType;
import com.egzosn.pay.common.bean.RefundOrder;
import com.egzosn.pay.common.bean.RefundResult;
import com.egzosn.pay.paypal.api.PayPalConfigStorage;
import com.egzosn.pay.paypal.v2.api.PayPalPayService;
import com.egzosn.pay.paypal.v2.bean.PayPalOrder;
import com.egzosn.pay.paypal.v2.bean.order.AddressPortable;
import com.egzosn.pay.paypal.v2.bean.order.Name;
import com.egzosn.pay.paypal.v2.bean.order.ShippingDetail;

/**
 * 发起支付入口
 *
 * @author: egan
 * email egzosn@gmail.com
 * date 2018/05/06 10:30
 */
@RestController
@RequestMapping("payPalV2")
public class PayPalV2PayController {


    private PayService service = null;

    @PostConstruct
    public void init() {
        PayPalConfigStorage storage = new PayPalConfigStorage();
        storage.setClientID("AZDS0IhUZvJTO99unlvSDMfbZIP-p-UecYXZdJoweha9LFuqKXKcQIGZgfVaX6oGiAOJAUuJD7JwyTl1");
        storage.setClientSecret("EK2YaOrw3oLSDWIRzvb9BWGTjiPPhY1fFUu5ylhUsGYLc_h_dlpJ0hr_LDEkbO9MyKP2P83YcywbPaem");
        storage.setTest(true);
        //发起付款后的页面转跳地址
        storage.setReturnUrl("http://www.egzosn.com/payPal/payBack.json");
        // 注意：这里不是异步回调的通知 IPN 地址设置的路径：https://developer.paypal.com/developer/ipnSimulator/
        //取消按钮转跳地址,
        storage.setCancelUrl("http://www.egzosn.com/pay/cancel");
        service = new PayPalPayService(storage);
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
        PayPalOrder order = new PayPalOrder();
        order.setBrandName("该标签将覆盖PayPal网站上PayPal帐户中的公司名称,非必填");
        order.setDescription("订单说明");
        order.setInvoiceId("非必填    API调用者为该订单提供的外部发票号码。出现在付款人的交易历史记录和付款人收到的电子邮件中。");
        order.setCustomId("非必填 api调用中没发现有任何用处    API调用者提供的外部ID。用于协调客户端交易与PayPal交易。出现在交易和结算报告中，但付款人不可见");
        order.setPrice(price);
        order.setShippingDetail(new ShippingDetail()
                .name(new Name().fullName("RATTA"))
                .addressPortable(new AddressPortable()
                        .addressLine1("梅陇镇")
                        .addressLine2("集心路168号")
                        .adminArea2("闵行区")
                        .adminArea1("上海市")
                        .postalCode("20000")
                        .countryCode("CN")));
        String toPayHtml = service.toPay(order);

        //某些支付下单时无法设置单号，通过下单后返回对应单号，如 paypal，友店。
        String tradeNo = order.getTradeNo();
        System.out.println("支付订单号：" + tradeNo + "  这里可以进行回存");

        return toPayHtml;
    }

    /**
     * 申请退款接口
     *
     * @return 返回支付方申请退款后的结果
     */
    @RequestMapping("refund")
    public RefundResult refund() {
        // TODO 这里需要  refundAmount， curType， description， tradeNo
        RefundOrder order = new RefundOrder();
        order.setCurType(DefaultCurType.USD);
        order.setDescription(" description ");
        order.setTradeNo("paypal 平台的单号, 支付下单返回的单号");
        order.setRefundAmount(BigDecimal.valueOf(0.01));
        RefundResult refundResult = service.refund(order);
        System.out.println("退款成功之后返回退款单号：" + refundResult.getRefundNo());
        return refundResult;
    }

    /**
     * 查询退款
     *
     * @return 返回支付方查询退款后的结果
     */
    @RequestMapping("refundquery")
    public Map<String, Object> refundquery() {
        RefundOrder order = new RefundOrder();
        order.setRefundNo("退款成功之后返回的退款单号");
        return service.refundquery(order);
    }
    /**
     * 注意：这里不是异步回调的通知 IPN 地址设置的路径：https://developer.paypal.com/developer/ipnSimulator/
     * PayPal确认付款调用的接口
     * 用户确认付款后，paypal调用的这个方法执行付款
     *
     * @param request 请求
     * @return 付款成功信息
     * @throws IOException IOException
     */
    @GetMapping(value = "payBackBefore.json")
    public String payBackBefore(HttpServletRequest request) throws IOException {
        try (InputStream is = request.getInputStream()) {
            // 参数解析与校验  https://developer.paypal.com/docs/api-basics/notifications/ipn/IPNIntro/#id08CKFJ00JYK
            if (service.verify(service.getParameter2Map(request.getParameterMap(), is))) {
                // TODO 这里进行成功后的订单业务处理
                // TODO 返回成功付款页面，这个到时候再做一个漂亮的页面显示，并使用前后端分离的模式
                return service.successPayOutMessage(null).toMessage();
            }
        }
        return "failure";
    }

    /*   */

    /**
     * 支付回调地址
     * 注意：这里不是异步回调的通知 IPN 地址设置的路径：https://developer.paypal.com/developer/ipnSimulator/
     * 参数解析与校验  https://developer.paypal.com/docs/api-basics/notifications/ipn/IPNIntro/#id08CKFJ00JYK
     *
     * @param request 请求
     * @return 结果
     * @throws IOException IOException
     *                     业务处理在对应的PayMessageHandler里面处理，在哪里设置PayMessageHandler，详情查看{@link PayService#setPayMessageHandler(com.egzosn.pay.common.api.PayMessageHandler)}
     *                     <p>
     *                     如果未设置 {@link com.egzosn.pay.common.api.PayMessageHandler} 那么会使用默认的 {@link com.egzosn.pay.common.api.DefaultPayMessageHandler}
     */
    @RequestMapping(value = "payBack.json")
    public String payBack(HttpServletRequest request) throws IOException {
        //业务处理在对应的PayMessageHandler里面处理，在哪里设置PayMessageHandler，详情查看com.egzosn.pay.common.api.PayService.setPayMessageHandler()
        // 参数解析与校验  https://developer.paypal.com/docs/api-basics/notifications/ipn/IPNIntro/#id08CKFJ00JYK
        return service.payBack(request.getParameterMap(), request.getInputStream()).toMessage();
    }


}
