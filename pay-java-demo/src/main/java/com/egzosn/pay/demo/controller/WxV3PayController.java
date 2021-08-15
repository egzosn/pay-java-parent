
package com.egzosn.pay.demo.controller;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.egzosn.pay.common.bean.CertStoreType;
import com.egzosn.pay.common.bean.PayOrder;
import com.egzosn.pay.common.bean.RefundOrder;
import com.egzosn.pay.common.bean.TransferOrder;
import com.egzosn.pay.demo.request.QueryOrder;
import com.egzosn.pay.wx.bean.WxBank;
import com.egzosn.pay.wx.bean.WxTransferType;
import com.egzosn.pay.wx.v3.api.WxPayConfigStorage;
import com.egzosn.pay.wx.v3.api.WxPayService;
import com.egzosn.pay.wx.v3.bean.WxTransactionType;
import com.egzosn.pay.wx.v3.bean.response.WxRefundResult;

/**
 * 发起支付入口
 *
 * @author egan
 * email egzosn@gmail.com
 * date 2016/11/18 0:25
 */
@RestController
@RequestMapping("wxV3")
public class WxV3PayController {

    private WxPayService service = null;





    public WxV3PayController() {


    }

    @PostConstruct
    public void init() {

        WxPayConfigStorage wxPayConfigStorage = new WxPayConfigStorage();
        wxPayConfigStorage.setAppId("wxc7b993ff15a9f27c");
        wxPayConfigStorage.setMchId("1602947766");
//        wxPayConfigStorage.setKeyPublic("转账公钥，转账时必填");
        wxPayConfigStorage.setSecretKey("9bd8f0e7af4841299d782406b7774f56");
        wxPayConfigStorage.setNotifyUrl("https://pay.egzosn.com");
        wxPayConfigStorage.setReturnUrl("https://pay.egzosn.com");
        wxPayConfigStorage.setInputCharset("utf-8");
        wxPayConfigStorage.setCertSign(true);
        wxPayConfigStorage.setApiClientKeyP12("E:\\Documents\\gitee\\pay-java-parent\\pay-java-demo\\src\\main\\resources\\yifenli_mall.p12");
        wxPayConfigStorage.setCertStoreType(CertStoreType.PATH);
        service = new WxPayService(wxPayConfigStorage);
        //设置回调消息处理
        //TODO {@link com.egzosn.pay.demo.controller.WxPayController#payBack}
//        service.setPayMessageHandler(new WxPayMessageHandler(null));
    }


    /**
     * 跳到支付页面
     * 针对实时支付
     *
     * @param request 请求
     * @param price   金额
     * @return 跳到支付页面
     */
    @RequestMapping(value = "toPay.html", produces = "text/html;charset=UTF-8")
    public String toPay(HttpServletRequest request, BigDecimal price) {
        PayOrder order = new PayOrder("订单title", "摘要", null == price ? BigDecimal.valueOf(0.01) : price, UUID.randomUUID().toString().replace("-", ""), WxTransactionType.H5);
        order.setSpbillCreateIp(request.getHeader("X-Real-IP"));
        StringBuffer requestURL = request.getRequestURL();
        //设置网页地址
        order.setWapUrl(requestURL.substring(0, requestURL.indexOf("/") > 0 ? requestURL.indexOf("/") : requestURL.length()));
        //设置网页名称
        order.setWapName("在线充值");

//        Map orderInfo = service.orderInfo(order);
//        return service.buildRequest(orderInfo, MethodType.POST);
        return service.toPay(order);
    }

    /**
     * 公众号支付
     *
     * @param openid openid
     * @param price  金额
     * @return 返回jsapi所需参数
     */
    @RequestMapping(value = "jsapi")
    public Map toPay(String openid, BigDecimal price) {

        PayOrder order = new PayOrder("订单title", "摘要", null == price ? BigDecimal.valueOf(0.01) : price, UUID.randomUUID().toString().replace("-", ""), WxTransactionType.JSAPI);
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
        PayOrder order = new PayOrder("订单title", "摘要", BigDecimal.valueOf(0.01), UUID.randomUUID().toString().replace("-", ""));
        //App支付
        order.setTransactionType(WxTransactionType.APP);
        data.put("orderInfo", service.app(order));
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
    public byte[] toWxQrPay(BigDecimal price) throws IOException {
        //获取对应的支付账户操作工具（可根据账户id）
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(service.genQrPay(new PayOrder("订单title", "摘要", null == price ? BigDecimal.valueOf(0.01) : price, System.currentTimeMillis() + "", WxTransactionType.NATIVE)), "JPEG", baos);
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
        return service.getQrPay(new PayOrder("订单title", "摘要", null == price ? BigDecimal.valueOf(0.01) : price, System.currentTimeMillis() + "", WxTransactionType.NATIVE));
    }



    /**
     * 支付回调地址 方式一
     * <p>
     * 方式二，{@link #payBack(HttpServletRequest)} 是属于简化方式， 试用与简单的业务场景
     *
     * @param request 请求
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
     * @return 是否成功
     * <p>
     * 业务处理在对应的PayMessageHandler里面处理，在哪里设置PayMessageHandler，详情查看{@link com.egzosn.pay.common.api.PayService#setPayMessageHandler(com.egzosn.pay.common.api.PayMessageHandler)}
     * <p>
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
    public WxRefundResult refund(RefundOrder order) {

        return service.refund(order);
    }

    /**
     * 查询退款
     *
     * @param order 订单的请求体
     * @return 返回支付方查询退款后的结果
     */
    @RequestMapping("refundquery")
    public Map<String, Object> refundquery(RefundOrder order) {
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
     * 转账到余额
     *
     * @param order 转账订单
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
     * @param outNo          商户转账订单号
     * @param wxTransferType 微信转账类型，
     *                       .....这里没办法了只能这样写(┬＿┬)，请见谅
     *                       {@link WxTransferType#QUERY_BANK}
     *                       {@link WxTransferType#GETTRANSFERINFO}
     *
     *                       <p>
     *                       <a href="https://pay.weixin.qq.com/wiki/doc/api/tools/mch_pay.php?chapter=14_3">企业付款到零钱</a>
     *                       <a href="https://pay.weixin.qq.com/wiki/doc/api/tools/mch_pay.php?chapter=24_3">商户企业付款到银行卡</a>
     *                       </p>
     * @return 对应的转账订单
     */
    @RequestMapping("transferQuery")
    public Map<String, Object> transferQuery(String outNo, String wxTransferType) {
        //默认查询银行卡的记录 com.egzosn.pay.wx.v3.bean.WxTransferType#QUERY_BANK
        return service.transferQuery(outNo, wxTransferType);
    }


}
