
package com.egzosn.pay.demo.controller;


import com.egzosn.pay.common.api.PayService;
import com.egzosn.pay.common.bean.CertStoreType;
import com.egzosn.pay.common.bean.MethodType;
import com.egzosn.pay.common.bean.PayOrder;
import com.egzosn.pay.common.bean.RefundOrder;
import com.egzosn.pay.common.http.HttpConfigStorage;
import com.egzosn.pay.common.util.sign.SignUtils;
import com.egzosn.pay.demo.request.QueryOrder;
import com.egzosn.pay.union.api.UnionPayConfigStorage;
import com.egzosn.pay.union.api.UnionPayService;
import com.egzosn.pay.union.bean.UnionTransactionType;
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

import static com.egzosn.pay.union.bean.UnionTransactionType.WEB;

/**
 *  银联相关
 *
 * @author: egan
 * email egzosn@gmail.com
 * date 2016/11/18 0:25
 */
@RestController
@RequestMapping("union")
public class UnionPayController {

    private UnionPayService service = null;

    @PostConstruct
    public void init() {
        UnionPayConfigStorage unionPayConfigStorage = new UnionPayConfigStorage();
        unionPayConfigStorage.setMerId("700000000000001");
        //是否为证书签名
        unionPayConfigStorage.setCertSign(true);
        //中级证书路径
        unionPayConfigStorage.setAcpMiddleCert("D:/certs/acp_test_middle.cer");
        //根证书路径
        unionPayConfigStorage.setAcpRootCert("D:/certs/acp_test_root.cer");
        // 私钥证书路径
        unionPayConfigStorage.setKeyPrivateCert("D:/certs/acp_test_sign.pfx");
        //私钥证书对应的密码
        unionPayConfigStorage.setKeyPrivateCertPwd("000000");
        //设置证书对应的存储方式，这里默认为文件地址
        unionPayConfigStorage.setCertStoreType(CertStoreType.PATH);




        //前台通知网址  即SDKConstants.param_frontUrl
        unionPayConfigStorage.setReturnUrl("http://www.pay.egzosn.com/payBack.json");
        //后台通知地址  即SDKConstants.param_backUrl
        unionPayConfigStorage.setNotifyUrl("http://www.pay.egzosn.com/payBack.json");
        //加密方式
        unionPayConfigStorage.setSignType(SignUtils.RSA2.name());
        //单一支付可不填
        unionPayConfigStorage.setPayType("unionPay");
        unionPayConfigStorage.setInputCharset("UTF-8");
        //是否为测试账号，沙箱环境
        unionPayConfigStorage.setTest(true);
        service = new UnionPayService(unionPayConfigStorage);

        //请求连接池配置
        HttpConfigStorage httpConfigStorage = new HttpConfigStorage();
        //最大连接数
        httpConfigStorage.setMaxTotal(20);
        //默认的每个路由的最大连接数
        httpConfigStorage.setDefaultMaxPerRoute(10);
        service.setRequestTemplateConfigStorage(httpConfigStorage);

    }



    /**
     * ---业务实现例子1---
     * 功能：生成自动跳转的Html表单
     * 业务类型（关键字）:网关支付(WEB)/手机网页支付,企业网银支付（B2B）,
     * @param price       金额
     * @return 生成自动跳转的Html表单
     */
    @RequestMapping(value = "toPay.html", produces = "text/html;charset=UTF-8")
    public String toPay( BigDecimal price) {
        //网关支付(WEB)/手机网页支付
        PayOrder order = new PayOrder("订单title", "摘要", null == price ? new BigDecimal(0.01) : price, UUID.randomUUID().toString().replace("-", ""),
                WEB);
         //企业网银支付（B2B支付）
//        PayOrder order = new PayOrder("订单title", "摘要", null == price ? new BigDecimal(0.01) : price, UUID.randomUUID().toString().replace("-", ""), UnionTransactionType.B2B);

//        Map orderInfo = service.orderInfo(order);
//        return service.buildRequest(orderInfo, MethodType.POST);
        return service.toPay(order);
    }

    /**
     *  ---业务实现例子2---
     * 功能：获取调起控件的tn号，支付结果等
     * 业务类型:手机控件支付产品(WAP),
     * @param price             金额
     * @return 支付结果
     */
    @RequestMapping(value = "toPay.json")
    public Map<String, Object> sendHttpRequest( BigDecimal price) {
        //手机控件支付产品
        PayOrder order = new PayOrder("订单title", "摘要", null == price ? new BigDecimal(0.01) : price, UUID.randomUUID().toString().replace("-", "")
                ,UnionTransactionType.WAP);
        return service.sendHttpRequest(order);
    }



