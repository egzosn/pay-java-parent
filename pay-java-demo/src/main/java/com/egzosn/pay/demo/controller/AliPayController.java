
package com.egzosn.pay.demo.controller;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.egzosn.pay.ali.api.AliPayConfigStorage;
import com.egzosn.pay.ali.api.AliPayService;
import com.egzosn.pay.ali.bean.AliRefundResult;
import com.egzosn.pay.ali.bean.AliTransactionType;
import com.egzosn.pay.ali.bean.AliTransferOrder;
import com.egzosn.pay.ali.bean.AliTransferType;
import com.egzosn.pay.ali.bean.OrderSettle;
import com.egzosn.pay.common.bean.AssistOrder;
import com.egzosn.pay.common.bean.CertStoreType;
import com.egzosn.pay.common.bean.NoticeParams;
import com.egzosn.pay.common.bean.PayOrder;
import com.egzosn.pay.common.bean.RefundOrder;
import com.egzosn.pay.common.http.HttpConfigStorage;
import com.egzosn.pay.common.http.UriVariables;
import com.egzosn.pay.common.util.sign.SignUtils;
import com.egzosn.pay.demo.request.QueryOrder;
import com.egzosn.pay.demo.service.handler.AliPayMessageHandler;
import com.egzosn.pay.demo.service.interceptor.AliPayMessageInterceptor;
import com.egzosn.pay.web.support.HttpRequestNoticeParams;


/**
 * 发起支付入口
 *
 * @author egan
 * email egzosn@gmail.com
 * date 2016/11/18 0:25
 */
@RestController
@RequestMapping("ali")
public class AliPayController {

    private AliPayService service = null;
    @Resource
    private AutowireCapableBeanFactory spring;

    /**
     * 设置普通公钥的方式
     * 普通公钥方式与证书公钥方式为两者取其一的方式
     *
     * @param aliPayConfigStorage 支付宝配置信息
     */
    private static void keyPublic(AliPayConfigStorage aliPayConfigStorage) {
        aliPayConfigStorage.setKeyPublic("MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDIgHnOn7LLILlKETd6BFRJ0GqgS2Y3mn1wMQmyh9zEyWlz5p1zrahRahbXAfCfSqshSNfqOmAQzSHRVjCqjsAw1jyqrXaPdKBmr90DIpIxmIyKXv4GGAkPyJ/6FTFY99uhpiq0qadD/uSzQsefWo0aTvP/65zi3eof7TcZ32oWpwIDAQAB");
    }

    /**
     * 设置证书公钥信息
     * 普通公钥方式与证书公钥方式为两者取其一的方式
     *
     * @param aliPayConfigStorage 支付宝配置信息
     */
    private static void certKeyPublic(AliPayConfigStorage aliPayConfigStorage) {
        //设置为证书方式
        aliPayConfigStorage.setCertSign(true);
        //设置证书存储方式，这里为路径
        aliPayConfigStorage.setCertStoreType(CertStoreType.CLASS_PATH);
        aliPayConfigStorage.setMerchantCert("ali/appCertPublicKey_2016080400165436.crt");
        aliPayConfigStorage.setAliPayRootCert("ali/alipayRootCert.crt");
        aliPayConfigStorage.setAliPayCert("ali/alipayCertPublicKey_RSA2.crt");
    }

