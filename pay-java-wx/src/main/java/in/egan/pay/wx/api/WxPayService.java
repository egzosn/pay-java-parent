package in.egan.pay.wx.api;

import in.egan.pay.common.api.PayConfigStorage;
import in.egan.pay.common.api.PayService;
import in.egan.pay.common.api.RequestExecutor;
import in.egan.pay.common.bean.MethodType;
import in.egan.pay.common.bean.PayOrder;
import in.egan.pay.common.bean.PayOutMessage;
import in.egan.pay.common.bean.result.PayError;
import in.egan.pay.common.exception.PayErrorException;
import in.egan.pay.common.util.MatrixToImageWriter;
import in.egan.pay.common.util.sign.SignUtils;
import in.egan.pay.common.util.str.StringUtils;
import in.egan.pay.wx.bean.WxTransactionType;
import in.egan.pay.wx.utils.SimplePostRequestExecutor;
import in.egan.pay.common.util.XML;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.*;

/**
 *  支付宝支付通知
 * @author  egan
 * @email egzosn@gmail.com
 * @date 2016-5-18 14:09:01
 */
public class WxPayService implements PayService {
    protected final Log log = LogFactory.getLog(WxPayService.class);

    protected PayConfigStorage payConfigStorage;

    protected CloseableHttpClient httpClient;

    protected HttpHost httpProxy;

    private int retrySleepMillis = 1000;

    private int maxRetryTimes = 5;

    public final static String httpsVerifyUrl = "https://gw.tenpay.com/gateway";
    public final static String unifiedOrderUrl = "https://api.mch.weixin.qq.com/pay/unifiedorder";
//    public final static String orderqueryUrl = "https://api.mch.weixin.qq.com/pay/orderquery";

    /**
     *  微信支付V2版本所需
     *  当前版本不需要
     * @return
     */
    @Deprecated
    @Override
    public String getHttpsVerifyUrl() {
        return httpsVerifyUrl + "/verifynotifyid.xml";
    }

    @Override
    public boolean verify(Map<String, String> params) {
        if (!"SUCCESS".equals(params.get("return_code"))){
            log.debug(String.format("微信支付异常：return_code=%s,参数集=%s", params.get("return_code"), params));
            return false;
        }

        if(params.get("sign") == null) {

            log.debug("微信支付异常：签名为空！out_trade_no=" + params.get("out_trade_no"));
        }

        try {
            return getSignVerify(params, params.get("sign")) && "true".equals(verifyUrl(params.get("out_trade_no")));
        } catch (PayErrorException e) {
            e.printStackTrace();
        }
        return false;
    }



    /**
     * 根据反馈回来的信息，生成签名结果
     * @param params 通知返回来的参数数组
     * @param sign 比对的签名结果
     * @return 生成的签名结果
     */
    public boolean getSignVerify(Map<String, String> params, String sign) {
       return SignUtils.valueOf(payConfigStorage.getSignType()).verify(params,  sign, "&key=" +  payConfigStorage.getKeyPublic(), payConfigStorage.getInputCharset());
    }

    /**
     * 支付宝需要，暂时预留
     * @param out_trade_no 商户单号
     * @return
     * @throws PayErrorException
     */
    @Override
    public String verifyUrl(String out_trade_no) throws PayErrorException {
//        return execute(new SimplePostRequestExecutor(), getHttpsVerifyUrl(), "partner=" + payConfigStorage.getPartner() + "&notify_id=" + notify_id);

        return "true";
    }


