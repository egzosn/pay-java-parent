
package com.egzosn.pay.demo.controller;


import com.egzosn.pay.common.api.Callback;
import com.egzosn.pay.common.api.PayService;
import com.egzosn.pay.common.bean.*;
import com.egzosn.pay.common.http.HttpConfigStorage;
import com.egzosn.pay.common.http.UriVariables;
import com.egzosn.pay.demo.entity.PayType;
import com.egzosn.pay.demo.request.QueryOrder;
import com.egzosn.pay.demo.service.PayResponse;
import com.egzosn.pay.demo.service.handler.AliPayMessageHandler;
import com.egzosn.pay.demo.service.handler.WxPayMessageHandler;
import com.egzosn.pay.wx.api.WxPayConfigStorage;
import com.egzosn.pay.wx.api.WxPayService;
import com.egzosn.pay.wx.bean.WxBank;
import com.egzosn.pay.wx.bean.WxTransactionType;
import com.egzosn.pay.wx.bean.WxTransferType;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
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
 * email egzosn@gmail.com
 * date 2016/11/18 0:25
 */
@RestController
@RequestMapping("wx")
public class WxPayController {

    private PayService service = null;



    //ssl 退款证书相关 不使用可注释
    private static String KEYSTORE = "ssl 退款证书";
    private static String STORE_PASSWORD = "ssl 证书对应的密码， 默认为商户号";

    @PostConstruct
    public void init() {
        WxPayConfigStorage wxPayConfigStorage = new WxPayConfigStorage();
        wxPayConfigStorage.setAppid("公众账号ID");

        wxPayConfigStorage.setMchId("合作者id（商户号）");
        //以下两个参数在 服务商版模式中必填--------
//        wxPayConfigStorage.setSubAppid("子商户公众账号ID ");
//        wxPayConfigStorage.setSubMchId("微信支付分配的子商户号 ");
        //-----------------------------------------------
        wxPayConfigStorage.setKeyPublic("转账公钥，转账时必填");
        wxPayConfigStorage.setSecretKey("密钥");
        wxPayConfigStorage.setNotifyUrl("异步回调地址");
        wxPayConfigStorage.setReturnUrl("同步回调地址");
        wxPayConfigStorage.setSignType("签名方式");
        wxPayConfigStorage.setInputCharset("utf-8");


        service = new WxPayService(wxPayConfigStorage);

        HttpConfigStorage httpConfigStorage = new HttpConfigStorage();

        //ssl 退款证书相关 不使用可注释
        if(!"ssl 退款证书".equals(KEYSTORE)){
            //TODO 这里也支持输入流的入参。
//            httpConfigStorage.setKeystore(WxPayController.class.getResourceAsStream("/证书文件"));
            httpConfigStorage.setKeystore(KEYSTORE);
            httpConfigStorage.setStorePassword(STORE_PASSWORD);
            //设置ssl证书对应的存储方式，这里默认为文件地址
            httpConfigStorage.setCertStoreType(CertStoreType.PATH);
        }


        //请求连接池配置
        //最大连接数
        httpConfigStorage.setMaxTotal(20);
        //默认的每个路由的最大连接数
        httpConfigStorage.setDefaultMaxPerRoute(10);
        service.setRequestTemplateConfigStorage(httpConfigStorage);

        //设置回调消息处理
        //TODO {@link com.egzosn.pay.demo.controller.WxPayController#payBack}
//        service.setPayMessageHandler(new WxPayMessageHandler(null));
    }