    @PostConstruct
    public void init() {
        AliPayConfigStorage aliPayConfigStorage = new AliPayConfigStorage();
        aliPayConfigStorage.setPid("2088102169916436");
        aliPayConfigStorage.setAppId("2016080400165436");
//        aliPayConfigStorage.setAppAuthToken("ISV代商户代用，指定appAuthToken");
        //普通公钥方式与证书公钥方式为两者取其一的方式
        keyPublic(aliPayConfigStorage);
        aliPayConfigStorage.setKeyPrivate("MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAKroe/8h5vC4L6T+B2WdXiVwGsMvUKgb2XsKix6VY3m2wcf6tyzpNRDCNykbIwGtaeo7FshN+qZxdXHLiIam9goYncBit/8ojfLGy2gLxO/PXfzGxYGs0KsDZ+ryVPPmE34ZZ8jiJpR0ygzCFl8pN3QJPJRGTJn5+FTT9EF/9zyZAgMBAAECgYAktngcYC35u7cQXDk+jMVyiVhWYU2ULxdSpPspgLGzrZyG1saOcTIi/XVX8Spd6+B6nmLQeF/FbU3rOeuD8U2clzul2Z2YMbJ0FYay9oVZFfp5gTEFpFRTVfzqUaZQBIjJe/xHL9kQVqc5xHlE/LVA27/Kx3dbC35Y7B4EVBDYAQJBAOhsX8ZreWLKPhXiXHTyLmNKhOHJc+0tFH7Ktise/0rNspojU7o9prOatKpNylp9v6kux7migcMRdVUWWiVe+4ECQQC8PqsuEz7B0yqirQchRg1DbHjh64bw9Kj82EN1/NzOUd53tP9tg+SO97EzsibK1F7tOcuwqsa7n2aY48mQ+y0ZAkBndA2xcRcnvOOjtAz5VO8G7R12rse181HjGfG6AeMadbKg30aeaGCyIxN1loiSfNR5xsPJwibGIBg81mUrqzqBAkB+K6rkaPXJR9XtzvdWb/N3235yPkDlw7Z4MiOVM3RzvR/VMDV7m8lXoeDde2zQyeMOMYy6ztwA6WgE1bhGOnQRAkEAouUBv1sVdSBlsexX15qphOmAevzYrpufKgJIRLFWQxroXMS7FTesj+f+FmGrpPCxIde1dqJ8lqYLTyJmbzMPYw==");
//        certKeyPublic(aliPayConfigStorage);
//        aliPayConfigStorage.setKeyPrivate("MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQCw7MD2Cwv/jnXssFjXnGx3JlGF57gJa2aYbJRV8MnNiPVpX4Ha+8ZjnQDhvkrWH4hHmzcujOr213HqloMpUSYBzCPiXGVRUUvdimejcHHTod7nI4g6nztzzfey/TXNDHmp7vY3pOIcjB0Zn0pkNAz2tKAFkqb4raHOqTB0QA0zD24Cn+26J2UJyYRcgeH0GtSQuUrm7yaGsuKakh+qtgWF6R71n5PMGOTQ5LH3i0WVHfCBkNGgJC6yC96HR4D7cosoyKD0+lp8UB/NVUWl7Tt/KLOgFUwh0GKSYFfv56O/VBV2+xqCGE4PlZESfVuOqz5vjjxzw3xDAUJrV8hSX/AJAgMBAAECggEBAKE0d3U4B4yo/2XUIH8EdgfykCFUSum6RFbpyBauORHfksyaSzV+ZvtomN8XhhSn0oJ8OMFfgM+86nz2+zdwSxMkMCYWTfLUAi4v59KRqAVO3kz4oS3Y3FDeAK3D7XuRvGFL7GgzAhtEx1cLPrsiehVn6s5pG15GxsIIgq/JlL1J88wn1zENLrVHmD6z/JpXvfb/RS1yR+5lyoohp4g0Ph9jJ3bCyUbRpK0QkPEzgAuWL0K2ITCL7PYHNAplI8d2xHHOLF9Qdjyx+ZrQ/RxtqzfyWzhqjsmp2qlgNCxWlt3woS9UhDB+nRvjEoWTJmIOszAMYuj8wGlX+3Ui3ALOdQECgYEA25EqnFPFinUnzgNvB6NYmh5STmZun6s4bUOLqwefKtEvrOtRwTu7sB7NIf37fizG3/MJUWHxiLy2/3ub4d2JxdDNBtJoEqnp6QB12qglCNa4CajdjtJa1dR81F9QvytsqEkmPYXFPPyviB0FcSIDAGMb3IbwvIfzBPY9WY8dJnECgYEAzkg3yKEFBZ8BU0WQ+3hyfKUoAhBEnxouxRSTBcXxwstJRiqaGTVe5aoJGQI+0xS7Z6q07XDtN2t97s6DnRLWbljsX6B64itzNhXRyzjdD3iZDU/KSw7khjhXf8XOZaj9eXmACDiUnkEn1xsM8bLiRGqB8y5f3aMY/RpuACGXnxkCgYEAx/zwT9Vpr1RIfjfYcJ+Su0X0994K0roUukj0tUJK8qf4gcsQ+y1aJe/YLib1ZBaKyj7G9O5+HmqtUAUZld/AdoJZzOXmz2EeYhD+R7wxh1xz4rCBpW3qOKvDS3jJxmZaIOoHv6/RWFxb0WGFrGcrTrX3EaWDLmWxr4pNlP5qsbECgYATllntrBR8/ycyEAX/SuWcHlaZM5BAh0zvm8+GGdCmDYWMqxjs0duL9URd4o+ynWJaKqR5c2KjA4r2tRdcP+Cqo7j2L5fbiAKtnQ7JvEGJaYsm72+nBuf+MrVkRZUepBhFg5r7rNu31zoAO+pTvQetNWvXeozRz93ckrjlPEtYaQKBgQDFwbV92rlRMLjZzlY+o0knoeJBjPQmPdiBTpGNimdy9L4c2Ure7affjcUiYhkKqrK5k5SScJTATgyQ7JF346FdtUtZ/6Kkj1RwJmmprPrDa9CATLoTle7g9OVd4sHT2ITHZMzPaF3ILvzcwJ70AD1xcxCQb+/7sDPmw7Mc8gOA7Q==");
        aliPayConfigStorage.setNotifyUrl("http://pay.egzosn.com/payBack.json");
        aliPayConfigStorage.setReturnUrl("http://pay.egzosn.com/payBack.html");
        aliPayConfigStorage.setSignType(SignUtils.RSA2.name());
        aliPayConfigStorage.setSeller("2088102169916436");
        aliPayConfigStorage.setInputCharset("utf-8");
        //是否为测试账号，沙箱环境
        aliPayConfigStorage.setTest(true);

        //请求连接池配置
        HttpConfigStorage httpConfigStorage = new HttpConfigStorage();
        //最大连接数
        httpConfigStorage.setMaxTotal(20);
        //默认的每个路由的最大连接数
        httpConfigStorage.setDefaultMaxPerRoute(10);
        service = new AliPayService(aliPayConfigStorage, httpConfigStorage);
        //增加支付回调消息拦截器
        service.addPayMessageInterceptor(new AliPayMessageInterceptor());
        //设置回调消息处理
        service.setPayMessageHandler(spring.getBean(AliPayMessageHandler.class));
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
        PayOrder order = new PayOrder("订单title", "摘'要", null == price ? BigDecimal.valueOf(0.01) : price, UUID.randomUUID().toString().replace("-", ""), AliTransactionType.PAGE);
        //WAP
//        PayOrder order = new PayOrder("订单title", "摘要", null == price ? BigDecimal.valueOf(0.01) : price, UUID.randomUUID().toString().replace("-", ""), AliTransactionType.WAP);

//        Map orderInfo = service.orderInfo(order);
//        return service.buildRequest(orderInfo, MethodType.POST);

        return service.toPay(order);
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
        PayOrder order = new PayOrder("订单title", "摘要", BigDecimal.valueOf(0.01), UUID.randomUUID().toString().replace("-", ""));
        //App支付
        order.setTransactionType(AliTransactionType.APP);
        data.put("orderInfo", UriVariables.getMapToParameters(service.app(order)));
        return data;
    }