    /**
     * 向支付端发送请求，在这里执行的策略是当发生access_token过期时才去刷新，然后重新执行请求，而不是全局定时请求
     *
     * @param executor
     * @param uri
     * @param data
     * @return
     * @throws PayErrorException
     */
    @Override
    public <T, E> T execute(RequestExecutor<T, E> executor, String uri, E data) throws PayErrorException {
        int retryTimes = 0;
        do {
            try {
                return executeInternal(executor, uri, data);
            } catch (PayErrorException e) {
                PayError error = e.getError();
                if (error.getErrorCode() == 403) {
                    int sleepMillis = retrySleepMillis * (1 << retryTimes);
                    try {
                        log.debug(String.format("微信支付系统繁忙，(%s)ms 后重试(第%s次)", sleepMillis, retryTimes + 1));
                        Thread.sleep(sleepMillis);
                    } catch (InterruptedException e1) {
                        throw new RuntimeException(e1);
                    }
                } else {
                    throw e;
                }
            }
        } while (++retryTimes < maxRetryTimes);

        throw new PayErrorException(new PayError(-1, "微信支付服务端异常，超出重试次数"));
    }


    /**
     * 获取支付平台所需的订单信息
     *
     * @param order 支付订单
     * @return
     * @see in.egan.pay.common.bean.PayOrder
     */
    @Override
    public Map<String, Object> orderInfo(PayOrder order) {


//        Map<String, Object> results = new HashMap<String, Object>();
        ////统一下单
        SortedMap<String, Object> parameters = new TreeMap<String, Object>();
        parameters.put("appid", payConfigStorage.getAppid());
        parameters.put("mch_id", payConfigStorage.getPartner());
        parameters.put("nonce_str", SignUtils.randomStr());
        parameters.put("body", order.getSubject());// 购买支付信息
        parameters.put("notify_url", payConfigStorage.getNotifyUrl());
        parameters.put("out_trade_no", order.getOutTradeNo());// 订单号
        parameters.put("spbill_create_ip", "192.168.1.150");
        parameters.put("total_fee", order.getPrice().multiply(new BigDecimal(100)).intValue());// 总金额单位为分
        parameters.put("trade_type", order.getTransactionType().getType());
        parameters.put("attach", order.getBody());
        if (WxTransactionType.NATIVE == order.getTransactionType()){
            parameters.put("product_id",  order.getOutTradeNo());
        }
        String sign = createSign(SignUtils.parameterText(parameters), payConfigStorage.getInputCharset());
        parameters.put("sign", sign);

       String requestXML = XML.getMap2Xml(parameters);
        log.debug("requestXML：" + requestXML);
        String result = null;
        try {
             result = execute(new SimplePostRequestExecutor(), unifiedOrderUrl, requestXML);
            log.debug("获取预支付订单返回结果33:" + result);

            /////////APP端调起支付的参数列表
            Map map  = XML.toJSONObject(result);
            if (!"SUCCESS".equals(map.get("return_code"))){
                throw new PayErrorException(new PayError(-1, (String) map.get("return_msg"), result));
            }
            //如果是扫码支付无需处理，直接返回
            if (WxTransactionType.NATIVE == order.getTransactionType()){
                return map;
            }

            SortedMap<String, Object> params = new TreeMap<String, Object>();
            params.put("appid", payConfigStorage.getAppid());
            params.put("partnerid", payConfigStorage.getPartner());
            params.put("prepayid", map.get("prepay_id"));
            params.put("timestamp", System.currentTimeMillis() / 1000);
            params.put("noncestr", map.get("nonce_str")/*WxpayCore.genNonceStr()*/);

            if (WxTransactionType.JSAPI == order.getTransactionType()){
                params.put("package", "prepay_id=" + map.get("prepay_id"));
                params.put("signType", payConfigStorage.getSignType());
            }else  if (WxTransactionType.APP == order.getTransactionType()){
                params.put("package", "Sign=WXPay");
            }
            String paySign = createSign(SignUtils.parameterText(params), payConfigStorage.getInputCharset());
            params.put("sign", paySign);
            return params;
        } catch (PayErrorException e) {
            e.printStackTrace();
        }

//        result = WxpayCore.httpsRequest2(httpsVerifyUrl, "POST", requestXML);
    //////////////////////////
        return null;
    }



