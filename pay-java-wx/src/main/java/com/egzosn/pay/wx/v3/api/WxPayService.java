package com.egzosn.pay.wx.v3.api;

import java.io.IOException;
import java.io.InputStream;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.http.HttpEntity;

import static com.egzosn.pay.wx.api.WxConst.OUT_TRADE_NO;
import static com.egzosn.pay.wx.api.WxConst.SANDBOXNEW;
import static com.egzosn.pay.wx.v3.utils.WxConst.FAILURE;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.egzosn.pay.common.api.BasePayService;
import com.egzosn.pay.common.api.TransferService;
import com.egzosn.pay.common.bean.AssistOrder;
import com.egzosn.pay.common.bean.BillType;
import com.egzosn.pay.common.bean.CurType;
import com.egzosn.pay.common.bean.MethodType;
import com.egzosn.pay.common.bean.NoticeParams;
import com.egzosn.pay.common.bean.NoticeRequest;
import com.egzosn.pay.common.bean.OrderParaStructure;
import com.egzosn.pay.common.bean.PayMessage;
import com.egzosn.pay.common.bean.PayOrder;
import com.egzosn.pay.common.bean.PayOutMessage;
import com.egzosn.pay.common.bean.RefundOrder;
import com.egzosn.pay.common.bean.RefundResult;
import com.egzosn.pay.common.bean.TransactionType;
import com.egzosn.pay.common.bean.TransferOrder;
import com.egzosn.pay.common.bean.result.PayException;
import com.egzosn.pay.common.exception.PayErrorException;
import com.egzosn.pay.common.http.HttpConfigStorage;
import com.egzosn.pay.common.http.ResponseEntity;
import com.egzosn.pay.common.http.UriVariables;
import com.egzosn.pay.common.util.DateUtils;
import com.egzosn.pay.common.util.IOUtils;
import com.egzosn.pay.common.util.MapGen;
import com.egzosn.pay.common.util.Util;
import com.egzosn.pay.common.util.sign.SignTextUtils;
import com.egzosn.pay.common.util.sign.SignUtils;
import com.egzosn.pay.common.util.sign.encrypt.RSA2;
import com.egzosn.pay.common.util.str.StringUtils;
import com.egzosn.pay.wx.bean.WxPayError;
import com.egzosn.pay.wx.v3.bean.WxAccountType;
import com.egzosn.pay.wx.v3.bean.WxBillType;
import com.egzosn.pay.wx.v3.bean.WxTransactionType;
import com.egzosn.pay.wx.v3.bean.WxTransferType;
import com.egzosn.pay.wx.v3.bean.order.Amount;
import com.egzosn.pay.wx.v3.bean.order.RefundAmount;
import com.egzosn.pay.wx.v3.bean.response.Resource;
import com.egzosn.pay.wx.v3.bean.response.WxNoticeParams;
import com.egzosn.pay.wx.v3.bean.response.WxPayMessage;
import com.egzosn.pay.wx.v3.bean.response.WxRefundResult;
import com.egzosn.pay.wx.v3.bean.transfer.TransferDetail;
import com.egzosn.pay.wx.v3.utils.AntCertificationUtil;
import com.egzosn.pay.wx.v3.utils.WxConst;

/**
 * 微信支付服务
 *
 * @author egan
 * <pre>
 * email egan@egzosn.com
 * date 2021/10/6
 * </pre>
 */
public class WxPayService extends BasePayService<WxPayConfigStorage> implements TransferService, WxPayServiceInf {


    /**
     * api服务地址，默认为国内
     */
    private String apiServerUrl = WxConst.URI;


    /**
     * 辅助api
     */
    private volatile WxPayAssistService assistService;

    /**
     * 微信参数构造器
     */
    private volatile WxParameterStructure wxParameterStructure;


    /**
     * 创建支付服务
     *
     * @param payConfigStorage 微信对应的支付配置
     */
    public WxPayService(WxPayConfigStorage payConfigStorage) {
        this(payConfigStorage, null);
    }

    /**
     * 创建支付服务
     *
     * @param payConfigStorage 微信对应的支付配置
     * @param configStorage    微信对应的网络配置，包含代理配置、ssl证书配置
     */
    public WxPayService(WxPayConfigStorage payConfigStorage, HttpConfigStorage configStorage) {
        super(payConfigStorage, configStorage);
    }

