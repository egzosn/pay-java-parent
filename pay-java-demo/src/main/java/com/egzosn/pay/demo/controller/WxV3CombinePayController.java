
package com.egzosn.pay.demo.controller;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.egzosn.pay.common.bean.AssistOrder;
import com.egzosn.pay.common.bean.CertStoreType;
import com.egzosn.pay.common.bean.RefundOrder;
import com.egzosn.pay.common.bean.RefundResult;
import com.egzosn.pay.demo.request.QueryOrder;
import com.egzosn.pay.demo.service.handler.WxV3CombinePayMessageHandler;
import com.egzosn.pay.web.support.HttpRequestNoticeParams;
import com.egzosn.pay.wx.v3.api.WxCombinePayService;
import com.egzosn.pay.wx.v3.api.WxPayConfigStorage;
import com.egzosn.pay.wx.v3.bean.WxTransactionType;
import com.egzosn.pay.wx.v3.bean.combine.CombineAmount;
import com.egzosn.pay.wx.v3.bean.combine.CombineCloseOrder;
import com.egzosn.pay.wx.v3.bean.combine.CombinePayOrder;
import com.egzosn.pay.wx.v3.bean.combine.CombineSubOrder;
import com.egzosn.pay.wx.v3.bean.order.H5Info;
import com.egzosn.pay.wx.v3.bean.order.SceneInfo;
import com.egzosn.pay.wx.v3.bean.order.SubOrder;

/**
 * 微信V3合单发起支付入口
 *
 * @author egan
 * email egzosn@gmail.com
 * date 2016/11/18 0:25
 */
@RestController
@RequestMapping("wxV3combine")
public class WxV3CombinePayController {

    private WxCombinePayService service = null;

//    @PostConstruct  //没有证书的情况下注释掉，避免启动报错
    public void init() {
        WxPayConfigStorage wxPayConfigStorage = new WxPayConfigStorage();
        wxPayConfigStorage.setAppId("wxc7b993ff15a9f26c");
        wxPayConfigStorage.setMchId("1602947765");
        //V3密钥 https://pay.weixin.qq.com/wiki/doc/apiv3/wechatpay/wechatpay3_2.shtml
        wxPayConfigStorage.setV3ApiKey("9bd8f0e7af4841299d782406b7774f57");
        wxPayConfigStorage.setNotifyUrl("http://sailinmu.iok.la/wxV3combine/payBack.json");
        wxPayConfigStorage.setReturnUrl("http://sailinmu.iok.la/wxV3combine/payBack.json");
        wxPayConfigStorage.setInputCharset("utf-8");
        //使用证书时设置为true
        wxPayConfigStorage.setCertSign(true);
        //商户API证书 https://pay.weixin.qq.com/wiki/doc/apiv3/wechatpay/wechatpay3_1.shtml
        wxPayConfigStorage.setApiClientKeyP12("yifenli_mall.p12");
        wxPayConfigStorage.setCertStoreType(CertStoreType.PATH);
        service = new WxCombinePayService(wxPayConfigStorage);
        //设置回调消息处理
        //TODO {@link com.egzosn.pay.demo.controller.WxPayController#payBack}
        service.setPayMessageHandler(new WxV3CombinePayMessageHandler());
    }


    /**
     * 跳到支付页面
     * 针对实时支付
     *
     * @return 跳到支付页面
     */
    @RequestMapping(value = "toPay.html", produces = "text/html;charset=UTF-8")
    public String toPay() {
        CombinePayOrder order = new CombinePayOrder();
        SceneInfo sceneInfo = new SceneInfo();
        sceneInfo.setPayerClientIp("用户终端IP ");
        sceneInfo.setDeviceId("终端设备号（门店号或收银设备ID） 。为了方便问题定位，H5支付场景下，该字段必填");
        sceneInfo.setH5Info(new H5Info("场景类型，枚举值：\n" +
                "iOS：IOS移动应用；\n" +
                "Android：安卓移动应用；\n" +
                "Wap：WAP网站应用；"));
        order.setSceneInfo(sceneInfo);
        order.setCombineOutTradeNo("合单商户订单号");
        //子单信息，最多50单.
        List<SubOrder> subOrders = new ArrayList<>();
        SubOrder subOrder = new SubOrder();
        subOrder.setMchid("子单商户号");
        subOrder.setAttach("附加数据，在查询API和支付通知中原样返回，可作为自定义参数使用。 ");
        //"子单金额，单位为分。 "
        subOrder.setAmount(new CombineAmount(121));
        subOrder.setOutTradeNo("子单商户订单号 ");
        subOrder.setDescription("商品描述");
        subOrder.setSubMchid("服务商必填----二级商户商户号，由微信支付生成并下发。服务商子商户的商户号，被合单方。直连商户不用传二级商户号。 ");
        subOrders.add(subOrder);
        order.setSubOrders(subOrders);
        order.setTransactionType(WxTransactionType.COMBINE_H5);
        return service.toPay(order);
    }

