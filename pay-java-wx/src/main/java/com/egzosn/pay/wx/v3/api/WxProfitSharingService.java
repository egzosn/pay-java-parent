package com.egzosn.pay.wx.v3.api;

import java.io.InputStream;
import java.util.Date;
import java.util.Map;

import org.apache.http.message.BasicHeader;

import com.alibaba.fastjson.JSONObject;
import com.egzosn.pay.common.bean.AssistOrder;
import com.egzosn.pay.common.bean.BillType;
import com.egzosn.pay.common.bean.MethodType;
import com.egzosn.pay.common.bean.NoticeParams;
import com.egzosn.pay.common.bean.OrderParaStructure;
import com.egzosn.pay.common.bean.PayMessage;
import com.egzosn.pay.common.bean.PayOrder;
import com.egzosn.pay.common.bean.RefundOrder;
import com.egzosn.pay.common.bean.RefundResult;
import com.egzosn.pay.common.bean.TransferOrder;
import com.egzosn.pay.common.bean.result.PayException;
import com.egzosn.pay.common.exception.PayErrorException;
import com.egzosn.pay.common.http.HttpConfigStorage;
import com.egzosn.pay.common.http.HttpStringEntity;
import com.egzosn.pay.common.http.UriVariables;
import com.egzosn.pay.common.util.DateUtils;
import com.egzosn.pay.common.util.MapGen;
import com.egzosn.pay.wx.bean.WxPayError;
import com.egzosn.pay.wx.bean.WxTransferType;
import com.egzosn.pay.wx.v3.bean.WxProfitSharingTransactionType;
import com.egzosn.pay.wx.v3.bean.sharing.ProfitSharingBillType;
import com.egzosn.pay.wx.v3.bean.sharing.ProfitSharingPayMessage;
import com.egzosn.pay.wx.v3.bean.sharing.WxProfitSharingReturnResult;
import com.egzosn.pay.wx.v3.utils.WxConst;

/**
 * 微信分账API服务
 *
 * @author egan
 * <pre>
 * email egan@egzosn.com
 * date 2021/10/6
 * </pre>
 */
public class WxProfitSharingService extends WxPayService implements ProfitSharingService {

    /**
     * 创建支付服务
     *
     * @param payConfigStorage 微信对应的支付配置
     */
    public WxProfitSharingService(WxPayConfigStorage payConfigStorage) {
        super(payConfigStorage);
    }

    /**
     * 创建支付服务
     *
     * @param payConfigStorage 微信对应的支付配置
     * @param configStorage    微信对应的网络配置，包含代理配置、ssl证书配置
     */
    public WxProfitSharingService(WxPayConfigStorage payConfigStorage, HttpConfigStorage configStorage) {
        super(payConfigStorage, configStorage);
    }


    /**
     * 初始化之后执行
     */
    @Override
    protected void initAfter() {
//        new Thread(() -> {
        payConfigStorage.loadCertEnvironment();
        setApiServerUrl(WxConst.URI);
//        }).start();

    }

    /**
     * 回调校验
     *
     * @param params 回调回来的参数集
     * @return 签名校验 true通过
     */
    @Override
    public boolean verify(Map<String, Object> params) {
        throw new PayErrorException(new WxPayError("", "分账不支持方式"));

    }

    /**
     * 验签，使用微信平台证书.
     *
     * @param noticeParams 通知参数
     * @return the boolean
     */
    @Override
    public boolean verify(NoticeParams noticeParams) {
        throw new PayErrorException(new WxPayError("", "分账不支持方式"));
    }


    /**
     * 微信统一下单接口
     *
     * @param order 支付订单集
     * @return 下单结果
     */
    @Override
    public JSONObject unifiedOrder(PayOrder order) {

        Map<String, Object> parameters = new MapGen<String, Object>(WxConst.APPID, payConfigStorage.getAppId())
                .keyValue(WxConst.TRANSACTION_ID, order.getTradeNo())
                .keyValue(WxConst.OUT_ORDER_NO, order.getOutTradeNo())
                .keyValue(WxConst.RECEIVERS, order.getAttr(WxConst.RECEIVERS))
                .keyValue(WxConst.UNFREEZE_UNSPLIT, order.getAttr(WxConst.UNFREEZE_UNSPLIT))
                .getAttr();
        //以下服务商模式必填
        OrderParaStructure.loadParameters(parameters, WxConst.SUB_MCH_ID, order);
        OrderParaStructure.loadParameters(parameters, WxConst.SUB_APPID, order);
        return getAssistService().doExecute(parameters, order);
    }

