
package com.egzosn.pay.demo.controller;


import com.egzosn.pay.common.api.Callback;
import com.egzosn.pay.common.api.PayService;
import com.egzosn.pay.common.bean.*;
import com.egzosn.pay.common.http.UriVariables;
import com.egzosn.pay.demo.entity.PayType;
import com.egzosn.pay.demo.request.QueryOrder;
import com.egzosn.pay.demo.service.PayResponse;
import com.egzosn.pay.wx.api.WxPayConfigStorage;
import com.egzosn.pay.wx.api.WxPayService;
import com.egzosn.pay.wx.bean.WxBank;
import com.egzosn.pay.wx.bean.WxTransactionType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 发起支付入口
 *
 * @author: egan
 * @email egzosn@gmail.com
 * @date 2016/11/18 0:25
 */
@RestController
@RequestMapping("wx")
public class WxPayController {

    private PayService service = null;

    @PostConstruct
    public void init() {
        WxPayConfigStorage wxPayConfigStorage = new WxPayConfigStorage();
        wxPayConfigStorage.setMchId("合作者id（商户号）");
        wxPayConfigStorage.setAppid("应用id");
        wxPayConfigStorage.setKeyPublic("转账公钥，转账时必填");
        wxPayConfigStorage.setSecretKey("密钥");
        wxPayConfigStorage.setNotifyUrl("异步回调地址");
        wxPayConfigStorage.setReturnUrl("同步回调地址");
        wxPayConfigStorage.setSignType("签名方式");
        wxPayConfigStorage.setInputCharset("utf-8");


        service = new WxPayService(wxPayConfigStorage);


    }



    /**
     * 跳到支付页面
     * 针对实时支付
     *
     * @param price       金额
     * @return 跳到支付页面
     */
    @RequestMapping(value = "toPay.html", produces = "text/html;charset=UTF-8")
    public String toPay( HttpServletRequest request, BigDecimal price) {
        PayOrder order = new PayOrder("订单title", "摘要",  null == price ? new BigDecimal(0.01) : price , UUID.randomUUID().toString().replace("-", ""),  WxTransactionType.MWEB);
        order.setSpbillCreateIp(request.getHeader("X-Real-IP"));
        StringBuffer requestURL = request.getRequestURL();
        //设置网页地址
        order.setWapUrl(requestURL.substring(0, requestURL.indexOf("/") > 0 ? requestURL.indexOf("/") : requestURL.length() ));
        //设置网页名称
        order.setWapName("在线充值");

        Map orderInfo = service.orderInfo(order);
        return service.buildRequest(orderInfo, MethodType.POST);
    }

    /**
     * 公众号支付
     *
     *
     * @param openid openid
     * @param price 金额
     * @return 返回jsapi所需参数
     */
    @RequestMapping(value = "jsapi" )
    public Map toPay(String openid, BigDecimal price) {

        PayOrder order = new PayOrder("订单title", "摘要", null == price ? new BigDecimal(0.01) : price, UUID.randomUUID().toString().replace("-", ""), WxTransactionType.JSAPI);
        order.setOpenid(openid);

        Map orderInfo = service.orderInfo(order);
        orderInfo.put("code", 0);

        return orderInfo;
    }



    /**
     * 获取支付预订单信息
     *
     * @return 支付预订单信息
     */
    @RequestMapping("app")
    public Map<String, Object> app() {
        Map<String, Object> data = new HashMap<>();
        data.put("code", 0);
        PayOrder order = new PayOrder("订单title", "摘要", new BigDecimal(0.01), UUID.randomUUID().toString().replace("-", ""));
        //App支付
        order.setTransactionType(WxTransactionType.APP);
        data.put("orderInfo", service.orderInfo(order));
        return data;
    }

    /**
     * 获取二维码图像
     * 二维码支付
     * @param price       金额
     * @return 二维码图像
     */
    @RequestMapping(value = "toQrPay.jpg", produces = "image/jpeg;charset=UTF-8")
    public byte[] toWxQrPay( BigDecimal price) throws IOException {
        //获取对应的支付账户操作工具（可根据账户id）
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(service.genQrPay( new PayOrder("订单title", "摘要", null == price ? new BigDecimal(0.01) : price, System.currentTimeMillis()+"", WxTransactionType.NATIVE)), "JPEG", baos);
        return baos.toByteArray();
    }


