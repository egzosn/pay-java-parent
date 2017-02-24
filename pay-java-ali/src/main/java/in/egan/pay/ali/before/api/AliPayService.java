package in.egan.pay.ali.before.api;

import in.egan.pay.ali.util.SimpleGetRequestExecutor;
import in.egan.pay.common.api.BasePayService;
import in.egan.pay.common.api.PayConfigStorage;
import in.egan.pay.common.api.RequestExecutor;
import in.egan.pay.common.bean.MethodType;
import in.egan.pay.common.bean.PayOrder;
import in.egan.pay.common.bean.PayOutMessage;
import in.egan.pay.common.bean.result.PayError;
import in.egan.pay.common.exception.PayErrorException;
import in.egan.pay.common.util.sign.SignUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.*;

/**
 *  支付宝支付通知
 * @author  egan
 * @email egzosn@gmail.com
 * @date 2016-5-18 14:09:01
 * @see in.egan.pay.ali.api.AliPayService
 */
@Deprecated
public class AliPayService extends BasePayService {
    protected final Log log = LogFactory.getLog(AliPayService.class);


    private String httpsReqUrl = "https://mapi.alipay.com/gateway.do";


    public String getHttpsVerifyUrl() {
        return httpsReqUrl + "?service=notify_verify";
    }

