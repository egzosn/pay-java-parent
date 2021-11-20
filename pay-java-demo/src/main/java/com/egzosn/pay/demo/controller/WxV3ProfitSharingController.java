
package com.egzosn.pay.demo.controller;


import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.egzosn.pay.common.bean.AssistOrder;
import com.egzosn.pay.common.bean.CertStoreType;
import com.egzosn.pay.common.bean.RefundResult;
import com.egzosn.pay.demo.request.QueryOrder;
import com.egzosn.pay.demo.service.handler.WxV3ProfitSharingMessageHandler;
import com.egzosn.pay.web.support.HttpRequestNoticeParams;
import com.egzosn.pay.wx.v3.api.WxPayConfigStorage;
import com.egzosn.pay.wx.v3.api.WxProfitSharingService;
import com.egzosn.pay.wx.v3.bean.WxProfitSharingTransactionType;
import com.egzosn.pay.wx.v3.bean.sharing.ProfitSharingBillType;
import com.egzosn.pay.wx.v3.bean.sharing.ProfitSharingOrder;
import com.egzosn.pay.wx.v3.bean.sharing.ProfitSharingReturnOrder;
import com.egzosn.pay.wx.v3.bean.sharing.Receiver;
import com.egzosn.pay.wx.v3.bean.sharing.ReceiverType;
import com.egzosn.pay.wx.v3.bean.sharing.ReceiversOrder;
import com.egzosn.pay.wx.v3.bean.sharing.RelationType;

/**
 * 微信V3分账发起支付入口
 *
 * @author egan
 * email egzosn@gmail.com
 * date 2016/11/18 0:25
 */
@RestController
@RequestMapping("wxV3profitSharing")
public class WxV3ProfitSharingController {

    private WxProfitSharingService service = null;

//    @PostConstruct  //没有证书的情况下注释掉，避免启动报错
    public void init() {
        WxPayConfigStorage wxPayConfigStorage = new WxPayConfigStorage();
        wxPayConfigStorage.setAppId("wxc7b993ff15a9f26c");
        wxPayConfigStorage.setMchId("1602947765");
        //V3密钥 https://pay.weixin.qq.com/wiki/doc/apiv3/wechatpay/wechatpay3_2.shtml
        wxPayConfigStorage.setV3ApiKey("9bd8f0e7af4841299d782406b7774f57");
        wxPayConfigStorage.setNotifyUrl("http://sailinmu.iok.la/wxV3profitSharing/payBack.json");
        wxPayConfigStorage.setReturnUrl("http://sailinmu.iok.la/wxV3profitSharing/payBack.json");
        wxPayConfigStorage.setInputCharset("utf-8");
        //使用证书时设置为true
        wxPayConfigStorage.setCertSign(true);
        //商户API证书 https://pay.weixin.qq.com/wiki/doc/apiv3/wechatpay/wechatpay3_1.shtml
        wxPayConfigStorage.setApiClientKeyP12("yifenli_mall.p12");
        wxPayConfigStorage.setCertStoreType(CertStoreType.PATH);
        service = new WxProfitSharingService(wxPayConfigStorage);
        //设置回调消息处理
        //TODO {@link com.egzosn.pay.demo.controller.WxPayController#payBack}
        service.setPayMessageHandler(new WxV3ProfitSharingMessageHandler());
    }


    /**
     * 请求分账API
     *
     * @return 分账
     */
    @RequestMapping(value = "orders")
    public Map<String, Object> orders() {
        ProfitSharingOrder order = new ProfitSharingOrder();
        order.setTransactionType(WxProfitSharingTransactionType.ORDERS);
        order.setSubMchid("服务商必填----微信支付分配的子商户号，即分账的出资商户号。");
        order.setSubAppid("服务商必填----微信分配的子商户公众账号ID，分账接收方类型包含PERSONAL_SUB_OPENID时必填。");
        order.setTransactionId("微信支付订单号");
        order.setOutOrderNo("务商系统内部的分账单号，在服务商系统内部唯一，同一分账单号多次请求等同一次。只能是数字、大小写字母_-|*@ ");
        List<Receiver> receivers = new ArrayList<>();
        Receiver receiver = new Receiver();
        //1、MERCHANT_ID：商户号
        //2、PERSONAL_OPENID：个人openid（由父商户APPID转换得到）
        //3、PERSONAL_SUB_OPENID: 个人sub_openid（由子商户APPID转换得到）
        //示例值：MERCHANT_ID
        receiver.setType(ReceiverType.MERCHANT_ID);
        receiver.setName("分账个人接收方姓名");
        receiver.setAccount("分账接收方账号");
        //分账金额，单位为分，只能为整数，不能超过原订单支付金额及最大分账比例金额
        receiver.setAmount(1);
        receiver.setDescription("分账的原因描述，分账账单中需要体现");

        order.setReceivers(receivers);
        //1、如果为true，该笔订单剩余未分账的金额会解冻回分账方商户；
        //2、如果为false，该笔订单剩余未分账的金额不会解冻回分账方商户，可以对该笔订单再次进行分账。
        order.setUnfreezeUnsplit(true);
        Map<String, Object> orderInfo = service.orderInfo(order);
        orderInfo.put("code", 0);
        return orderInfo;
    }


