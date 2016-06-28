package in.egan.pay.wx.api;

import in.egan.pay.common.api.PayConfigStorage;
import in.egan.pay.common.api.PayService;
import in.egan.pay.common.api.RequestExecutor;
import in.egan.pay.common.bean.result.PayError;
import in.egan.pay.common.exception.PayErrorException;
import in.egan.pay.common.util.str.StringUtils;
import in.egan.pay.wx.bean.WxpayCore;
import in.egan.pay.wx.utils.SimplePostRequestExecutor;
import in.egan.pay.wx.utils.XML;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

/**
 *  支付宝支付通知
 * @author  egan
 * @email egzosn@gmail.com
 * @date 2016-5-18 14:09:01
 */
public class WxPayService implements PayService {
    protected final Logger log = LoggerFactory.getLogger(WxPayService.class);

    protected PayConfigStorage payConfigStorage;

    protected CloseableHttpClient httpClient;

    protected HttpHost httpProxy;

    private int retrySleepMillis = 1000;

    private int maxRetryTimes = 5;

    public final static String httpsVerifyUrl = "https://gw.tenpay.com/gateway";
    public final static String unifiedOrderUrl = "https://api.mch.weixin.qq.com/pay/unifiedorder";

    /**
     *  微信支付V2版本所需
     * @return
     */
    @Override
    public String getHttpsVerifyUrl() {
        return httpsVerifyUrl + "/verifynotifyid.xml";
    }

    @Override
    public boolean verify(Map<String, String> params) {
        if (!"SUCCESS".equals(params.get("return_code"))){
            log.debug("微信支付异常：return_code={},参数集=" , params.get("return_code"), params);
            return false;
        }
        SortedMap<String, Object> data = new TreeMap<String, Object>();
        for (String key : params.keySet()){
            data.put(key, params.get(key).trim());
        }
        String sign = createSign(getOrderInfo(data), "UTF-8");
        String tenpaySign = params.get("sign");
        log.debug( " => sign:" + sign + " tenpaySign:" + tenpaySign);
        return tenpaySign.equals(sign);
    }

    @Override
    public boolean checkSignature(Map<String, String> params) {
        return verify(params);
    }

    /**
     * 根据反馈回来的信息，生成签名结果
     * @param Params 通知返回来的参数数组
     * @param sign 比对的签名结果
     * @return 生成的签名结果
     */
    public   boolean getSignVeryfy(Map<String, String> Params, String sign) {
        //过滤空值、sign与sign_type参数
        Map<String, String> sParaNew = WxpayCore.paraFilter(Params);
        //获取待签名字符串
        String preSignStr = WxpayCore.createLinkString(sParaNew);
        //获得签名验证结果
        boolean isSign = false;
        if(payConfigStorage.getSignType().equals("md5")){
//            isSign = RSA.verify(preSignStr, sign, payConfigStorage.getSecretKey(), payConfigStorage.getInputCharset());
        }
        return isSign;
    }

    /**
     * 支付宝需要，暂时预留
     * @param notify_id
     * @return
     * @throws PayErrorException
     */
    @Override
    public String verifyUrl(String notify_id) throws PayErrorException {
//        return execute(new SimplePostRequestExecutor(), getHttpsVerifyUrl(), "partner=" + payConfigStorage.getPartner() + "&notify_id=" + notify_id);

        return null;
    }


    /**
     * 向支付宝端发送请求，在这里执行的策略是当发生access_token过期时才去刷新，然后重新执行请求，而不是全局定时请求
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
                        log.debug("微信支付系统繁忙，{}ms 后重试(第{}次)", sleepMillis, retryTimes + 1);
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
     * @param body 商品名称
     * @param attach 附加参数
     * @param price 价格
     * @param tradeNo 商户单号
     * @return
     */
    @Override
    public Object orderInfo(String body, String attach, BigDecimal price, String tradeNo) {


//        Map<String, Object> results = new HashMap<String, Object>();
        ////统一下单
        SortedMap<String, Object> parameters = new TreeMap<String, Object>();
        parameters.put("appid", payConfigStorage.getAppid());
        parameters.put("mch_id", payConfigStorage.getPartner());
        parameters.put("nonce_str", WxpayCore.genNonceStr());
        parameters.put("body", body);// 购买支付信息
        parameters.put("notify_url", payConfigStorage.getNotifyUrl());
        parameters.put("out_trade_no", tradeNo);// 订单号
        parameters.put("spbill_create_ip", "192.168.1.150");
        parameters.put("total_fee", price.multiply(new BigDecimal(100)).intValue());// 总金额单位为分
        parameters.put("trade_type", "APP");
        parameters.put("attach", attach);
        String sign = createSign(getOrderInfo(parameters), payConfigStorage.getInputCharset());
        parameters.put("sign", sign);

       log.debug("parameters:" + parameters);
       String requestXML = WxpayCore.getMap2Xml(parameters);
        log.debug("requestXML：" + requestXML);
        String result = null;
        try {
             result = execute(new SimplePostRequestExecutor(), unifiedOrderUrl, requestXML);
            log.debug("获取预支付订单返回结果33:" + result);

            /////////APP端调起支付的参数列表
            Map map  = XML.toMap(result);
            SortedMap<String, Object> params = new TreeMap<String, Object>();
            params.put("appid", payConfigStorage.getAppid());
            params.put("timestamp", System.currentTimeMillis() / 1000);
            params.put("noncestr", map.get("nonce_str")/*WxpayCore.genNonceStr()*/);
            params.put("package", "Sign=WXPay");
            params.put("partnerid", payConfigStorage.getPartner());
            params.put("prepayid", map.get("prepay_id"));
//            params.put("signType", "MD5");
            String paySign = createSign(getOrderInfo(params), payConfigStorage.getInputCharset());
            params.put("sign", paySign);
   /*     results.put("appId", WxUtils.APPID);
        results.put("nonceStr", map.get("nonce_str"));
        results.put("package", "prepay_id=" + map.get("prepay_id"));
        results.put("timeStamp", timeStamp);
        results.put("signType", "MD5");
        results.put("paySign", paySign);*/
            return params;
        } catch (PayErrorException e) {
            e.printStackTrace();
        }

//        result = WxpayCore.httpsRequest2(httpsVerifyUrl, "POST", requestXML);
    //////////////////////////
        return null;
    }


    /**
     * 支付宝创建订单信息
     * create the order info
     * @param  parameters 排序后包装好的订单信息与账户信息
     * @return
     */
    private  String getOrderInfo(SortedMap<String, Object> parameters) {

        StringBuffer sb = new StringBuffer();
        Set es = parameters.entrySet();
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            Object v = entry.getValue();
            if (null != v && !"".equals(v) && !"sign".equals(k) && !"key".equals(k)) {
                sb.append(k + "=" + v + "&");
            }
        }
        sb.append("key=" + payConfigStorage.getKeyPrivate());
        log.debug("请求参数拼接："+sb.toString());

        return sb.toString();
    }

    /**
     * 签名
     * @param content 需要签名的内容
     * @param characterEncoding 字符编码
     * @return
     */
    @Override
    public String createSign(String content, String characterEncoding) {
        String sign = WxpayCore.MD5Encode(content, characterEncoding).toUpperCase();
        return sign;
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
