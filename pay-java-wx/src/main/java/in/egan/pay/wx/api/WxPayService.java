package in.egan.pay.wx.api;

import in.egan.pay.common.api.PayConfigStorage;
import in.egan.pay.common.api.PayService;
import in.egan.pay.common.api.RequestExecutor;
import in.egan.pay.common.bean.result.PayError;
import in.egan.pay.common.exception.PayErrorException;
import in.egan.pay.common.util.encrypt.RSA;
import in.egan.pay.common.util.http.SimpleGetRequestExecutor;
import in.egan.pay.wx.bean.WxpayCore;
import org.apache.commons.lang3.StringUtils;
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

    private String httpsVerifyUrl = "https://mapi.alipay.com/gateway.do?service=";


    @Override
    public String getHttpsVerifyUrl() {
        return httpsVerifyUrl + "notify_verify";
    }

    @Override
    public boolean verify(Map<String, String> params) {
        String responseTxt = "false";
        if(params.get("notify_id") != null) {
            String notify_id = params.get("notify_id");
            try {
                responseTxt = verifyUrl(notify_id);
            } catch (PayErrorException e) {
                e.printStackTrace();
            }
        }
        String sign = "";
        if(params.get("sign") != null) {sign = params.get("sign");}
        boolean isSign = getSignVeryfy(params, sign);

        //写日志记录（若要调试，请取消下面两行注释）
        //String sWord = "responseTxt=" + responseTxt + "\n isSign=" + isSign + "\n 返回回来的参数：" + AlipayCore.createLinkString(params);
        //AlipayCore.logResult(sWord);

        if (isSign && responseTxt.equals("true")) {
            return true;
        } else {
            return false;
        }
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
            isSign = RSA.verify(preSignStr, sign, payConfigStorage.getSecretKey(), payConfigStorage.getInputCharset());
        }
        return isSign;
    }


    @Override
    public String verifyUrl(String notify_id) throws PayErrorException {
        return execute(new SimpleGetRequestExecutor(), getHttpsVerifyUrl(), "partner=" + payConfigStorage.getPartner() + "&notify_id=" + notify_id);
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
                if (error.getErrorCode() == -1) {
                    int sleepMillis = retrySleepMillis * (1 << retryTimes);
                    try {
                        log.debug("支付宝系统繁忙，{}ms 后重试(第{}次)", sleepMillis, retryTimes + 1);
                        Thread.sleep(sleepMillis);
                    } catch (InterruptedException e1) {
                        throw new RuntimeException(e1);
                    }
                } else {
                    throw e;
                }
            }
        } while (++retryTimes < maxRetryTimes);

        throw new RuntimeException("支付宝服务端异常，超出重试次数");
    }

    @Override
    public Object orderInfo(String subject, String body, String price, String tradeNo) {


        Map<String, Object> results = new HashMap<String, Object>();
        // System.out.println("微信支付"+openId);
        // Map<String, String> params = this.getConfigParams();
        // 返回格式
        String format = "xml";
        // 必填，不需要修改
        // 请求号
        //String req_id = UtilDate.getOrderNum();
        // 必填，须保证每次请求都是唯一

        // req_data详细信息


        // 用户付款中途退出返回商户的地址。需http://格式的完整路径，不允许加?id=123这类自定义参数
        String time = "";


        // log.info("base path=" + basePath);
        // log.info("go pay come in the openId=" + openId
        // +"==time="+time);
        SortedMap<String, Object> parameters = new TreeMap<String, Object>();
        parameters.put("appid", payConfigStorage.getAppid());
        parameters.put("body", "ceshi");// 购买支付信息
        parameters.put("mch_id", payConfigStorage.getPartner());
//        parameters.put("nonce_str", new Date().getTime() + "");
        parameters.put("notify_url", payConfigStorage.getNotifyUrl());
        parameters.put("out_trade_no", tradeNo);// 订单号
//        parameters.put("spbill_create_ip", "192.168.0.1");
        parameters.put("total_fee", price);// 总金额单位为分
        parameters.put("trade_type", "JSAPI");
//        parameters.put("openid", wxMember.getOpenid());


        String sign = createSign(getOrderInfo(parameters), payConfigStorage.getInputCharset());
        parameters.put("sign", sign);
        System.out.println("parameters:" + parameters);
       /* String requestXML = WxUtils.getRequestXml(parameters);
        System.out.println("requestXML：" + requestXML);
        String result = WxUtils.httpsRequest2(WxUtils.WXPAY_GATEWAY, "POST", requestXML);//
        System.out.println("获取预支付订单返回结果33:" + result);
        Map<String, String> map = WxUtils.doXMLParse(result);
        System.out.println("213");
        SortedMap<Object, Object> params = new TreeMap<Object, Object>();
        long timeStamp = new Date().getTime();
        params.put("appId", WxUtils.APPID);
        params.put("timeStamp", timeStamp);
        params.put("nonceStr", map.get("nonce_str"));
        params.put("package", "prepay_id=" + map.get("prepay_id"));
        params.put("signType", "MD5");
        String paySign = WxUtils.createSign("", params);
        params.put("packageValue", "prepay_id=" + map.get("prepay_id")); // 这里用packageValue是预防package是关键字在js获取值出错
        params.put("paySign", paySign); // paySign的生成规则和Sign的生成规则一致
        params.put("sendUrl", notify_url + "?openId=" + wxMember.getOpenid() + "&time=" + timeStamp); // 付款成功后跳转的*/
           /* // 方法
            String userAgent = request.getHeader("user-agent");
            char agent = userAgent.charAt(userAgent.indexOf("MicroMessenger") + 15);
            params.put("agent", new String(new char[] { agent }));// 微信版本号，用于前面提到的判断用户手机微信的版本是否是5.0以上版本。
            String json = JSONArray.fromObject(params).toString();
            System.out.println(json);*/

      /*  results.put("appId", WxUtils.APPID);
        results.put("timeStamp", timeStamp);
        results.put("nonceStr", map.get("nonce_str"));
        results.put("signType", "MD5");
        results.put("package", "prepay_id=" + map.get("prepay_id"));
        results.put("paySign", paySign);*/
        return results;
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
        sb.append("key=" + payConfigStorage.getSecretKey());
        System.out.println("请求参数拼接："+sb.toString());

        return sb.toString();
    }

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
}