    /**
     * http 实体 钩子
     *
     * @param entity 实体
     * @return 返回处理后的实体
     */
    @Override
    public HttpStringEntity hookHttpEntity(HttpStringEntity entity) {
        entity.addHeader(new BasicHeader(WxConst.WECHATPAY_SERIAL, payConfigStorage.getCertEnvironment().getPlatformSerialNumber()));
        return entity;
    }

    /**
     * 返回创建的订单信息
     *
     * @param order 支付订单
     * @return 订单信息
     * @see PayOrder 支付订单信息
     */
    @Override
    public Map<String, Object> orderInfo(PayOrder order) {

        if (null == order.getTransactionType()) {
            order.setTransactionType(WxProfitSharingTransactionType.ORDERS);
        }
        switch ((WxProfitSharingTransactionType) order.getTransactionType()) {
            case ORDERS_UNFREEZE:
                return unfreeze(order);
            case RECEIVERS_ADD:
                return add(order);
            case RECEIVERS_DELETE:
                return delete(order);
            default:
                return unifiedOrder(order);
        }
    }


    /**
     * 将请求参数或者请求流转化为 Map
     *
     * @param parameterMap 请求参数
     * @param is           请求流
     * @return 获得回调的请求参数
     */
    @Deprecated
    @Override
    public Map<String, Object> getParameter2Map(Map<String, String[]> parameterMap, InputStream is) {
        throw new PayErrorException(new WxPayError("", "分账不支持方式"));

    }

    /**
     * 获取输出消息，用户返回给支付端, 针对于web端
     *
     * @param orderInfo 发起支付的订单信息
     * @param method    请求方式  "post" "get",
     * @return 获取输出消息，用户返回给支付端, 针对于web端
     * @see MethodType 请求类型
     */
    @Override
    public String buildRequest(Map<String, Object> orderInfo, MethodType method) {
        throw new PayErrorException(new WxPayError("", "分账不支持方式"));
    }

    /**
     * 获取输出二维码信息,
     *
     * @param order 发起支付的订单信息
     * @return 返回二维码信息,，支付时需要的
     */
    @Override
    public String getQrPay(PayOrder order) {
        throw new PayErrorException(new WxPayError("", "分账不支持方式"));
    }

    /**
     * 刷卡付,pos主动扫码付款
     *
     * @param order 发起支付的订单信息
     * @return 返回支付结果
     */
    @Override
    public Map<String, Object> microPay(PayOrder order) {
        throw new PayErrorException(new WxPayError("", "分账不支持方式"));
    }

    /**
     * 查询分账结果API
     * 非服务商模式使用
     *
     * @param transactionId 微信支付平台订单号
     * @param outTradeNo    商户单号
     * @return 返回查询回来的结果集，支付方原值返回
     */
    @Deprecated
    @Override
    public Map<String, Object> query(String transactionId, String outTradeNo) {
        return query(new AssistOrder(transactionId, outTradeNo));
    }

    /**
     * 查询分账结果API
     * <p>
     * 发起分账请求后，可调用此接口查询分账结果
     * <p>
     * 注意： 发起解冻剩余资金请求后，可调用此接口查询解冻剩余资金的结果
     *
     * @param assistOrder 查询条件
     * @return 返回查询回来的结果集，支付方原值返回
     */
    @Override
    public Map<String, Object> query(AssistOrder assistOrder) {
        if (null == assistOrder.getTransactionType()) {
            assistOrder.setTransactionType(WxProfitSharingTransactionType.ORDERS_RESULT);
        }
        switch ((WxProfitSharingTransactionType) assistOrder.getTransactionType()) {
            case AMOUNTS:
                return getAssistService().doExecute("", assistOrder.getTransactionType(), assistOrder.getTradeNo());
            case MCH_CONFIG:
                return getAssistService().doExecute("", assistOrder.getTransactionType(), assistOrder.getAttr(WxConst.SUB_MCH_ID));
            default:
                Map<String, Object> parameters = new MapGen<String, Object>(WxConst.TRANSACTION_ID, assistOrder.getTradeNo()).getAttr();
                //服务商模式使用
                OrderParaStructure.loadParameters(parameters, WxConst.SUB_MCH_ID, assistOrder);
                return getAssistService().doExecute(UriVariables.getMapToParameters(parameters), assistOrder.getTransactionType(), assistOrder.getOutTradeNo());

        }

    }


