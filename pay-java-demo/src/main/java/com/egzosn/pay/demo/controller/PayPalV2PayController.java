package com.egzosn.pay.demo.controller;


import java.math.BigDecimal;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.PostMapping;
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
import com.egzosn.pay.web.support.HttpRequestNoticeParams;

/**
 * 发起支付入口
 *
 * @author egan
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
     * 支付回调地址
     * 请求方式必须为post, 回调详情文档：https://developer.paypal.com/api/rest/webhooks
     * 回调成功之后必须返回http状态码 200 才能行，不然一直会重复回调，注意:如果您的应用程序以任何其他状态码响应，贝宝会在三天内重试25次通知消息
     *
     * @param request  请求
     * @param response 响应
     * @return 是否成功
     * <p>
     * 业务处理在对应的PayMessageHandler里面处理，在哪里设置PayMessageHandler，详情查看{@link com.egzosn.pay.common.api.PayService#setPayMessageHandler(com.egzosn.pay.common.api.PayMessageHandler)}
     * <p>
     * 如果未设置 {@link com.egzosn.pay.common.api.PayMessageHandler} 那么会使用默认的 {@link com.egzosn.pay.common.api.DefaultPayMessageHandler}
     * 付款之后不会进行扣款，需要调用 {@link PayPalPayService#ordersCapture(java.lang.String)}进行扣款，并返回 captureId使用，后续退款，查订单等等使用，用来替换下单返回的id
     * 注意：最好在付款成功之后回调时进行调用 {@link PayPalPayService#ordersCapture(java.lang.String)}
     * 确认订单并返回确认后订单信息
     * <b>注意：此方法一个订单只能调用一次, 建议在支付回调时进行调用</b>
     * 这里主要用来获取captureId使用，后续退款，查订单等等使用，用来替换下单返回的id
     * 详情： https://developer.paypal.com/docs/api/orders/v2/#orders_capture
     */
    @PostMapping(value = "payBack.json")
    public String payBack(HttpServletRequest request, HttpServletResponse response) {
        //业务处理在对应的PayMessageHandler里面处理，在哪里设置PayMessageHandler，详情查看com.egzosn.pay.common.api.PayService.setPayMessageHandler()
        final String message = service.payBack(new HttpRequestNoticeParams(request)).toMessage();
        if (!"200".equals(message)) {
            response.setStatus(400);
        }
        return message;
    }


}