    {
        initAfter();
    }

    /**
     * 辅助api
     *
     * @return 辅助api
     */
    @Override
    public WxPayAssistService getAssistService() {
        if (null == assistService) {
            assistService = new DefaultWxPayAssistService(this);
        }
        //在这预先进行初始化
        assistService.refreshCertificate();
        return assistService;
    }

    public void setAssistService(WxPayAssistService assistService) {
        this.assistService = assistService;
    }

    /**
     * 初始化之后执行
     */
    protected void initAfter() {
        payConfigStorage.setPartner(StringUtils.isNotEmpty(payConfigStorage.getSubMchId()));
//        new Thread(() -> {
        payConfigStorage.loadCertEnvironment();
        wxParameterStructure = new WxParameterStructure(payConfigStorage);
        setApiServerUrl(WxConst.URI);
//        }).start();

    }

    /**
     * 设置api服务器地址
     *
     * @param apiServerUrl api服务器地址
     * @return 自身
     */
    @Override
    public WxPayService setApiServerUrl(String apiServerUrl) {
        this.apiServerUrl = apiServerUrl;
        getAssistService();
        return this;
    }

    @Override
    public String getApiServerUrl() {
        return apiServerUrl;
    }

    public WxParameterStructure getWxParameterStructure() {
        return wxParameterStructure;
    }

    /**
     * 根据交易类型获取url
     *
     * @param transactionType 交易类型
     * @return 请求url
     */
    @Override
    public String getReqUrl(TransactionType transactionType) {
        String type = transactionType.getType();
        String partnerStr = "";
        if (payConfigStorage.isPartner()) {
            partnerStr = "/partner";
        }
        type = type.replace("{partner}", partnerStr);
        return apiServerUrl + (payConfigStorage.isTest() ? SANDBOXNEW : "") + type;
    }


    /**
     * 回调校验
     *
     * @param params 回调回来的参数集
     * @return 签名校验 true通过
     */
    @Override
    public boolean verify(Map<String, Object> params) {
        throw new PayErrorException(new WxPayError("", "微信V3不支持方式"));

    }

    /**
     * 验签，使用微信平台证书.
     *
     * @param noticeParams 通知参数
     * @return the boolean
     */
    @Override
    public boolean verify(NoticeParams noticeParams) {

        //当前使用的微信平台证书序列号
        String serial = noticeParams.getHeader("wechatpay-serial");
        //微信服务器的时间戳
        String timestamp = noticeParams.getHeader("wechatpay-timestamp");
        //微信服务器提供的随机串
        String nonce = noticeParams.getHeader("wechatpay-nonce");
        //微信平台签名
        String signature = noticeParams.getHeader("wechatpay-signature");

        Certificate certificate = getAssistService().getCertificate(serial);


        //这里为微信回调时的请求内容体，原值数据
        String body = noticeParams.getBodyStr();
        //签名信息
        String signText = StringUtils.joining("\n", timestamp, nonce, body);

        return RSA2.verify(signText, signature, certificate, payConfigStorage.getInputCharset());
    }