    /**
     *  APP 获取支付预订单信息
     *
     * @return 支付预订单信息
     */
    @RequestMapping("app")
    public Map<String, Object> app() {
        Map<String, Object> data = new HashMap<>();
        data.put("code", 0);
        PayOrder order = new PayOrder("订单title", "摘要", new BigDecimal(0.01), SignUtils.randomStr());
        //App支付
        order.setTransactionType(UnionTransactionType.APP);

        //APPLE支付 苹果付
//        order.setTransactionType(UnionTransactionType.APPLE);

        data.put("orderInfo", service.orderInfo(order));
        return data;
    }

    /**
     * 获取二维码图像 APPLY_QR_CODE
     * 二维码支付
     * @param price       金额
     * @return 二维码图像
     * @throws IOException IOException
     */
    @RequestMapping(value = "toQrPay.jpg", produces = "image/jpeg;charset=UTF-8")
    public byte[] toWxQrPay( BigDecimal price) throws IOException {
        //获取对应的支付账户操作工具（可根据账户id）
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(service.genQrPay( new PayOrder("订单title", "摘要", null == price ? new BigDecimal(0.01) : price, System.currentTimeMillis()+"", UnionTransactionType.APPLY_QR_CODE)), "JPEG", baos);
        return baos.toByteArray();
    }
    /**
     * 获取二维码地址
     * 二维码支付
     * @param price       金额
     * @return 二维码图像
     * @throws IOException IOException
     */
    @RequestMapping(value = "getQrPay.json")
    public String getQrPay(BigDecimal price) throws IOException {
        //获取对应的支付账户操作工具（可根据账户id）
        return service.getQrPay( new PayOrder("订单title", "摘要", null == price ? new BigDecimal(0.01) : price, System.currentTimeMillis()+"", UnionTransactionType.APPLY_QR_CODE));
    }

    /**
     * 刷卡付,pos主动扫码付款(条码付)  CONSUME
     * @param authCode        授权码，条码等
     * @param price       金额
     * @return 支付结果
     */
    @RequestMapping(value = "microPay")
    public Map<String, Object> microPay(BigDecimal price, String authCode)  {
        //获取对应的支付账户操作工具（可根据账户id）
        //条码付
        PayOrder order = new PayOrder("egan order", "egan order", null == price ? new BigDecimal(0.01) : price, SignUtils.randomStr(), UnionTransactionType.CONSUME);
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
     * 支付回调地址 方式一
     *
     * 方式二，{@link #payBack(HttpServletRequest)} 是属于简化方式， 试用与简单的业务场景
     *
     * @param request 请求
     *
     * @return 是否成功
     * @see #payBack(HttpServletRequest)
     * @throws IOException IOException
     */
    @Deprecated
    @RequestMapping(value = "payBackBefore.json")
    public String payBackBefore(HttpServletRequest request) throws IOException {

        //获取支付方返回的对应参数
        Map<String, Object> params = service.getParameter2Map(request.getParameterMap(), request.getInputStream());
        if (null == params) {
            return service.getPayOutMessage("fail", "失败").toMessage();
        }

        //校验
        if (service.verify(params)) {
            //这里处理业务逻辑
            //......业务逻辑处理块........
            return service.successPayOutMessage(null).toMessage();
        }

        return service.getPayOutMessage("fail", "失败").toMessage();
    }
    /**
     * 支付回调地址
     *
     * @param request 请求
     *
     * @return  是否成功
     *
     * 业务处理在对应的PayMessageHandler里面处理，在哪里设置PayMessageHandler，详情查看{@link PayService#setPayMessageHandler(com.egzosn.pay.common.api.PayMessageHandler)}
     *
     * 如果未设置 {@link com.egzosn.pay.common.api.PayMessageHandler} 那么会使用默认的 {@link com.egzosn.pay.common.api.DefaultPayMessageHandler}
     * @throws IOException IOException
     *
     */
    @RequestMapping(value = "payBack.json")
    public String payBack(HttpServletRequest request) throws IOException {
        //业务处理在对应的PayMessageHandler里面处理，在哪里设置PayMessageHandler，详情查看com.egzosn.pay.common.api.PayService.setPayMessageHandler()
        return service.payBack(request.getParameterMap(), request.getInputStream()).toMessage();
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



}