    /**
     * 获取二维码图像
     * 二维码支付
     *
     * @param price 金额
     * @return 二维码图像
     * @throws IOException IOException
     */
    @RequestMapping(value = "toQrPay.jpg", produces = "image/jpeg;charset=UTF-8")
    public byte[] toQrPay(BigDecimal price) throws IOException {
        //获取对应的支付账户操作工具（可根据账户id）
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(service.genQrPay(new PayOrder("订单title", "摘要", null == price ? BigDecimal.valueOf(0.01) : price, System.currentTimeMillis() + "", AliTransactionType.SWEEPPAY)), "JPEG", baos);
        return baos.toByteArray();
    }

    /**
     * 获取二维码地址
     * 二维码支付
     *
     * @param price 金额
     * @return 二维码图像
     * @throws IOException IOException
     */
    @RequestMapping(value = "getQrPay.json")
    public String getQrPay(BigDecimal price) throws IOException {
        //获取对应的支付账户操作工具（可根据账户id）
        return service.getQrPay(new PayOrder("订单title", "摘要", null == price ? BigDecimal.valueOf(0.01) : price, System.currentTimeMillis() + "", AliTransactionType.SWEEPPAY));
    }


    /**
     * 刷卡付,pos主动扫码付款(条码付)
     *
     * @param authCode 授权码，条码等
     * @param price    金额
     * @return 支付结果
     */
    @RequestMapping(value = "microPay")
    public Map<String, Object> microPay(BigDecimal price, String authCode) {
        //获取对应的支付账户操作工具（可根据账户id）
        //条码付
        PayOrder order = new PayOrder("egan order", "egan order", null == price ? BigDecimal.valueOf(0.01) : price, UUID.randomUUID().toString().replace("-", ""), AliTransactionType.BAR_CODE);
        //声波付
//        PayOrder order = new PayOrder("egan order", "egan order", null == price ? BigDecimal.valueOf(0.01) : price, UUID.randomUUID().toString().replace("-", ""), AliTransactionType.WAVE_CODE);
        //设置授权码，条码等
        order.setAuthCode(authCode);
        //支付结果
        Map<String, Object> params = service.microPay(order);
        //校验
        if (service.verify(new NoticeParams(params))) {

            //支付校验通过后的处理
            //......业务逻辑处理块........


        }
        //这里开发者自行处理
        return params;
    }