    /**
     * 签名
     * @param content 需要签名的内容 不包含key
     * @param characterEncoding 字符编码
     * @return
     */
    @Override
    public String createSign(String content, String characterEncoding) {
       return SignUtils.valueOf(payConfigStorage.getSignType().toUpperCase()).createSign(content, "&key=" + payConfigStorage.getKeyPrivate(), characterEncoding).toUpperCase();
    }

    @Override
    public Map<String, String> getParameter2Map(Map<String, String[]> parameterMap, InputStream is) {
        TreeMap<String, String> map = new TreeMap();
        try {
            return  XML.inputStream2Map(is, map);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }


    @Override
    public PayOutMessage getPayOutMessage(String code, String message) {
        return PayOutMessage.XML().code(code.toUpperCase()).content(message).build();
    }

    /**
     * 针对web端的即时付款
     *  暂未实现或无此功能
     * @param orderInfo 发起支付的订单信息
     * @param method 请求方式  "post" "get",
     * @return
     */
    @Override
    public String buildRequest(Map<String, Object> orderInfo, MethodType method) {
        throw new UnsupportedOperationException();
    }

    @Override
    public BufferedImage genQrPay(Map<String, Object> orderInfo) {
        //获取对应的支付账户操作工具（可根据账户id）
        if (!"SUCCESS".equals(orderInfo.get("result_code"))){
            throw new RuntimeException(new PayError(-1, (String) orderInfo.get("err_code")).toString());
    }


        return  MatrixToImageWriter.writeInfoToJpgBuff((String) orderInfo.get("code_url"));
    }

    @Override
    public Map<String, Object> query(String tradeNo, String outTradeNo) {
        return null;
    }

    @Override
    public Map<String, Object> close(String tradeNo, String outTradeNo) {
        return null;
    }

    @Override
    public Map<String, Object> refund(String tradeNo, String outTradeNo) {
        return null;
    }

    @Override
    public Map<String, Object> refundquery(String tradeNo, String outTradeNo) {
        return null;
    }

    @Override
    public Object downloadbill(Date billDate, String billType) {
        return null;
    }


    protected <T, E> T executeInternal(RequestExecutor<T, E> executor, String uri, E data) throws PayErrorException {

        try {
            return executor.execute(getHttpClient(), httpProxy, uri, data);
        } catch (IOException  e) {
            throw new RuntimeException(e);
        }
    }

    public HttpHost getHttpProxy() {
        return httpProxy;
    }

    public CloseableHttpClient getHttpClient() {
        return httpClient;
    }

    /**
     * 设置支付配置
     * @param payConfigStorage 支付配置
     */
    public void setPayConfigStorage(PayConfigStorage payConfigStorage) {
        this.payConfigStorage = payConfigStorage;

        String http_proxy_host = payConfigStorage.getHttpProxyHost();
        int http_proxy_port = payConfigStorage.getHttpProxyPort();
        String http_proxy_username = payConfigStorage.getHttpProxyUsername();
        String http_proxy_password = payConfigStorage.getHttpProxyPassword();

        if (StringUtils.isNotBlank(http_proxy_host)) {
            // 使用代理服务器
            if (StringUtils.isNotBlank(http_proxy_username)) {
                // 需要用户认证的代理服务器
                CredentialsProvider credsProvider = new BasicCredentialsProvider();
                credsProvider.setCredentials(
                        new AuthScope(http_proxy_host, http_proxy_port),
                        new UsernamePasswordCredentials(http_proxy_username, http_proxy_password));
                httpClient = HttpClients
                        .custom()
                        .setDefaultCredentialsProvider(credsProvider)
                        .build();
            } else {
                // 无需用户认证的代理服务器
                httpClient = HttpClients.createDefault();
            }
            httpProxy = new HttpHost(http_proxy_host, http_proxy_port);
        } else {
            httpClient = HttpClients.createDefault();
        }
    }

    @Override
    public PayConfigStorage getPayConfigStorage() {
        return payConfigStorage;
    }

    public WxPayService() {
    }

    public WxPayService(PayConfigStorage payConfigStorage) {
        setPayConfigStorage(payConfigStorage);
    }
}