    /**
     * 刷卡付,pos主动扫码付款(条码付)
     * @param authCode        授权码，条码等
     * @param price       金额
     * @return 支付结果
     */
    @RequestMapping(value = "microPay")
    public Map<String, Object> microPay( BigDecimal price, String authCode) throws IOException {
        //获取对应的支付账户操作工具（可根据账户id）
        //条码付
        PayOrder order = new PayOrder("huodull order", "huodull order", null == price ? new BigDecimal(0.01) : price, UUID.randomUUID().toString().replace("-", ""), WxTransactionType.MICROPAY);
        //设置授权码，条码等
        order.setAuthCode(authCode);
        //支付结果
        Map<String, Object> params = service.microPay(order);
        //校验
        if (service.verify(params)) {

            //支付校验通过后的处理
            //......业务逻辑处理块........


        }
        //这里开发者自行处理
        return params;
    }

    /**
     * 支付回调地址
     *
     * @param request
     *
     * @return
     */
    @RequestMapping(value = "payBack.json")
    public String payBack(HttpServletRequest request) throws IOException {

        //获取支付方返回的对应参数
        Map<String, Object> params = service.getParameter2Map(request.getParameterMap(), request.getInputStream());
        if (null == params) {
            return service.getPayOutMessage("fail", "失败").toMessage();
        }

        //校验
        if (service.verify(params)) {
            //这里处理业务逻辑
            //......业务逻辑处理块........
            return service.getPayOutMessage("success", "成功").toMessage();
        }

        return service.getPayOutMessage("fail", "失败").toMessage();
    }


    /**
     * 查询
     *
     * @param order 订单的请求体
     * @return 返回查询回来的结果集，支付方原值返回
     */
    @RequestMapping("query")
    public Map<String, Object> query(QueryOrder order) {
        return service.query(order.getTradeNo(), order.getOutTradeNo());
    }


    /**
     * 交易关闭接口
     *
     * @param order 订单的请求体
     * @return 返回支付方交易关闭后的结果
     */
    @RequestMapping("close")
    public Map<String, Object> close(QueryOrder order) {
        return service.close(order.getTradeNo(), order.getOutTradeNo());
    }

    /**
     * 申请退款接口
     *
     * @param order 订单的请求体
     * @return 返回支付方申请退款后的结果
     */
    @RequestMapping("refund")
    public Map<String, Object> refund(RefundOrder order) {
        return service.refund(order);
    }

    /**
     * 查询退款
     *
     * @param order 订单的请求体
     * @return 返回支付方查询退款后的结果
     */
    @RequestMapping("refundquery")
    public Map<String, Object> refundquery(QueryOrder order) {
        return service.refundquery(order.getTradeNo(), order.getOutTradeNo());
    }

    /**
     * 下载对账单
     *
     * @param order 订单的请求体
     * @return 返回支付方下载对账单的结果
     */
    @RequestMapping("downloadbill")
    public Object downloadbill(QueryOrder order) {
        return service.downloadbill(order.getBillDate(), order.getBillType());
    }


    /**
     * 通用查询接口，根据 WxTransactionType 类型进行实现,此接口不包括退款
     *
     * @param order 订单的请求体
     * @return 返回支付方对应接口的结果
     */
    @RequestMapping("secondaryInterface")
    public Map<String, Object> secondaryInterface(QueryOrder order) {
        TransactionType type = WxTransactionType.valueOf(order.getTransactionType());
        return service.secondaryInterface(order.getTradeNoOrBillDate(), order.getOutTradeNoBillType(), type);
    }



    /**
     * 转账
     *
     * @param order 转账订单
     *
     * @return 对应的转账结果
     */
    @RequestMapping("transfer")
    public Map<String, Object> transfer(TransferOrder order) {
        order.setOutNo("partner_trade_no 商户转账订单号");
        //采用标准RSA算法，公钥由微信侧提供,将公钥信息配置在PayConfigStorage#setKeyPublic(String)
        order.setPayeeAccount("enc_bank_no 收款方银行卡号");
        order.setPayeeName("收款方用户名");
        order.setBank(WxBank.ABC);
        order.setRemark("转账备注, 非必填");
        order.setAmount(new BigDecimal(10));
        return service.transfer(order);
    }

    /**
     * 转账查询
     *
     * @param outNo   商户转账订单号
     * @param tradeNo 支付平台转账订单号
     *
     * @return 对应的转账订单
     */
    @RequestMapping("transferQuery")
    public Map<String, Object> transferQuery(String outNo, String tradeNo) {
        return service.transferQuery(outNo, tradeNo);
    }
}