    /**
     * 获取支付预订单信息
     *
     * @return 支付预订单信息
     */
    @RequestMapping("unfreeze")
    public Map<String, Object> unfreeze() {
        ProfitSharingOrder order = new ProfitSharingOrder();
        order.setTransactionType(WxProfitSharingTransactionType.ORDERS_UNFREEZE);
        order.setSubMchid("服务商必填----微信支付分配的子商户号，即分账的出资商户号。");
        order.setTransactionId("微信支付订单号");
        order.setOutOrderNo("务商系统内部的分账单号，在服务商系统内部唯一，同一分账单号多次请求等同一次。只能是数字、大小写字母_-|*@ ");
        order.setSubject("分账的原因描述，分账账单中需要体现");
        return  service.orderInfo(order);
    }
    /**
     * 添加分账接收方
     *
     * @return 添加分账接收方
     */
    @RequestMapping("add")
    public Map<String, Object> add() {
        ReceiversOrder order = new ReceiversOrder();
        order.setTransactionType(WxProfitSharingTransactionType.RECEIVERS_ADD);
        order.setSubMchid("服务商必填----微信支付分配的子商户号，即分账的出资商户号。");
        order.setSubAppid("服务商必填----子商户应用ID");
        //分账接收方类型：
        order.setType(ReceiverType.MERCHANT_ID);
        order.setAccount("分账接收方账号");
        order.setName("分账个人接收方姓名");
        //与分账方的关系类型
        order.setRelationType(RelationType.BRAND);
        order.setCustomRelation("自定义的分账关系,子商户与接收方具体的关系，本字段最多10个字。\n" +
                "当字段relation_type的值为CUSTOM时，本字段必填；\n" +
                "当字段relation_type的值不为CUSTOM时，本字段无需填写");
        return  service.orderInfo(order);
    }
    /**
     * 删除分账接收方
     *
     * @return 删除分账接收方
     */
    @RequestMapping("delete")
    public Map<String, Object> delete() {
        ReceiversOrder order = new ReceiversOrder();
        order.setTransactionType(WxProfitSharingTransactionType.RECEIVERS_DELETE);
        order.setSubMchid("服务商必填----微信支付分配的子商户号，即分账的出资商户号。");
        order.setSubAppid("服务商必填----子商户应用ID");
        //分账接收方类型：
        order.setType(ReceiverType.MERCHANT_ID);
        order.setAccount("分账接收方账号");


        return  service.orderInfo(order);
    }


    /**
     * 分账回调地址
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
     * 查询分账结果
     *
     * @return 返回查询回来的结果集，支付方原值返回
     */
    @RequestMapping("query")
    public Map<String, Object> query() {
        AssistOrder assistOrder = new AssistOrder();
        assistOrder.setOutTradeNo("商户分账单号");
        assistOrder.setTradeNo("微信订单号");
        assistOrder.addAttr("sub_mchid", "服务商必填---子商户号");
        return service.query(assistOrder);
    }


    /**
     * 查询剩余待分金额
     *
     * @return 返回查询回来的结果集，支付方原值返回
     */
    @RequestMapping("amounts")
    public Map<String, Object> amounts() {
        AssistOrder assistOrder = new AssistOrder();
        assistOrder.setTradeNo("微信订单号");
        return service.query(assistOrder);
    }


    /**
     * 查询最大分账比例
     * 服务商使用
     * @return 返回查询回来的结果集，支付方原值返回
     */
    @RequestMapping("merchantConfigs")
    public Map<String, Object> merchantConfigs() {
        AssistOrder assistOrder = new AssistOrder();
        assistOrder.addAttr("sub_mchid", "服务商必填---子商户号");
        return service.query(assistOrder);
    }

    /**
     * 请求分账回退
     *
     * @return 返回支付方申请退款后的结果
     */
    @RequestMapping("refund")
    public RefundResult refund() {
        ProfitSharingReturnOrder returnOrder = new ProfitSharingReturnOrder();
        returnOrder.setSubMchid("服务商必填---子商户号");
        returnOrder.setRefundNo("商户回退单号");
        returnOrder.setTradeNo("微信分账单号");
        returnOrder.setOutTradeNo("商户分账单号");
        returnOrder.setRefundAmount(new BigDecimal(1));

        returnOrder.setDescription("分账回退的原因描述");
        return service.refund(returnOrder);
    }

    /**
     * 查询分账回退结果
     *
     * @return 返回支付方查询退款后的结果
     */
    @RequestMapping("refundquery")
    public Map<String, Object> refundquery() {
        ProfitSharingReturnOrder returnOrder = new ProfitSharingReturnOrder();
        returnOrder.setSubMchid("服务商必填---子商户号");
        returnOrder.setRefundNo("商户回退单号");
        returnOrder.setOutTradeNo("商户分账单号");
        return service.refundquery(returnOrder);
    }

    /**
     * 下载对账单
     *
     * @param order 订单的请求体
     * @return 返回支付方下载对账单的结果
     */
    @RequestMapping("downloadbill")
    public Object downloadBill(QueryOrder order) {
        return service.downloadBill(order.getBillDate(), ProfitSharingBillType.GZIP);
    }


}
