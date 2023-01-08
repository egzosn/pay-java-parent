
package com.egzosn.pay.demo.controller;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.egzosn.pay.common.bean.AssistOrder;
import com.egzosn.pay.common.bean.CertStoreType;
import com.egzosn.pay.common.bean.PayOrder;
import com.egzosn.pay.common.bean.RefundOrder;
import com.egzosn.pay.common.bean.RefundResult;
import com.egzosn.pay.demo.request.QueryOrder;
import com.egzosn.pay.demo.service.handler.WxV3PayMessageHandler;
import com.egzosn.pay.web.support.HttpRequestNoticeParams;
import com.egzosn.pay.wx.v3.api.WxPayConfigStorage;
import com.egzosn.pay.wx.v3.api.WxPayService;
import com.egzosn.pay.wx.v3.bean.WxTransactionType;
import com.egzosn.pay.wx.v3.bean.WxTransferType;
import com.egzosn.pay.wx.v3.bean.order.H5Info;
import com.egzosn.pay.wx.v3.bean.order.SceneInfo;
import com.egzosn.pay.wx.v3.bean.transfer.TransferDetail;
import com.egzosn.pay.wx.v3.bean.transfer.WxTransferOrder;
import com.egzosn.pay.wx.v3.bean.transfer.WxTransferQueryOrder;
import com.egzosn.pay.wx.v3.utils.WxConst;

/**
 * 微信V3发起支付入口
 *
 * @author egan
 * email egzosn@gmail.com
 * date 2016/11/18 0:25
 */
@RestController
@RequestMapping("wxV3")
public class WxV3PayController {