    /**
     * 微信统一下单接口
     *
     * @param order 支付订单集
     * @return 下单结果
     */
    public JSONObject unifiedOrder(PayOrder order) {

        //统一下单
        Map<String, Object> parameters = wxParameterStructure.initPartner(null);
//        wxParameterStructure.getPublicParameters(parameters);
        // 商品描述
        OrderParaStructure.loadParameters(parameters, WxConst.DESCRIPTION, order.getSubject());
        OrderParaStructure.loadParameters(parameters, WxConst.DESCRIPTION, order.getBody());
        // 订单号
        parameters.put(WxConst.OUT_TRADE_NO, order.getOutTradeNo());
        //交易结束时间
        if (null != order.getExpirationTime()) {
            parameters.put("time_expire", DateUtils.formatDate(order.getExpirationTime(), DateUtils.YYYY_MM_DD_T_HH_MM_SS_XX));
        }
        OrderParaStructure.loadParameters(parameters, "attach", order.getAddition());
        wxParameterStructure.initNotifyUrl(parameters, order);
        //订单优惠标记
        OrderParaStructure.loadParameters(parameters, "goods_tag", order);
        parameters.put("amount", Amount.getAmount(order.getPrice(), order.getCurType()));

        //优惠功能
        OrderParaStructure.loadParameters(parameters, "detail", order);
        //支付场景描述
        OrderParaStructure.loadParameters(parameters, WxConst.SCENE_INFO, order);
        wxParameterStructure.loadSettleInfo(parameters, order);

        TransactionType transactionType = order.getTransactionType();
        ((WxTransactionType) transactionType).setAttribute(parameters, order);
        // 订单附加信息，可用于预设未提供的参数，这里会覆盖以上所有的订单信息，
        parameters.putAll(order.getAttrs());
        return getAssistService().doExecute(parameters, order);
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

        ////统一下单
        JSONObject result = unifiedOrder(order);
        //如果是扫码支付或者刷卡付无需处理，直接返回
        if (((WxTransactionType) order.getTransactionType()).isReturn()) {
            return result;
        }

        Map<String, Object> params = new LinkedHashMap<>();
        String appId = payConfigStorage.getAppId();
        if (payConfigStorage.isPartner() && StringUtils.isNotEmpty(payConfigStorage.getSubAppId())) {
            appId = payConfigStorage.getSubAppId();
        }
        String timeStamp = String.valueOf(DateUtils.toEpochSecond());
        String randomStr = SignTextUtils.randomStr();
        String prepayId = result.getString("prepay_id");
        if (WxTransactionType.JSAPI == order.getTransactionType()) {
            params.put("appId", appId);
            params.put("timeStamp", timeStamp);
            params.put("nonceStr", randomStr);
            prepayId = "prepay_id=" + prepayId;
            params.put("package", prepayId);
            params.put("signType", SignUtils.RSA.getName());
        }
        else if (WxTransactionType.APP == order.getTransactionType()) {
            params.put(WxConst.APPID, appId);
            params.put("partnerid", payConfigStorage.getMchId());
            params.put("timestamp", timeStamp);
            params.put("noncestr", randomStr);
            params.put("prepayid", prepayId);
            params.put("package", "Sign=WXPay");
        }
        String signText = StringUtils.joining("\n", appId, timeStamp, randomStr, prepayId);
        String paySign = createSign(signText, payConfigStorage.getInputCharset());
        params.put(WxTransactionType.JSAPI.equals(order.getTransactionType()) ? "paySign" : "sign", paySign);
        return params;

    }


    /**
     * 签名
     *
     * @param content           需要签名的内容 不包含key
     * @param characterEncoding 字符编码
     * @return 签名结果
     */
    @Override
    public String createSign(String content, String characterEncoding) {
        PrivateKey privateKey = payConfigStorage.getCertEnvironment().getPrivateKey();
        return RSA2.sign(content, privateKey, characterEncoding);
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
        throw new PayErrorException(new WxPayError(FAILURE, "微信V3不支持方式"));

    }

