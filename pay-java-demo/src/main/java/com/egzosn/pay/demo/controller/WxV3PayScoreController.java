package com.egzosn.pay.demo.controller;

import com.egzosn.pay.common.bean.*;
import com.egzosn.pay.common.http.HttpConfigStorage;
import com.egzosn.pay.wx.v3.api.WxPayScoreService;
import com.egzosn.pay.wx.v3.api.WxPayConfigStorage;
import com.egzosn.pay.wx.v3.api.WxPayService;
import com.egzosn.pay.wx.v3.bean.payscore.*;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/wxV3CreditScore")
public class WxV3PayScoreController {

    private WxPayService service3 = null;
    private WxPayScoreService wxPayScoreService = null;

    private static final String APPID = "wxc7b993ff15a9f26c";
    private static final String SERVICE_ID = "xxxxxxxxxxxxxxxxxxxxxxxxxxxxx";

    private static final String MCH_ID = "1602947765"; // 商户号

    private static final String V3_API_KEY = "9bd8f0e7af4841299d782406b7774f57";
    //    @PostConstruct
    public void init() {
        WxPayConfigStorage paymentStandardConfig = new WxPayConfigStorage();
        paymentStandardConfig.setAppid(APPID);
        paymentStandardConfig.setServiceId(SERVICE_ID);
        paymentStandardConfig.setMchId(MCH_ID);
        paymentStandardConfig.setV3ApiKey(V3_API_KEY);
        paymentStandardConfig.setNotifyUrl("http://sailinmu.iok.la/wxV3combine/payBack.json");
        paymentStandardConfig.setInputCharset("UTF-8");
        paymentStandardConfig.setSignType("MD5");
        paymentStandardConfig.setCertStoreType(CertStoreType.PATH);
        paymentStandardConfig.setApiClientKeyP12("apiclient_cert.p12");

        HttpConfigStorage httpConfigStorage = new HttpConfigStorage();
        httpConfigStorage.setStorePassword(MCH_ID);
        httpConfigStorage.setKeystore("apiclient_cert.p12");
        service3 = new WxPayService(paymentStandardConfig,httpConfigStorage);
        wxPayScoreService = new WxPayScoreService(paymentStandardConfig,httpConfigStorage);
    }
    @PostMapping("/refund")
    public RefundResult refund() {
        RefundOrder refundOrder = new RefundOrder();

        refundOrder.setRefundNo("R2023082416493947872");

        refundOrder.setTradeNo("4200001930202308240314610507");
        //订单号
        refundOrder.setOutTradeNo("P2023082416243247872");
        //退款金额
        refundOrder.setRefundAmount(new BigDecimal("0.01"));
        //退款备注
        refundOrder.setDescription("退款测试");

        refundOrder.setCurType(DefaultCurType.CNY);
        //总金额
        refundOrder.setTotalAmount(new BigDecimal("0.01"));
        refundOrder.addAttr("funds_account","AVAILABLE");
        return service3.refund(refundOrder);
    }




    @GetMapping("/queryOrder")
    public Map<String, Object> queryOrder(AssistOrder order) {
        return wxPayScoreService.query(order);
    }
    @GetMapping("/queryRefundOrder")
    public Map<String, Object> queryRefundOrder(RefundOrder refundOrder) {
        return service3.refundquery(refundOrder);
    }



    @PostMapping("/create")
    public Map<String,Object> create() {
        CreateOrder createOrder = new CreateOrder();
        createOrder.setOutTradeNo("P2023091301010100000");
        createOrder.setStartTime("OnAccept");
        //paymentRequest.setStartTime(DateUtils.formatDate(new Date(),DateUtils.YYYYMMDDHHMMSS));
        createOrder.setServiceIntroduction("测试");
        createOrder.setRiskFundAmount(new BigDecimal("1.11"));
        createOrder.setRiskFundName("ESTIMATE_ORDER_COST");
        createOrder.setOpenId("oZu615JDX_H9Ni4KXmiXzuCKiBqQ");
        return wxPayScoreService.create(createOrder);
    }


    @PostMapping("/cancel")
    public Map<String,Object> cancel() {
        //撤销智慧零售
        CancelOrder cancelOrder = new CancelOrder();
        cancelOrder.setOutTradeNo("商户订单号");
        cancelOrder.setReason("测试");
        return wxPayScoreService.cancel(cancelOrder.getOutTradeNo(),cancelOrder.getReason());
    }


    @PostMapping("/modify")
    public Map<String,Object> modify() {
        //修改订单金额
        ModifyOrder modifyOrder = new ModifyOrder();
        modifyOrder.setOutTradeNo("P2023091301010100000");
        PostPayment postPayment = new PostPayment();
        postPayment.setAmount(BigDecimal.ONE);
        postPayment.setName("ESTIMATE_ORDER_COST");
        modifyOrder.setPostPayments(Arrays.asList(postPayment));
        modifyOrder.setTotalAmount(BigDecimal.ONE);
        modifyOrder.setReason("test");
        return wxPayScoreService.modify(modifyOrder);
    }

    @PostMapping("/complete")
    public Map<String,Object> complete() {
        //修改订单金额
        CompleteOrder completeOrder = new CompleteOrder();
        completeOrder.setOutTradeNo("P2023091301010100000");
        PostPayment postPayment = new PostPayment();
        postPayment.setAmount(BigDecimal.ONE);
        postPayment.setName("ESTIMATE_ORDER_COST");
        completeOrder.setPostPayments(Arrays.asList(postPayment));
        completeOrder.setTotalAmount(BigDecimal.ONE);
        return wxPayScoreService.complete(completeOrder);
    }

    @PostMapping("/sync")
    public Map<String,Object> sync() {
        //修改订单金额
        SyncOrder syncOrder = new SyncOrder();
        syncOrder.setOutTradeNo("商户订单号");
        syncOrder.setPaidTime(new Date());
        return wxPayScoreService.sync(syncOrder.getOutTradeNo(),syncOrder.getPaidTime());
    }
}