    private WxPayService service = null;


//    @PostConstruct  //没有证书的情况下注释掉，避免启动报错
    public void init() {
        WxPayConfigStorage wxPayConfigStorage = new WxPayConfigStorage();
        wxPayConfigStorage.setAppId("wxc7b993ff15a9f26c");
        wxPayConfigStorage.setMchId("1602947765");
        //V3密钥 https://pay.weixin.qq.com/wiki/doc/apiv3/wechatpay/wechatpay3_2.shtml
        wxPayConfigStorage.setV3ApiKey("9bd8f0e7af4841299d782406b7774f57");
        wxPayConfigStorage.setNotifyUrl("http://sailinmu.iok.la/wxV3/payBack.json");
        wxPayConfigStorage.setReturnUrl("http://sailinmu.iok.la/wxV3/payBack.json");
        wxPayConfigStorage.setInputCharset("utf-8");
        //使用证书时设置为true
//        wxPayConfigStorage.setCertSign(true);
        //商户API证书 https://pay.weixin.qq.com/wiki/doc/apiv3/wechatpay/wechatpay3_1.shtml
        wxPayConfigStorage.setApiClientKeyP12("yifenli_mall.p12");
        wxPayConfigStorage.setCertStoreType(CertStoreType.PATH);
        service = new WxPayService(wxPayConfigStorage);
        //微信海外支付：东南亚
//        service.setApiServerUrl("https://apihk.mch.weixin.qq.com");
        //设置回调消息处理
        //TODO {@link com.egzosn.pay.demo.controller.WxPayController#payBack}
        service.setPayMessageHandler(new WxV3PayMessageHandler());
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
        StringBuffer requestURL = request.getRequestURL();
        SceneInfo sceneInfo = new SceneInfo();
        sceneInfo.setPayerClientIp(request.getHeader("X-Real-IP"));
        sceneInfo.setH5Info(new H5Info("在线充值", requestURL.substring(0, requestURL.indexOf("/") > 0 ? requestURL.indexOf("/") : requestURL.length())));
        order.addAttr(WxConst.SCENE_INFO, sceneInfo);

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
        ImageIO.write(service.genQrPay(new PayOrder("测试商品", "测试商品", null == price ? BigDecimal.valueOf(0.01) : price, UUID.randomUUID().toString().replace("-", ""), WxTransactionType.NATIVE)), "JPEG", baos);
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
     * 申请退款接口
     *
     * @param order 订单的请求体
     * @return 返回支付方申请退款后的结果
     */
    @RequestMapping("refund")
    public RefundResult refund(RefundOrder order) {

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
     *
     * @return 对应的转账结果
     */
    @RequestMapping("transfer")
    public Map<String, Object> transfer() {

        WxTransferOrder order = new WxTransferOrder();
        order.setOutBatchNo("商户系统内部的商家批次单号，要求此参数只能由数字、大小写字母组成，在商户系统内部唯一");
        order.setBatchName("该笔批量转账的名称");
        order.setBatchRemark("转账说明，UTF8编码，最多允许32个字符");
        // 转账金额单位为“分”。转账总金额必须与批次内所有明细转账金额之和保持一致，否则无法发起转账操作
        order.setTotalAmount(100);
        //一个转账批次单最多发起一千笔转账。转账总笔数必须与批次内所有明细之和保持一致，否则无法发起转账操作
        order.setTotalNum(1);
        TransferDetail transferDetail = new TransferDetail();
        transferDetail.setOutDetailNo("商户系统内部区分转账批次单下不同转账明细单的唯一标识，要求此参数只能由数字、大小写字母组成");
        // 转账金额单位为“分”。转账总金额必须与批次内所有明细转账金额之和保持一致，否则无法发起转账操作
        transferDetail.setTransferAmount(100);
        transferDetail.setTransferRemark("单条转账备注（微信用户会收到该备注），UTF8编码，最多允许32个字符");
        transferDetail.setOpenid("商户appid下，某用户的openid");
        transferDetail.setUserName("收款方真实姓名: 张三");
        transferDetail.setUserIdCard("当填入收款方身份证号时，姓名字段必须填入。");
        order.setTransferDetailList(Collections.singletonList(transferDetail));
        //发起商家转账,转账到零钱
        order.setTransferType(WxTransferType.TRANSFER_BATCHES);
        order.setTransferSceneId("必填，指定该笔转账使用的转账场景ID");
        return service.transfer(order);
    }
    /**
     * 转账账单电子回单申请受理接口
     *
     *
     * @return 转账账单电子回单申请受理接口结果
     */
    @RequestMapping("billReceipt")
    public Map<String, Object> billReceipt() {
        WxTransferOrder order = new WxTransferOrder();
        order.setOutBatchNo("商户系统内部的商家批次单号，要求此参数只能由数字、大小写字母组成，在商户系统内部唯一");
        //转账账单电子回单申请受理接口
        order.setTransferType(WxTransferType.TRANSFER_BILL_RECEIPT);
        return service.transfer(order);
    }


    /**
     * 通过微信批次单号查询批次单
     *
     * <p>
     *  <a href="https://pay.weixin.qq.com/docs/merchant/apis/batch-transfer-to-balance/transfer-batch/get-transfer-batch-by-no.html">通过微信批次单号查询批次单</a>
     * </p>
     * @return 对应的转账订单
     */
    @RequestMapping("getTransferBatchByNo")
    public Map<String, Object> getTransferBatchByNo() {
        WxTransferQueryOrder queryOrder = new WxTransferQueryOrder();
        queryOrder.setBatchId("1030000071100999991182020050700019480001");
        queryOrder.setNeedQueryDetail(true);
        queryOrder.setOffset(0);
        queryOrder.setLimit(20);
        queryOrder.setDetailStatus("FAIL");
        queryOrder.setTransactionType(WxTransferType.QUERY_BATCH_BY_BATCH_ID);
        return service.transferQuery(queryOrder);
    }
    /**
     * 通过微信批次单号查询批次单
     *
     * <p>
     *  <a href="https://pay.weixin.qq.com/docs/merchant/apis/batch-transfer-to-balance/transfer-batch/get-transfer-batch-by-out-no.html">通过商家批次单号查询批次单</a>
     * </p>
     * @return 对应的转账订单
     */
    @RequestMapping("getTransferBatchByOutNo")
    public Map<String, Object> getTransferBatchByOutNo() {
        WxTransferQueryOrder queryOrder = new WxTransferQueryOrder();
        queryOrder.setOutBatchNo("1030000071100999991182020050700019480001");
        queryOrder.setNeedQueryDetail(true);
        queryOrder.setOffset(0);
        queryOrder.setLimit(20);
        queryOrder.setDetailStatus("FAIL");
        queryOrder.setTransactionType(WxTransferType.QUERY_BATCH_BY_OUT_BATCH_NO);
        return service.transferQuery(queryOrder);
    }
    /**
     * 通过微信明细单号查询明细单
     *
     * <p>
     *  <a href="https://pay.weixin.qq.com/docs/merchant/apis/batch-transfer-to-balance/transfer-detail/get-transfer-detail-by-no.html">通过微信明细单号查询明细单</a>
     * </p>
     * @return 对应的转账订单
     */
    @RequestMapping("getTransferDetailByNo")
    public Map<String, Object> getTransferDetailByNo() {
        WxTransferQueryOrder queryOrder = new WxTransferQueryOrder();
        queryOrder.setBatchId("1030000071100999991182020050700019480001");
        queryOrder.setDetailId("1040000071100999991182020050700019500100");
        queryOrder.setTransactionType(WxTransferType.QUERY_BATCH_DETAIL_BY_BATCH_ID);
        return service.transferQuery(queryOrder);
    }
    /**
     * 通过商家明细单号查询明细单
     *
     * <p>
     *  <a href="https://pay.weixin.qq.com/docs/merchant/apis/batch-transfer-to-balance/transfer-detail/get-transfer-detail-by-out-no.html">通过商家明细单号查询明细单</a>
     * </p>
     * @return 对应的转账订单
     */
    @RequestMapping("getTransferDetailByOutNo")
    public Map<String, Object> getTransferDetailByOutNo() {
        WxTransferQueryOrder queryOrder = new WxTransferQueryOrder();
        queryOrder.setOutDetailNo("x23zy545Bd5436");
        queryOrder.setOutBatchNo("plfk2020042013");
        queryOrder.setTransactionType(WxTransferType.QUERY_BATCH_DETAIL_BY_OUT_BATCH_NO);
        return service.transferQuery(queryOrder);
    }
    /**
     * 查询转账账单电子回单接口
     *
     * <p>
     *  <a href="https://pay.weixin.qq.com/docs/merchant/apis/batch-transfer-to-balance/transfer-detail/get-transfer-detail-by-out-no.html">查询转账账单电子回单接口</a>
     * </p>
     * @return 对应的转账订单
     */
    @RequestMapping("getElectronicSignatureByOutNo")
    public Map<String, Object> getElectronicSignatureByOutNo() {
        WxTransferQueryOrder queryOrder = new WxTransferQueryOrder();
        queryOrder.setOutBatchNo("plfk2020042013");
        queryOrder.setTransactionType(WxTransferType.QUERY_TRANSFER_BILL_RECEIPT);
        return service.transferQuery(queryOrder);
    }

}