    /**
     * 将请求参数或者请求流转化为 Map
     *
     * @param request 通知请求
     * @return 获得回调的请求参数
     */
    @Override
    public NoticeParams getNoticeParams(NoticeRequest request) {
        WxNoticeParams noticeParams = null;
        try (InputStream is = request.getInputStream()) {
            String body = IOUtils.toString(is);
            noticeParams = JSON.parseObject(body, WxNoticeParams.class);
            noticeParams.setAttr(new MapGen<String, Object>(WxConst.RESP_BODY, body).getAttr());
            noticeParams.setBodyStr(body);
            Resource resource = noticeParams.getResource();
            String associatedData = resource.getAssociatedData();
            String nonce = resource.getNonce();
            String ciphertext = resource.getCiphertext();
            String data = AntCertificationUtil.decryptToString(associatedData, nonce, ciphertext, payConfigStorage.getV3ApiKey(), payConfigStorage.getInputCharset());
            noticeParams.setBody(JSON.parseObject(data));
        }
        catch (IOException e) {
            throw new PayErrorException(new WxPayError(FAILURE, "获取回调参数异常"), e);
        }
        Map<String, List<String>> headers = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            headers.put(name, Collections.list(request.getHeaders(name)));
        }
        noticeParams.setHeaders(headers);
        return noticeParams;
    }

    /**
     * 获取输出消息，用户返回给支付端
     *
     * @param code    状态
     * @param message 消息
     * @return 返回输出消息
     */
    @Override
    public PayOutMessage getPayOutMessage(String code, String message) {
        return PayOutMessage.JSON().content("code", code).content("message", message).build();
    }


    /**
     * 获取成功输出消息，用户返回给支付端
     * 主要用于拦截器中返回
     *
     * @param payMessage 支付回调消息
     * @return 返回输出消息
     */
    @Override
    public PayOutMessage successPayOutMessage(PayMessage payMessage) {
        return getPayOutMessage("SUCCESS", "成功");
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
        String redirectUrl = StringUtils.isEmpty(payConfigStorage.getReturnUrl()) ? "" : "&redirect_url=" + UriVariables.urlEncoder(payConfigStorage.getReturnUrl());
        return String.format("<script type=\"text/javascript\">location.href=\"%s%s\"</script>", orderInfo.get("h5_url"), redirectUrl);
    }

    /**
     * 获取输出二维码信息,
     *
     * @param order 发起支付的订单信息
     * @return 返回二维码信息,，支付时需要的
     */
    @Override
    public String getQrPay(PayOrder order) {
        order.setTransactionType(WxTransactionType.NATIVE);
        Map<String, Object> orderInfo = orderInfo(order);

        return (String) orderInfo.get("code_url");
    }

    /**
     * 刷卡付,pos主动扫码付款
     *
     * @param order 发起支付的订单信息
     * @return 返回支付结果
     */
    @Override
    public Map<String, Object> microPay(PayOrder order) {
        throw new PayErrorException(new PayException("failure", "V3暂时没有提供此功能，请查看V2版本功能"));
    }

    /**
     * 交易查询接口
     *
     * @param transactionId 微信支付平台订单号
     * @param outTradeNo    商户单号
     * @return 返回查询回来的结果集，支付方原值返回
     */
    @Override
    public Map<String, Object> query(String transactionId, String outTradeNo) {
        return query(new AssistOrder(transactionId, outTradeNo));
    }

    /**
     * 交易查询接口
     *
     * @param assistOrder 查询条件
     * @return 返回查询回来的结果集，支付方原值返回
     */
    @Override
    public Map<String, Object> query(AssistOrder assistOrder) {
        String transactionId = assistOrder.getTradeNo();
        String outTradeNo = assistOrder.getOutTradeNo();
        String parameters = UriVariables.getMapToParameters(wxParameterStructure.getMchParameters());
        WxTransactionType transactionType = WxTransactionType.QUERY_TRANSACTION_ID;
        String uriVariable = transactionId;
        if (StringUtils.isNotEmpty(outTradeNo)) {
            transactionType = WxTransactionType.QUERY_OUT_TRADE_NO;
            uriVariable = outTradeNo;
        }

        return getAssistService().doExecute(parameters, transactionType, uriVariable);

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
        String parameters = JSON.toJSONString(wxParameterStructure.getMchParameters());
        final ResponseEntity<JSONObject> responseEntity = getAssistService().doExecuteEntity(parameters, WxTransactionType.CLOSE, assistOrder.getOutTradeNo());
        if (responseEntity.getStatusCode() == 204) {
            return new MapGen<String, Object>("statusCode", responseEntity.getStatusCode()).getAttr();
        }
        return responseEntity.getBody();
    }


    /**
     * 申请退款接口
     *
     * @param refundOrder 退款订单信息
     * @return 返回支付方申请退款后的结果
     */
    @Override
    public RefundResult refund(RefundOrder refundOrder) {
        //获取公共参数
        Map<String, Object> parameters = wxParameterStructure.initSubMchId(null);

        OrderParaStructure.loadParameters(parameters, "transaction_id", refundOrder.getTradeNo());
        OrderParaStructure.loadParameters(parameters, OUT_TRADE_NO, refundOrder.getOutTradeNo());
        OrderParaStructure.loadParameters(parameters, "out_refund_no", refundOrder.getRefundNo());
        OrderParaStructure.loadParameters(parameters, "reason", refundOrder.getDescription());
        OrderParaStructure.loadParameters(parameters, "funds_account", refundOrder);
        wxParameterStructure.initNotifyUrl(parameters, refundOrder);
        RefundAmount refundAmount = new RefundAmount();
        refundAmount.setRefund(Util.conversionCentAmount(refundOrder.getRefundAmount()));
        refundAmount.setTotal(Util.conversionCentAmount(refundOrder.getTotalAmount()));
        CurType curType = refundOrder.getCurType();
        if (null != curType) {
            refundAmount.setCurrency(curType.getType());
        }
        parameters.put("amount", refundAmount);
        OrderParaStructure.loadParameters(parameters, "amount", refundOrder);
        return WxRefundResult.create(getAssistService().doExecute(parameters, WxTransactionType.REFUND));
    }


    /**
     * 查询退款
     *
     * @param refundOrder 退款订单单号信息
     * @return 返回支付方查询退款后的结果
     */
    @Override
    public Map<String, Object> refundquery(RefundOrder refundOrder) {
        String parameters = UriVariables.getMapToParameters(wxParameterStructure.initSubMchId(null));
        return getAssistService().doExecute(parameters, WxTransactionType.REFUND_QUERY, refundOrder.getRefundNo());
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
        BillType wxBillType = WxBillType.forType(billType);
        if (null == wxBillType) {
            wxBillType = WxAccountType.forType(billType);
        }
        return downloadBill(billDate, wxBillType);
    }

    /**
     * 目前只支持
     * 对账单中涉及金额的字段单位为“元”。
     *
     * @param billDate 下载对账单的日期，格式：20140603
     * @param billType 账单类型 {@link WxBillType} 与 {@link WxAccountType}
     * @return 返回支付方下载对账单的结果, 如果【账单类型】为gzip的话则返回值中key为data值为gzip的输入流
     */
    @Override
    public Map<String, Object> downloadBill(Date billDate, BillType billType) {
        //获取公共参数
        Map<String, Object> parameters = new HashMap<>(5);

        //目前只支持日账单
        parameters.put(WxConst.BILL_DATE, DateUtils.formatDate(billDate, DateUtils.YYYY_MM_DD));
        String fileType = billType.getFileType();
        OrderParaStructure.loadParameters(parameters, "tar_type", fileType);
        if (billType instanceof WxAccountType) {
            OrderParaStructure.loadParameters(parameters, "account_type", billType.getType());
        }
        else {
            wxParameterStructure.initSubMchId(parameters).put("bill_type", billType.getType());
        }
        String body = UriVariables.getMapToParameters(parameters);
        JSONObject result = getAssistService().doExecute(body, WxTransactionType.valueOf(billType.getCustom()));
        String downloadUrl = result.getString("download_url");
        MethodType methodType = MethodType.GET;
        HttpEntity entity = getAssistService().buildHttpEntity(downloadUrl, "", methodType.name());
        ResponseEntity<InputStream> responseEntity = requestTemplate.doExecuteEntity(downloadUrl, entity, InputStream.class, methodType);
        InputStream inputStream = responseEntity.getBody();
        int statusCode = responseEntity.getStatusCode();
        if (statusCode >= 400) {
            try {
                String errorText = IOUtils.toString(inputStream);
                JSONObject json = JSON.parseObject(errorText);
                throw new PayErrorException(new WxPayError(statusCode + "", json.getString(WxConst.MESSAGE), errorText));
            }
            catch (IOException e) {
                throw new PayErrorException(new WxPayError(statusCode + "", ""));
            }
        }
        Map<String, Object> data = new HashMap<>();
        data.put("file", inputStream);
        return data;
    }


    /**
     * 发起商家转账, 转账账单电子回单申请受理接口
     *
     * @param transferOrder 转账订单
     * @return 对应的转账结果
     */
    @Override
    public Map<String, Object> transfer(TransferOrder transferOrder) {
        //转账账单电子回单申请受理接口
        if (transferOrder.getTransferType() == WxTransferType.TRANSFER_BILL_RECEIPT) {
            Map<String, Object> attr = new MapGen<String, Object>(WxConst.OUT_BATCH_NO, transferOrder.getBatchNo()).getAttr();
            return getAssistService().doExecute(attr, transferOrder.getTransferType());
        }

        Map<String, Object> parameters = new HashMap<>(12);
        parameters.put(WxConst.APPID, payConfigStorage.getAppId());
        parameters.put(WxConst.OUT_BATCH_NO, transferOrder.getBatchNo());
        OrderParaStructure.loadParameters(parameters, WxConst.BATCH_NAME, transferOrder);
        parameters.put(WxConst.BATCH_REMARK, transferOrder.getRemark());
        parameters.put(WxConst.TOTAL_AMOUNT, Util.conversionCentAmount(transferOrder.getAmount()));
        parameters.put(WxConst.TOTAL_NUM, transferOrder.getAttr(WxConst.TOTAL_NUM));
        Object transferDetailListAttr = transferOrder.getAttr(WxConst.TRANSFER_DETAIL_LIST);
        List<TransferDetail> transferDetails = initTransferDetailListAttr(transferDetailListAttr);
        parameters.put(WxConst.TRANSFER_DETAIL_LIST, transferDetails);
        OrderParaStructure.loadParameters(parameters, WxConst.TRANSFER_SCENE_ID, transferOrder);
        return getAssistService().doExecute(parameters, transferOrder.getTransferType());
    }

    private List<TransferDetail> initTransferDetailListAttr(Object transferDetailListAttr) {
        List<TransferDetail> transferDetails = null;
        if (transferDetailListAttr instanceof String) {
            transferDetails = JSON.parseArray((String) transferDetailListAttr, TransferDetail.class);
        }
        else if (null != transferDetailListAttr) {
            //偷懒的做法
            transferDetails = JSON.parseArray(JSON.toJSONString(transferDetailListAttr), TransferDetail.class);
        }
        else {
            return null;
        }

        // 商户上送敏感信息时使用`微信支付平台公钥`加密
        String serialNumber = payConfigStorage.getCertEnvironment().getPlatformSerialNumber();
        Certificate certificate = getAssistService().getCertificate(serialNumber);
        return transferDetails.stream()
                .peek(transferDetailListItem -> {
                    String userName = transferDetailListItem.getUserName();
                    if (StringUtils.isNotEmpty(userName)) {
                        String encryptedUserName = AntCertificationUtil.encryptToString(userName, certificate);
                        transferDetailListItem.setUserName(encryptedUserName);
                    }
                    String userIdCard = transferDetailListItem.getUserIdCard();
                    if (StringUtils.isNotEmpty(userIdCard)) {
                        String encryptedUserIdCard = AntCertificationUtil.encryptToString(userIdCard, certificate);
                        transferDetailListItem.setUserIdCard(encryptedUserIdCard);
                    }
                }).collect(Collectors.toList());
    }

    /**
     * 转账查询API
     * 通过批次单号查询批次单 与 通过明细单号查询明细单
     *
     * @param assistOrder 辅助交易订单
     * @return 对应的转账订单
     */
    @Override
    public Map<String, Object> transferQuery(AssistOrder assistOrder) {
        Map<String, Object> parameters = new HashMap<>(6);
        TransactionType transactionType = assistOrder.getTransactionType();
        List<Object> uriVariables = new ArrayList<>(3);

        if (StringUtils.isNotEmpty(assistOrder.getTradeNo())) {
            uriVariables.add(assistOrder.getTradeNo());
            String detailId = assistOrder.getAttrForString(WxConst.DETAIL_ID);
            if (StringUtils.isNotEmpty(detailId)) {
                uriVariables.add(detailId);
            }
        }
        else if (StringUtils.isNotEmpty(assistOrder.getOutTradeNo())) {
            uriVariables.add(assistOrder.getOutTradeNo());
            String outDetailNo = assistOrder.getAttrForString(WxConst.OUT_DETAIL_NO);
            if (StringUtils.isNotEmpty(outDetailNo)) {
                uriVariables.add(outDetailNo);
            }
        }

        if (transactionType == WxTransferType.QUERY_BATCH_BY_BATCH_ID || transactionType == WxTransferType.QUERY_BATCH_BY_OUT_BATCH_NO) {
            OrderParaStructure.loadParameters(parameters, WxConst.NEED_QUERY_DETAIL, assistOrder);
            OrderParaStructure.loadParameters(parameters, WxConst.OFFSET, assistOrder);
            OrderParaStructure.loadParameters(parameters, WxConst.LIMIT, assistOrder);
            OrderParaStructure.loadParameters(parameters, WxConst.DETAIL_STATUS, assistOrder);
        }


        String requestBody = UriVariables.getMapToParameters(parameters);
        return getAssistService().doExecute(requestBody, assistOrder.getTransactionType(), uriVariables.toArray());
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
        throw new PayErrorException(new WxPayError("", "V3不支持此转账查询：替代方法transferQuery(AssistOrder assistOrder)"));
    }


    /**
     * 创建消息
     *
     * @param message 支付平台返回的消息
     * @return 支付消息对象
     */
    @Override
    public PayMessage createMessage(Map<String, Object> message) {
        return WxPayMessage.create(message);
    }


}
