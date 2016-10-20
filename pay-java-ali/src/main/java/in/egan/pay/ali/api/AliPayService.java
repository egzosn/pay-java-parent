package in.egan.pay.ali.api;

import in.egan.pay.ali.bean.AlipayCore;
import in.egan.pay.common.api.PayConfigStorage;
import in.egan.pay.common.api.PayService;
import in.egan.pay.common.api.RequestExecutor;
import in.egan.pay.common.bean.PayOrder;
import in.egan.pay.common.bean.result.PayError;
import in.egan.pay.common.exception.PayErrorException;
import in.egan.pay.common.util.encrypt.RSA;
import in.egan.pay.common.util.http.SimpleGetRequestExecutor;
import in.egan.pay.common.util.str.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *  支付宝支付通知
 * @author  egan
 * @email egzosn@gmail.com
 * @date 2016-5-18 14:09:01
 */
public class AliPayService implements PayService {
    protected final Log log = LogFactory.getLog(AliPayService.class);

    protected PayConfigStorage payConfigStorage;

    protected CloseableHttpClient httpClient;

    protected HttpHost httpProxy;

    private int retrySleepMillis = 1000;

    private int maxRetryTimes = 5;

    private String httpsVerifyUrl = "https://mapi.alipay.com/gateway.do?service=";


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
        Map<String, String> sParaNew = AlipayCore.paraFilter(Params);
        //获取待签名字符串
        String preSignStr = AlipayCore.createLinkString(sParaNew);
        //获得签名验证结果
        boolean isSign = false;
        if(payConfigStorage.getSignType().equals("RSA")){
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
                if (error.getErrorCode() == 404) {
                    int sleepMillis = retrySleepMillis * (1 << retryTimes);
                    try {
                        log.debug(String.format("支付宝系统繁忙，(%s)ms 后重试(第%s次)", sleepMillis, retryTimes + 1));
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

    /**
     * 返回创建的订单信息
     *
     * @param order 支付订单
     * @return
     * @see in.egan.pay.common.bean.PayOrder
     */
    @Override
    public String orderInfo(PayOrder order) {
        String orderInfo = getOrderInfo(order);
        String sign = createSign(orderInfo, "UTF-8");

        try {
            sign = URLEncoder.encode(sign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        final String payInfo = orderInfo + "&sign=\"" + sign + "\"&" + "sign_type=\"RSA\"";

        return payInfo;
    }


    /**
     * 支付宝创建订单信息
     * create the order info
     *
     * @param order 支付订单
     * @return
     * @see in.egan.pay.common.bean.PayOrder
     */
    private  String getOrderInfo(PayOrder order) {

        // 签约合作者身份ID
        String orderInfo = "partner=" + "\"" + payConfigStorage.getPartner() + "\"";

        // 签约卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + payConfigStorage.getSeller() + "\"";

        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" +order.getTradeNo() + "\"";

        // 商品名称
        orderInfo += "&subject=" + "\"" + order.getSubject() + "\"";

        // 商品详情
        orderInfo += "&body=" + "\"" + order.getBody() + "\"";

        // 商品金额
        orderInfo += "&total_fee=" + "\"" + order.getPrice().setScale(2, BigDecimal.ROUND_HALF_UP) + "\"";

        // 服务器异步通知页面路径
        orderInfo += "&notify_url=" + "\"" + payConfigStorage.getNotifyUrl() + "\"";

        // 服务接口名称， 固定值
        orderInfo += "&service=\"" + order.getTransactionType().getType() + "\"";

        // 支付类型， 固定值
        orderInfo += "&payment_type=\"1\"";

        // 参数编码， 固定值
        orderInfo += "&_input_charset=\"utf-8\"";

        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo += "&it_b_pay=\"30m\"";

        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        orderInfo += "&return_url=\"m.alipay.com\"";

        // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
        // orderInfo += "&paymethod=\"expressGateway\"";

        return orderInfo;
    }

    @Override
    public String createSign(String content, String characterEncoding) {
        return RSA.sign(content, payConfigStorage.getKeyPrivate(), payConfigStorage.getSignType(), characterEncoding);
    }


    @Override
    public Map<String, String> getParameter2Map(Map<String, String[]> parameterMap, InputStream is) {

        Map<String,String> params = new HashMap<String,String>();
        for (Iterator iter = parameterMap.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = parameterMap.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
            //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
            params.put(name, valueStr);
        }

        return params;
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

    public AliPayService() {
    }

    public AliPayService(PayConfigStorage payConfigStorage) {
        setPayConfigStorage(payConfigStorage);
    }
}
