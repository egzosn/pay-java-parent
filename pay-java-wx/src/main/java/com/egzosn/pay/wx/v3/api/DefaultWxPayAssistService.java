package com.egzosn.pay.wx.v3.api;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.security.Signature;
import java.security.cert.Certificate;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.message.BasicHeader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.egzosn.pay.common.bean.MethodType;
import com.egzosn.pay.common.bean.PayOrder;
import com.egzosn.pay.common.bean.TransactionType;
import com.egzosn.pay.common.exception.PayErrorException;
import com.egzosn.pay.common.http.HttpRequestTemplate;
import com.egzosn.pay.common.http.HttpStringEntity;
import com.egzosn.pay.common.http.ResponseEntity;
import com.egzosn.pay.common.http.UriVariables;
import com.egzosn.pay.common.util.DateUtils;
import com.egzosn.pay.common.util.sign.SignTextUtils;
import com.egzosn.pay.common.util.str.StringUtils;
import com.egzosn.pay.wx.bean.WxPayError;
import com.egzosn.pay.wx.v3.bean.WxTransactionType;
import com.egzosn.pay.wx.v3.utils.AntCertificationUtil;
import com.egzosn.pay.wx.v3.utils.WxConst;

/**
 * 默认的微信支付辅助服务
 *
 * @author Egan
 * email egzosn@gmail.com
 * date 2021/8/7
 */
public class DefaultWxPayAssistService implements WxPayAssistService {
    protected final Logger LOG = LoggerFactory.getLogger(getClass());
    private WxPayConfigStorage payConfigStorage;

    private HttpRequestTemplate requestTemplate;

    private WxPayService wxPayService;


    public DefaultWxPayAssistService(WxPayService wxPayService) {
        this.wxPayService = wxPayService;
        payConfigStorage = wxPayService.getPayConfigStorage();
        requestTemplate = wxPayService.getHttpRequestTemplate();

    }


    /**
     * 发起请求
     *
     * @param parameters      支付参数
     * @param transactionType 交易类型
     * @return 响应内容体
     */
    public JSONObject doExecute(Map<String, Object> parameters, TransactionType transactionType) {
        String requestBody = JSON.toJSONString(parameters, SerializerFeature.WriteMapNullValue);
        return doExecute(requestBody, transactionType);
    }


    /**
     * 发起请求
     *
     * @param body            请求内容
     * @param transactionType 交易类型
     * @param uriVariables    用于匹配表达式
     * @return 响应内容体
     */
    public JSONObject doExecute(String body, TransactionType transactionType, Object... uriVariables) {
        String reqUrl = UriVariables.getUri(wxPayService.getReqUrl(transactionType), uriVariables);
        MethodType method = MethodType.valueOf(transactionType.getMethod());
        if (MethodType.GET == method && StringUtils.isNotEmpty(body)) {
            reqUrl += UriVariables.QUESTION.concat(body);
            body = "";
        }
        HttpEntity entity = buildHttpEntity(reqUrl, body, transactionType.getMethod());
        ResponseEntity<JSONObject> responseEntity = requestTemplate.doExecuteEntity(reqUrl, entity, JSONObject.class, method);
        int statusCode = responseEntity.getStatusCode();
        JSONObject responseBody = responseEntity.getBody();
        if (statusCode >= 400) {
            throw new PayErrorException(new WxPayError(responseBody.getString(WxConst.CODE), responseBody.getString(WxConst.MESSAGE), responseBody.toJSONString()));
        }
        return responseBody;
    }

    /**
     * 发起请求
     *
     * @param parameters 支付参数
     * @param order      订单
     * @return 请求响应
     */
    public JSONObject doExecute(Map<String, Object> parameters, PayOrder order) {
        TransactionType transactionType = order.getTransactionType();
        return doExecute(parameters, transactionType);
    }


    /**
     * 构建请求实体
     * 这里也做签名处理
     *
     * @param body   请求内容体
     * @param method 请求方法
     * @return 请求实体
     */
    public HttpEntity buildHttpEntity(String url, String body, String method) {
        String nonceStr = SignTextUtils.randomStr();
        long timestamp = DateUtils.toEpochSecond();
        String canonicalUrl = UriVariables.getCanonicalUrl(url);
        //签名信息
        String signText = StringUtils.joining("\n", method, canonicalUrl, String.valueOf(timestamp), nonceStr, body);
        String sign = wxPayService.createSign(signText, payConfigStorage.getInputCharset());
        String serialNumber = payConfigStorage.getCertEnvironment().getSerialNumber();
        // 生成token
        String token = String.format(WxConst.TOKEN_PATTERN, payConfigStorage.getMchId(), nonceStr, timestamp, serialNumber, sign);
        HttpStringEntity entity = new HttpStringEntity(body, ContentType.APPLICATION_JSON);
        entity.addHeader(new BasicHeader("Authorization", WxConst.SCHEMA.concat(token)));
        entity.addHeader(new BasicHeader("User-Agent", "Pay-Java-Service"));
        return entity;
    }


    /**
     * 当缓存中平台证书不存在事进行刷新重新获取平台证书
     * 调用/v3/certificates
     */
    @Override
    public void refreshCertificate() {
        JSONObject responseEntity = doExecute("", WxTransactionType.CERT);

        if (null == responseEntity) {
            throw new PayErrorException(new WxPayError(WxConst.FAILURE, "获取证书失败"));
        }
        JSONArray certificates = responseEntity.getJSONArray("data");
        if (null == certificates) {
            return;
        }

        for (int i = 0; i < certificates.size(); i++) {
            JSONObject certificate = certificates.getJSONObject(i);
            JSONObject encryptCertificate = certificate.getJSONObject("encrypt_certificate");
            String associatedData = encryptCertificate.getString("associated_data");
            String nonce = encryptCertificate.getString("nonce");
            String ciphertext = encryptCertificate.getString("ciphertext");
            String publicKey = AntCertificationUtil.decryptToString(associatedData, nonce, ciphertext, payConfigStorage.getSecretKey(), payConfigStorage.getInputCharset());
            ByteArrayInputStream inputStream = new ByteArrayInputStream(publicKey.getBytes(StandardCharsets.UTF_8));
            AntCertificationUtil.loadCertificate(certificate.getString("serial_no"), inputStream);
        }

    }

    /**
     * 通过证书序列获取平台证书
     *
     * @param serialNo 证书序列
     * @return 平台证书
     */
    @Override
    public Certificate getCertificate(String serialNo) {
        final Certificate certificate = AntCertificationUtil.getCertificate(serialNo);
        if (null == certificate){
            refreshCertificate();
        }



        return null;
    }


/*    *//**
     * 我方对响应验签，和应答签名做比较，使用微信平台证书.
     *
     * @param params the params
     * @return the boolean
     *//*
    public boolean responseSignVerify(ResponseSignVerifyParams params) {

        String wechatpaySerial = params.getWechatpaySerial();
        if (CERTIFICATE_MAP.isEmpty() || !CERTIFICATE_MAP.containsKey(wechatpaySerial)) {
            wechatMetaContainer.getTenantIds().forEach(this::refreshCertificate);
        }
        Certificate certificate = CERTIFICATE_MAP.get(wechatpaySerial);

        final String signatureStr = createSign(true, params.getWechatpayTimestamp(), params.getWechatpayNonce(), params.getBody());
        Signature signer = Signature.getInstance("SHA256withRSA");
        signer.initVerify(certificate);
        signer.update(signatureStr.getBytes(StandardCharsets.UTF_8));

        return signer.verify(Base64Utils.decodeFromString(params.getWechatpaySignature()));
    }*/
}