    /**
     * 公众号支付,小程序
     *
     * @return 返回jsapi所需参数
     */
    @RequestMapping(value = "jsapi")
    public Map jsapi() {

        CombinePayOrder order = new CombinePayOrder();
        order.setTransactionType(WxTransactionType.COMBINE_JSAPI);
        order.setCombineOutTradeNo("合单商户订单号");
        order.setOpenid("使用合单appid获取的对应用户openid。是用户在商户appid下的唯一标识。 ");
        //子单信息，最多50单.
        List<SubOrder> subOrders = new ArrayList<>();
        SubOrder subOrder = new SubOrder();
        subOrder.setMchid("子单商户号");
        subOrder.setAttach("附加数据，在查询API和支付通知中原样返回，可作为自定义参数使用。 ");
        //"子单金额，单位为分。 "
        subOrder.setAmount(new CombineAmount(111));
        subOrder.setOutTradeNo("子单商户订单号 ");
        subOrder.setDescription("商品描述");
        subOrder.setSubMchid("服务商必填----二级商户商户号，由微信支付生成并下发。服务商子商户的商户号，被合单方。直连商户不用传二级商户号。 ");
        subOrders.add(subOrder);
        order.setSubOrders(subOrders);
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

        CombinePayOrder order = new CombinePayOrder();
        order.setTransactionType(WxTransactionType.COMBINE_APP);
        order.setCombineOutTradeNo("合单商户订单号");
        //子单信息，最多50单.
        List<SubOrder> subOrders = new ArrayList<>();
        SubOrder subOrder = new SubOrder();
        subOrder.setMchid("子单商户号");
        subOrder.setAttach("附加数据，在查询API和支付通知中原样返回，可作为自定义参数使用。 ");
        //"子单金额，单位为分。 "
        subOrder.setAmount(new CombineAmount(211));
        subOrder.setOutTradeNo("子单商户订单号 ");
        subOrder.setDescription("商品描述");
        subOrder.setSubMchid("服务商必填----二级商户商户号，由微信支付生成并下发。服务商子商户的商户号，被合单方。直连商户不用传二级商户号。 ");
        subOrders.add(subOrder);
        order.setSubOrders(subOrders);
        Map orderInfo = service.orderInfo(order);
        orderInfo.put("code", 0);
        return orderInfo;
    }

    /**
     * 获取二维码图像
     * 二维码支付
     *
     * @return 二维码图像
     * @throws IOException IOException
     */
    @RequestMapping(value = "toQrPay.jpg", produces = "image/jpeg;charset=UTF-8")
    public byte[] toWxQrPay() throws IOException {
        CombinePayOrder order = new CombinePayOrder();
        order.setTransactionType(WxTransactionType.COMBINE_NATIVE);
        order.setCombineOutTradeNo("合单商户订单号");
        //子单信息，最多50单.
        List<SubOrder> subOrders = new ArrayList<>();
        SubOrder subOrder = new SubOrder();
        subOrder.setMchid("子单商户号");
        subOrder.setAttach("附加数据，在查询API和支付通知中原样返回，可作为自定义参数使用。 ");
        //"子单金额，单位为分。 "
        subOrder.setAmount(new CombineAmount(131));
        subOrder.setOutTradeNo("子单商户订单号 ");
        subOrder.setDescription("商品描述");
        subOrder.setSubMchid("服务商必填----二级商户商户号，由微信支付生成并下发。服务商子商户的商户号，被合单方。直连商户不用传二级商户号。 ");
        subOrders.add(subOrder);
        order.setSubOrders(subOrders);

        //获取对应的支付账户操作工具（可根据账户id）
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(service.genQrPay(order), "JPEG", baos);
        return baos.toByteArray();
    }

    /**
     * 获取二维码地址
     * 二维码支付
     *
     * @return 二维码图像
     * @throws IOException IOException
     */
    @RequestMapping(value = "getQrPay.json")
    public String getQrPay() {
        CombinePayOrder order = new CombinePayOrder();
        order.setTransactionType(WxTransactionType.COMBINE_NATIVE);
        order.setCombineOutTradeNo("合单商户订单号");
        //子单信息，最多50单.
        List<SubOrder> subOrders = new ArrayList<>();
        SubOrder subOrder = new SubOrder();
        subOrder.setMchid("子单商户号");
        subOrder.setAttach("附加数据，在查询API和支付通知中原样返回，可作为自定义参数使用。1 ");
        //"子单金额，单位为分。 "
        subOrder.setAmount(new CombineAmount(115));
        subOrder.setOutTradeNo("子单商户订单号 ");
        subOrder.setDescription("商品描述");
        subOrder.setSubMchid("服务商必填----二级商户商户号，由微信支付生成并下发。服务商子商户的商户号，被合单方。直连商户不用传二级商户号。 ");
        subOrders.add(subOrder);
        order.setSubOrders(subOrders);
        //获取对应的支付账户操作工具（可根据账户id）
        return service.getQrPay(order);
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
     * @return 返回支付方交易关闭后的结果
     */
    @RequestMapping("close")
    public Map<String, Object> close() {
        CombineCloseOrder order = new CombineCloseOrder();
        order.setOutTradeNo("合单商户订单号");
        //子单信息，最多50单.
        List<CombineSubOrder> subOrders = new ArrayList<>();
        CombineSubOrder subOrder = new CombineSubOrder();
        subOrder.setMchid("子单商户号");
        subOrder.setOutTradeNo("子单商户订单号 ");
        subOrder.setSubMchid("服务商必填----二级商户商户号，由微信支付生成并下发。服务商子商户的商户号，被合单方。直连商户不用传二级商户号。 ");
        subOrders.add(subOrder);
        order.setSubOrders(subOrders);
        return service.close(order);
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


}