    /**
     * 支付回调地址 方式一
     * <p>
     * 方式二，{@link #payBack(HttpServletRequest)} 是属于简化方式， 试用与简单的业务场景
     *
     * @param request 请求
     * @return 返回对应的响应码
     * @throws IOException IOException
     * @see #payBack(HttpServletRequest)
     */
    @Deprecated
    @RequestMapping(value = "payBackBefore.json")
    public String payBackBefore(HttpServletRequest request) throws IOException {

        //获取支付方返回的对应参数
        NoticeParams noticeParams = service.getNoticeParams(new HttpRequestNoticeParams(request));
        if (null == noticeParams) {
            return service.getPayOutMessage("fail", "失败").toMessage();
        }

        //校验
        if (service.verify(noticeParams)) {
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
     * @return 是否成功
     * <p>
     * 业务处理在对应的PayMessageHandler里面处理，在哪里设置PayMessageHandler，详情查看{@link com.egzosn.pay.common.api.PayService#setPayMessageHandler(com.egzosn.pay.common.api.PayMessageHandler)}
     * <p>
     * 如果未设置 {@link com.egzosn.pay.common.api.PayMessageHandler} 那么会使用默认的 {@link com.egzosn.pay.common.api.DefaultPayMessageHandler}
     * @throws IOException IOException
     */
    @Deprecated
    @RequestMapping(value = "payBackOld.json")
    public String payBackOld(HttpServletRequest request) throws IOException {
        //业务处理在对应的PayMessageHandler里面处理，在哪里设置PayMessageHandler，详情查看com.egzosn.pay.common.api.PayService.setPayMessageHandler()
        return service.payBack(request.getParameterMap(), request.getInputStream()).toMessage();
    }

    /**
     * 支付回调地址
     *
     * @param request 请求
     * @return 是否成功
     * <p>
     * 业务处理在对应的PayMessageHandler里面处理，在哪里设置PayMessageHandler，详情查看{@link com.egzosn.pay.common.api.PayService#setPayMessageHandler(com.egzosn.pay.common.api.PayMessageHandler)}
     * <p>
     * 如果未设置 {@link com.egzosn.pay.common.api.PayMessageHandler} 那么会使用默认的 {@link com.egzosn.pay.common.api.DefaultPayMessageHandler}
     * @throws IOException IOException
     */
    @RequestMapping(value = "payBack.json")
    public String payBack(HttpServletRequest request) {
        //业务处理在对应的PayMessageHandler里面处理，在哪里设置PayMessageHandler，详情查看com.egzosn.pay.common.api.PayService.setPayMessageHandler()
        return service.payBack(new HttpRequestNoticeParams(request)).toMessage();
    }

    /**
     * 查询
     *
     * @param order 订单的请求体
     * @return 返回查询回来的结果集，支付方原值返回
     */
    @RequestMapping("query")
    public Map<String, Object> query(QueryOrder order) {
        return service.query(new AssistOrder(order.getTradeNo(), order.getOutTradeNo()));
    }

    /**
     * 统一收单交易结算接口
     *
     * @param order 订单的请求体
     * @return 返回查询回来的结果集，支付方原值返回
     */
    @RequestMapping("settle")
    public Map<String, Object> settle(OrderSettle order) {
       /* OrderSettle order = new OrderSettle();
        order.setTradeNo("支付宝单号");
        order.setOutRequestNo("商户单号");
        order.setAmount(new BigDecimal(100));
        order.setDesc("线下转账");*/
        return service.settle(order);
    }


    /**
     * 交易关闭接口
     *
     * @param order 订单的请求体
     * @return 返回支付方交易关闭后的结果
     */
    @RequestMapping("close")
    public Map<String, Object> close(QueryOrder order) {
        return service.close(new AssistOrder(order.getTradeNo(), order.getOutTradeNo()));
    }

    /**
     * 交易c撤销接口
     *
     * @param order 订单的请求体
     * @return 返回支付方交易关闭后的结果
     */
    @RequestMapping("cancel")
    public Map<String, Object> cancel(QueryOrder order) {
        return service.cancel(order.getTradeNo(), order.getOutTradeNo());
    }

    /**
     * 申请退款接口
     *
     * @param order 订单的请求体
     * @return 返回支付方申请退款后的结果
     */
    @RequestMapping("refund")
    public AliRefundResult refund(RefundOrder order) {
        return service.refund(order);
    }

    /**
     * 查询退款
     *
     * @return 返回支付方查询退款后的结果
     */
    @RequestMapping("refundquery")
    public Map<String, Object> refundquery() {
        RefundOrder order = new RefundOrder();
        order.setOutTradeNo("我方系统商户单号");
        order.setTradeNo("支付宝单号");
        //退款金额
        order.setRefundAmount(new BigDecimal(1));
        order.setRefundNo("退款单号");
        order.setDescription("");
        return service.refundquery(order);
    }

    /**
     * 下载对账单
     *
     * @param order 订单的请求体
     * @return 返回支付方下载对账单的结果
     */
    @RequestMapping("downloadbill")
    public Object downloadBill(QueryOrder order) {
        return service.downloadBill(order.getBillDate(), order.getBillType());
    }


    /**
     * 转账
     *
     * @param order 转账订单
     * @return 对应的转账结果
     */
    @RequestMapping("transfer")
    public Map<String, Object> transfer(AliTransferOrder order) {
        order.setOutBizNo("转账单号");
        order.setTransAmount(new BigDecimal(10));
        order.setOrderTitle("转账业务的标题");
        order.setIdentity("参与方的唯一标识");
        order.setIdentityType("参与方的标识类型，目前支持如下类型：");
        order.setName("参与方真实姓名");
        order.setRemark("转账备注, 非必填");
        //单笔无密转账到支付宝账户
        order.setTransferType(AliTransferType.TRANS_ACCOUNT_NO_PWD);
        //单笔无密转账到银行卡
//        order.setTransferType(AliTransferType.TRANS_BANKCARD_NO_PWD);
        return service.transfer(order);
    }

    /**
     * 转账查询
     *
     * @param outNo   商户转账订单号
     * @param tradeNo 支付平台转账订单号
     * @return 对应的转账订单
     */
    @RequestMapping("transferQuery")
    public Map<String, Object> transferQuery(String outNo, String tradeNo) {
        return service.transferQuery(new AssistOrder(tradeNo, outNo));
    }
}