    /**
     * 交易关闭接口
     *
     * @param transactionId 支付平台订单号
     * @param outTradeNo    商户单号
     * @return 返回支付方交易关闭后的结果
     */
    @Override
    public Map<String, Object> close(String transactionId, String outTradeNo) {
        return close(new AssistOrder(outTradeNo));
    }


    /**
     * 交易关闭接口
     *
     * @param assistOrder 关闭订单
     * @return 返回支付方交易关闭后的结果
     */
    @Override
    public Map<String, Object> close(AssistOrder assistOrder) {
        throw new PayErrorException(new PayException("failure", "V3暂时没有提供此功能，请查看V2版本功能"));
    }

    /**
     * 申请退款接口
     *
     * @param refundOrder 退款订单信息
     * @return 返回支付方申请退款后的结果
     */
    @Override
    public RefundResult refund(RefundOrder refundOrder) {

        Map<String, Object> parameters = new MapGen<String, Object>("return_mchid", payConfigStorage.getMchId())
                .keyValue("out_return_no", refundOrder.getRefundNo())
                .keyValue("amount", refundOrder.getRefundAmount().intValue())
                .keyValue(WxConst.DESCRIPTION, refundOrder.getDescription())
                .getAttr();
        //服务商模式使用
        OrderParaStructure.loadParameters(parameters, WxConst.SUB_MCH_ID, refundOrder);
        OrderParaStructure.loadParameters(parameters, "order_id", refundOrder.getTradeNo());
        OrderParaStructure.loadParameters(parameters, "out_order_no", refundOrder.getOutTradeNo());
        return WxProfitSharingReturnResult.create(getAssistService().doExecute(parameters, WxProfitSharingTransactionType.RETURN_ORDERS));
    }


    /**
     * 查询退款
     *
     * @param refundOrder 退款订单单号信息
     * @return 返回支付方查询退款后的结果
     */
    @Override
    public Map<String, Object> refundquery(RefundOrder refundOrder) {

        Map<String, Object> parameters = new MapGen<String, Object>(WxConst.OUT_ORDER_NO, refundOrder.getOutTradeNo()).getAttr();
        //服务商模式使用
        OrderParaStructure.loadParameters(parameters, WxConst.SUB_MCH_ID, refundOrder);
        String requestBody = UriVariables.getMapToParameters(parameters);
        return getAssistService().doExecute(requestBody, WxProfitSharingTransactionType.RETURN_ORDERS_RESULT, refundOrder.getRefundNo());
    }

    /**
     * 下载对账单
     *
     * @param billDate 账单时间：日账单格式为yyyy-MM-dd，月账单格式为yyyy-MM。
     * @param billType 账单类型 内部自动转化 {@link BillType}
     * @return 返回支付方下载对账单的结果
     */
    @Override
    public Map<String, Object> downloadBill(Date billDate, String billType) {
        BillType wxBillType = ProfitSharingBillType.valueOf(billType);
        return downloadBill(billDate, wxBillType);
    }

    /**
     * 申请分账账单
     * <b>目前不支持指定子商户号查询</b>
     *
     * @param billDate 下载对账单的日期
     * @param billType 账单类型  {@link ProfitSharingBillType}
     * @return 返回支付方下载对账单的结果, 如果【账单类型】为gzip的话则返回值中key为data值为gzip的输入流
     */
    @Override
    public Map<String, Object> downloadBill(Date billDate, BillType billType) {

        Map<String, Object> parameters = new MapGen<String, Object>(WxConst.BILL_DATE, DateUtils.formatDate(billDate, DateUtils.YYYY_MM_DD))
                .getAttr();
        OrderParaStructure.loadParameters(parameters, WxConst.TAR_TYPE, billType.getType());

        return getAssistService().doExecute(UriVariables.getMapToParameters(parameters), WxProfitSharingTransactionType.BILLS);
    }


    /**
     * 转账
     *
     * @param order 转账订单
     * @return 对应的转账结果
     */
    @Override
    public Map<String, Object> transfer(TransferOrder order) {
        throw new PayErrorException(new WxPayError("", "分账不支持方式"));
    }