    /**
     * 跳到支付页面
     * 针对实时支付
     *
     * @param request       请求
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

//        Map orderInfo = service.orderInfo(order);
//        return service.buildRequest(orderInfo, MethodType.POST);
        return service.toPay(order);
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
     * @throws IOException IOException
     */
    @RequestMapping(value = "toQrPay.jpg", produces = "image/jpeg;charset=UTF-8")
    public byte[] toWxQrPay( BigDecimal price) throws IOException {
        //获取对应的支付账户操作工具（可根据账户id）
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(service.genQrPay( new PayOrder("订单title", "摘要", null == price ? new BigDecimal(0.01) : price, System.currentTimeMillis()+"", WxTransactionType.NATIVE)), "JPEG", baos);
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
        return service.getQrPay( new PayOrder("订单title", "摘要", null == price ? new BigDecimal(0.01) : price, System.currentTimeMillis()+"", WxTransactionType.NATIVE));
    }
    /**
     * 刷卡付,pos主动扫码付款(条码付)
     * @param authCode        授权码，条码等
     * @param price       金额
     * @return 支付结果
     */
    @RequestMapping(value = "microPay")
    public Map<String, Object> microPay( BigDecimal price, String authCode) {
        //获取对应的支付账户操作工具（可根据账户id）
        //条码付
        PayOrder order = new PayOrder("egan order", "egan order", null == price ? new BigDecimal(0.01) : price, UUID.randomUUID().toString().replace("-", ""), WxTransactionType.MICROPAY);
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
     * 刷脸付
     * @param price       金额
     * @param authCode        人脸凭证
     * @param openid        用户在商户 appid下的唯一标识
     * @return 支付结果
     */
    @RequestMapping(value = "facePay")
    public Map<String, Object> facePay(BigDecimal price, String authCode, String openid)  {
        //获取对应的支付账户操作工具（可根据账户id）
        PayOrder order = new PayOrder("egan order", "egan order", null == price ? new BigDecimal(0.01) : price, UUID.randomUUID().toString().replace("-", ""), WxTransactionType.FACEPAY);
        //设置人脸凭证
        order.setAuthCode(authCode);
        //  用户在商户 appid下的唯一标识
        order.setOpenid(openid);
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
     * @throws IOException IOException
     * @see #payBack(HttpServletRequest)
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
     * @return 是否成功
     *
     * 业务处理在对应的PayMessageHandler里面处理，在哪里设置PayMessageHandler，详情查看{@link com.egzosn.pay.common.api.PayService#setPayMessageHandler(com.egzosn.pay.common.api.PayMessageHandler)}
     *
     * 如果未设置 {@link com.egzosn.pay.common.api.PayMessageHandler} 那么会使用默认的 {@link com.egzosn.pay.common.api.DefaultPayMessageHandler}
     * @throws IOException IOException
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
        if("ssl 退款证书".equals(KEYSTORE)){
           throw new RuntimeException("请设置好SSL退款证书");
        }
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
     * 转账到余额
     *
     * @param order 转账订单
     *
     * @return 对应的转账结果
     */
    @RequestMapping("transfer")
    public Map<String, Object> transfer(TransferOrder order) {
        order.setOutNo("partner_trade_no 商户转账订单号");
        order.setPayeeAccount("用户openid");
        order.setPayeeName("收款用户姓名， 非必填，如果填写将强制验证收款人姓名");
        order.setRemark("转账备注, 非必填");
        order.setAmount(new BigDecimal(10));

        //转账到余额，这里默认值是转账到银行卡
        order.setTransferType(WxTransferType.TRANSFERS);

        return service.transfer(order);
    }


    /**
     * 转账到银行卡
     *
     * @param order 转账订单
     *
     * @return 对应的转账结果
     */
    @RequestMapping("transferPayBank")
    public Map<String, Object> transferPayBank(TransferOrder order) {
        order.setOutNo("partner_trade_no 商户转账订单号");
        //采用标准RSA算法，公钥由微信侧提供,将公钥信息配置在PayConfigStorage#setKeyPublic(String)
        order.setPayeeAccount("enc_bank_no 收款方银行卡号");
        order.setPayeeName("收款方用户名");
        order.setBank(WxBank.ABC);
        order.setRemark("转账备注, 非必填");
        order.setAmount(new BigDecimal(10));
        //转账到银行卡，这里默认值是转账到银行卡
        order.setTransferType(WxTransferType.PAY_BANK);

        return service.transfer(order);
    }

    /**
     * 转账查询
     *
     * @param outNo   商户转账订单号
     * @param wxTransferType 微信转账类型，
     *                       .....这里没办法了只能这样写(┬＿┬)，请见谅
     *                       {@link com.egzosn.pay.wx.bean.WxTransferType#QUERY_BANK}
     *                       {@link com.egzosn.pay.wx.bean.WxTransferType#GETTRANSFERINFO}
     *
     * <p>
     *  <a href="https://pay.weixin.qq.com/wiki/doc/api/tools/mch_pay.php?chapter=14_3">企业付款到零钱</a>
     *  <a href="https://pay.weixin.qq.com/wiki/doc/api/tools/mch_pay.php?chapter=24_3">商户企业付款到银行卡</a>
     * </p>
     * @return 对应的转账订单
     */
    @RequestMapping("transferQuery")
    public Map<String, Object> transferQuery(String outNo, String wxTransferType) {
       //默认查询银行卡的记录 com.egzosn.pay.wx.bean.WxTransferType#QUERY_BANK
        return service.transferQuery(outNo, wxTransferType);
    }
}