    @Override
    public boolean verify(Map<String, String> params) {


        if (params.get("sign") == null || params.get("notify_id") == null) {
            log.debug("支付宝支付异常：params：" + params);
            return false;
        }

        try {
            return getSignVerify(params, params.get("sign")) && "true".equals(verifyUrl(params.get("notify_id")));
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
    public   boolean getSignVerify(Map<String, String> params, String sign) {

        return SignUtils.valueOf(payConfigStorage.getSignType()).verify(params,  sign,  payConfigStorage.getKeyPublic(), payConfigStorage.getInputCharset());
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
     * @source chanjarster/weixin-java-tools
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
     * @see PayOrder
     */
    @Override
    public Map<String, Object> orderInfo(PayOrder order) {

        Map<String, Object> orderInfo = getOrder(order);

        String sign = createSign( SignUtils.parameterText(orderInfo, "&"), "UTF-8");

        try {
            sign = URLEncoder.encode(sign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        orderInfo.put("sign", sign);
        orderInfo.put("sign_type", payConfigStorage.getSignType());
        return orderInfo;
    }

    /**
     * 支付宝创建订单信息
     * create the order info
     *
     * @param order 支付订单
     * @return
     * @see in.egan.pay.common.bean.PayOrder
     */
    private  Map<String, Object> getOrder(PayOrder order) {
        Map<String, Object> orderInfo = new TreeMap<>();
//        StringBuilder orderInfo = new StringBuilder();
        // 签约合作者身份ID
        orderInfo.put("partner", payConfigStorage.getPartner());
//        orderInfo.append( "partner=").append( "\"").append( payConfigStorage.getPartner() ).append("\"");

        // 签约卖家支付宝账号
        orderInfo.put("seller_id", payConfigStorage.getSeller());
//         orderInfo.append("&seller_id=" ) .append("\"" ) .append(payConfigStorage.getSeller() ) .append("\"");

        // 商户网站唯一订单号
        orderInfo.put("out_trade_no", order.getTradeNo());
//         orderInfo.append("&out_trade_no=" ) .append("\"" ).append(order.getTradeNo() ) .append("\"");

        // 商品名称
        orderInfo.put("subject", order.getSubject());
//         orderInfo.append("&subject=" ) .append("\"" ) .append(order.getSubject() ) .append("\"");

        // 商品详情
        orderInfo.put("body", order.getBody());
//         orderInfo.append("&body=" ) .append("\"" ) .append(order.getBody() ) .append("\"");

        // 商品金额
        orderInfo.put("total_fee", order.getPrice().setScale(2, BigDecimal.ROUND_HALF_UP).toString() );
//         orderInfo.append("&total_fee=" ) .append("\"" ) .append(order.getPrice().setScale(2, BigDecimal.ROUND_HALF_UP) ) .append("\"");

        // 服务器异步通知页面路径
        orderInfo.put("notify_url", payConfigStorage.getNotifyUrl() );
//         orderInfo.append("&notify_url=" ) .append("\"" ).append( payConfigStorage.getNotifyUrl() ) .append("\"");

        // 服务接口名称， 固定值
        orderInfo.put("service",  order.getTransactionType().getType()  );
//         orderInfo.append("&service=\"" ).append( order.getTransactionType().getType() ).append("\"");

        // 支付类型， 固定值
        orderInfo.put("payment_type",  "1" );
//         orderInfo.append("&payment_type=\"1\"");

        // 参数编码， 固定值
        orderInfo.put("_input_charset",  payConfigStorage.getInputCharset());
//         orderInfo.append("&_input_charset=\"utf-8\"");

        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        // TODO 2017/2/6 11:05 author: egan  目前写死，这一块建议配置
        orderInfo.put("it_b_pay",  "30m");
//         orderInfo.append("&it_b_pay=\"30m\"");

        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        //  orderInfo.append("&extern_token=" ) .append("\"" ) extern_token ) .append("\"");

        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        orderInfo.put("return_url", payConfigStorage.getReturnUrl());
//         orderInfo.append("&return_url=\"m.alipay.com\"");

        // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
//        if (order.getTransactionType().getType())
//          orderInfo.append("&paymethod=\"expressGateway\"");

        return orderInfo;
    }


    @Override
    public String createSign(String content, String characterEncoding) {

        return  SignUtils.valueOf(payConfigStorage.getSignType()).createSign(content, payConfigStorage.getKeyPrivate(),characterEncoding);
    }


    @Override
    public Map<String, String> getParameter2Map(Map<String, String[]> parameterMap, InputStream is) {

        Map<String,String> params = new TreeMap<String,String>();
        for (Iterator iter = parameterMap.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = parameterMap.get(name);
            String valueStr = "";
            for (int i = 0,len =  values.length; i < len; i++) {
                valueStr += (i == len - 1) ?  values[i]
                        : values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
            //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
            params.put(name, valueStr);
        }

        return params;
    }

    @Override
    public PayOutMessage getPayOutMessage(String code, String message) {
        return PayOutMessage.TEXT().content(code.toLowerCase()).build();
    }

    @Override
    public String buildRequest(Map<String, Object> orderInfo, MethodType method) {

        StringBuffer formHtml = new StringBuffer();

        formHtml.append("<form id=\"_alipaysubmit_\" name=\"alipaysubmit\" action=\"" )
                .append( httpsReqUrl)
                .append(  "?_input_charset=" )
                .append( payConfigStorage.getInputCharset())
                .append( "\" method=\"")
                .append( method.name().toLowerCase()) .append( "\">");

        for (String key: orderInfo.keySet()) {
            Object o = orderInfo.get(key);
            if (null == o ||"null".equals(o) || "".equals(o) ){
                continue;
            }
            formHtml.append("<input type=\"hidden\" name=\"" + key + "\" value=\"" + orderInfo.get(key) + "\"/>");
        }


        //submit按钮控件请不要含有name属性
//        formHtml.append("<input type=\"submit\" value=\"\" style=\"display:none;\">");
        formHtml.append("</form>");
        formHtml.append("<script>document.forms['_alipaysubmit_'].submit();</script>");

        return formHtml.toString();
    }

    /**
     * 生成二维码支付
     * 暂未实现或无此功能
     * @param orderInfo 发起支付的订单信息
     * @return
     */
    @Override
    public BufferedImage genQrPay(Map<String, Object> orderInfo) {
        throw new UnsupportedOperationException();
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

    /**
     *
     * @param executor
     * @param uri
     * @param data
     * @param <T>
     * @param <E>
     * @return
     * @throws PayErrorException
     */
    protected <T, E> T executeInternal(RequestExecutor<T, E> executor, String uri, E data) throws PayErrorException {

        try {
            return executor.execute(getHttpClient(), httpProxy, uri, data);
        } catch (IOException  e) {
            throw new RuntimeException(e);
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