    /**
     * 转账查询
     *
     * @param outNo          商户转账订单号
     * @param wxTransferType 微信转账类型，.....这里没办法了只能这样写(┬＿┬)，请见谅 {@link WxTransferType}
     *                       <p>
     *                       <a href="https://pay.weixin.qq.com/wiki/doc/api/tools/mch_pay.php?chapter=14_3">企业付款到零钱</a>
     *                       <a href="https://pay.weixin.qq.com/wiki/doc/api/tools/mch_pay.php?chapter=24_3">商户企业付款到银行卡</a>
     *                       </p>
     * @return 对应的转账订单
     */
    @Override
    public Map<String, Object> transferQuery(String outNo, String wxTransferType) {
        throw new PayErrorException(new WxPayError("", "分账不支持方式"));
    }


    /**
     * 创建消息
     *
     * @param message 支付平台返回的消息
     * @return 支付消息对象
     */
    @Override
    public PayMessage createMessage(Map<String, Object> message) {
        return ProfitSharingPayMessage.create(message);
    }


    /**
     * 添加分账接收方
     *
     * @param order 添加分账
     * @return 结果
     */
    @Override
    public Map<String, Object> add(PayOrder order) {
        if (null == order.getTransactionType()) {
            order.setTransactionType(WxProfitSharingTransactionType.RECEIVERS_ADD);
        }
        Map<String, Object> parameters = new MapGen<String, Object>(WxConst.APPID, payConfigStorage.getAppId())
                .getAttr();

        OrderParaStructure.loadParameters(parameters, WxConst.TYPE, order);
        OrderParaStructure.loadParameters(parameters, WxConst.ACCOUNT, order);
        OrderParaStructure.loadParameters(parameters, WxConst.NAME, order);
        OrderParaStructure.loadParameters(parameters, WxConst.RELATION_TYPE, order);
        OrderParaStructure.loadParameters(parameters, WxConst.CUSTOM_RELATION, order);
        //以下服务商模式必填
        OrderParaStructure.loadParameters(parameters, WxConst.SUB_MCH_ID, order);
        OrderParaStructure.loadParameters(parameters, WxConst.SUB_APPID, order);

        return getAssistService().doExecute(parameters, order);
    }

    /**
     * 删除分账接收方
     *
     * @param order 删除分账
     * @return 结果
     */
    @Override
    public Map<String, Object> delete(PayOrder order) {
        if (null == order.getTransactionType()) {
            order.setTransactionType(WxProfitSharingTransactionType.RECEIVERS_DELETE);
        }
        Map<String, Object> parameters = new MapGen<String, Object>(WxConst.APPID, payConfigStorage.getAppId())
                .getAttr();

        OrderParaStructure.loadParameters(parameters, WxConst.TYPE, order);
        OrderParaStructure.loadParameters(parameters, WxConst.ACCOUNT, order);
        //以下服务商模式必填
        OrderParaStructure.loadParameters(parameters, WxConst.SUB_MCH_ID, order);
        OrderParaStructure.loadParameters(parameters, WxConst.SUB_APPID, order);

        return getAssistService().doExecute(parameters, order);
    }

    /**
     * 解冻剩余资金
     *
     * @param order 解冻
     * @return 结果
     */
    @Override
    public Map<String, Object> unfreeze(PayOrder order) {
        if (null == order.getTransactionType()) {
            order.setTransactionType(WxProfitSharingTransactionType.ORDERS_UNFREEZE);
        }
        Map<String, Object> parameters = new MapGen<String, Object>(WxConst.TRANSACTION_ID, order.getTradeNo())
                .keyValue(WxConst.OUT_ORDER_NO, order.getOutTradeNo())
                .getAttr();
        // 商品描述
        OrderParaStructure.loadParameters(parameters, WxConst.DESCRIPTION, order.getSubject());
        OrderParaStructure.loadParameters(parameters, WxConst.DESCRIPTION, order.getBody());
        OrderParaStructure.loadParameters(parameters, WxConst.DESCRIPTION, order);

        //以下服务商模式必填
        OrderParaStructure.loadParameters(parameters, WxConst.SUB_MCH_ID, order);
        return getAssistService().doExecute(parameters, order);
    }
}
