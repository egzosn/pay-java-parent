package in.egan.pay.ali.api;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import in.egan.pay.ali.bean.AliTransactionType;
import in.egan.pay.common.api.BasePayService;
import in.egan.pay.common.api.Callback;
import in.egan.pay.common.api.PayConfigStorage;
import in.egan.pay.common.bean.MethodType;
import in.egan.pay.common.bean.PayOrder;
import in.egan.pay.common.bean.PayOutMessage;
import in.egan.pay.common.bean.TransactionType;
import in.egan.pay.common.bean.result.PayException;
import in.egan.pay.common.exception.PayErrorException;
import in.egan.pay.common.http.ClientHttpRequest;
import in.egan.pay.common.http.HttpConfigStorage;
import in.egan.pay.common.util.sign.SignUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 *  支付宝支付通知
 * @author  egan
 * @email egzosn@gmail.com
 * @date 2017-2-22 20:09
 */
public class AliPayService extends BasePayService {
    protected final Log log = LogFactory.getLog(AliPayService.class);

      //正式测试环境
    private String httpsReqUrl = "https://openapi.alipay.com/gateway.do";
    //沙箱测试环境账号
    private String devReqUrl = "https://openapi.alipaydev.com/gateway.do";
    //兼容上一版本即时收款
    private String httpsReqUrlBefore = "https://mapi.alipay.com/gateway.do";


    /**
     * 获取对应的请求地址
     * @return
     */
    public String getReqUrl(){
        return payConfigStorage.isTest() ? devReqUrl : httpsReqUrl;
    }


    public AliPayService(PayConfigStorage payConfigStorage, HttpConfigStorage configStorage) {
        super(payConfigStorage, configStorage);
    }

    public AliPayService(PayConfigStorage payConfigStorage) {
        super(payConfigStorage);
    }


    public String getHttpsVerifyUrl() {
        return getReqUrl() + "?service=notify_verify";
    }

    @Override
    public boolean verify(Map<String, String> params) {


        if (params.get("sign") == null || params.get("notify_id") == null) {
            log.debug("支付宝支付异常：params：" + params);
            return false;
        }

         return signVerify(params, params.get("sign")) && verifySource(params.get("notify_id"));

    }

    /**
     * 根据反馈回来的信息，生成签名结果
     * @param params 通知返回来的参数数组
     * @param sign 比对的签名结果
     * @return 生成的签名结果
     */
    @Override
    public boolean signVerify(Map<String, String> params, String sign) {

        return SignUtils.valueOf(payConfigStorage.getSignType()).verify(params,  sign,  payConfigStorage.getKeyPublic(), payConfigStorage.getInputCharset());
    }






    /**
     *
     * @param id 业务id, 数据的真实性.
     * @return
     */
    @Override
    public boolean verifySource(String id) {

//        return "true".equals(requestTemplate.getForObject( getHttpsVerifyUrl() + "partner=" + payConfigStorage.getPid() + "&notify_id=" + id, String.class));
        return true;
    }


    /**
     *  生成并设置签名
     * @param parameters 请求参数
     * @return
     */
    private Map<String, Object> setSign(Map<String, Object> parameters){
        parameters.put("sign_type", payConfigStorage.getSignType());
        String sign = createSign( SignUtils.parameterText(parameters, "&", "sign"), payConfigStorage.getInputCharset());

        parameters.put("sign", sign);
        return parameters;
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

        return setSign(getOrder(order));
    }


   /**
     * 支付宝创建订单信息
     * create the order info
     *
     * @param order 支付订单
     * @return
     * @see PayOrder
     */
    private  Map<String, Object> getOrder(PayOrder order) {

        //兼容上一版本 即时收款
  /*      if (AliTransactionType.DIRECT == order.getTransactionType() || AliTransactionType.MOBILE == order.getTransactionType() || AliTransactionType.WAPPAY == order.getTransactionType()){
            return getOrderBefore(order);
        }
*/

        Map<String, Object> orderInfo = getPublicParameters(order.getTransactionType());

        orderInfo.put("notify_url", payConfigStorage.getNotifyUrl());
        orderInfo.put("format", "json");


        Map<String, Object> bizContent = new TreeMap<>();
        if (order.getTransactionType() == AliTransactionType.WAP){
            bizContent.put("product_code", "QUICK_WAP_PAY");
            orderInfo.put("return_url", payConfigStorage.getReturnUrl());
        }else if ("alipay.trade.pay".equals(order.getTransactionType().getMethod())){
            bizContent.put("scene", order.getTransactionType().toString().toLowerCase());
            bizContent.put("product_code", "FACE_TO_FACE_PAYMENT");
            bizContent.put("auth_code", order.getAuthCode());
        }else {
            bizContent.put("product_code", "QUICK_MSECURITY_PAY");
        }
        bizContent.put("body", order.getBody());
        bizContent.put("seller_id", payConfigStorage.getSeller());
        bizContent.put("subject", order.getSubject());
        bizContent.put("out_trade_no", order.getOutTradeNo());
        bizContent.put("total_amount", order.getPrice().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        orderInfo.put("biz_content", JSON.toJSONString(bizContent));
        return orderInfo;
    }

    /**
     * 获取公共请求参数
     * @param transactionType 交易类型
     * @return
     */
    private Map<String, Object> getPublicParameters(TransactionType transactionType ){
        Map<String, Object> orderInfo = new TreeMap<>();
        orderInfo.put("app_id", payConfigStorage.getAppid());
        orderInfo.put("method", transactionType.getMethod());
        orderInfo.put("charset", payConfigStorage.getInputCharset());
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        df.setTimeZone(TimeZone.getTimeZone("GMT+8"));
//      DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        orderInfo.put("timestamp", df.format(new Date()));
        orderInfo.put("version", "1.0");
        return  orderInfo;
    }

    /**
     * 支付宝创建订单信息
     * create the order info
     *
     * @param order 支付订单
     * @return
     * @see PayOrder
     */
    private  Map<String, Object> getOrderBefore(PayOrder order) {
        Map<String, Object> orderInfo = new TreeMap<>();
        // 签约合作者身份ID
        orderInfo.put("partner", payConfigStorage.getPartner());

        // 签约卖家支付宝账号
        orderInfo.put("seller_id", payConfigStorage.getSeller());

        // 商户网站唯一订单号
        orderInfo.put("out_trade_no", order.getTradeNo());

        // 商品名称
        orderInfo.put("subject", order.getSubject());

        // 商品详情
        orderInfo.put("body", order.getBody());

        // 商品金额
        orderInfo.put("total_fee", order.getPrice().setScale(2, BigDecimal.ROUND_HALF_UP).toString() );

        // 服务器异步通知页面路径
        orderInfo.put("notify_url", payConfigStorage.getNotifyUrl() );

        // 服务接口名称， 固定值
        orderInfo.put("service",  order.getTransactionType().getType()  );

        // 支付类型， 固定值
        orderInfo.put("payment_type",  "1" );

        // 参数编码， 固定值
        orderInfo.put("_input_charset",  payConfigStorage.getInputCharset());

        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        // TODO 2017/2/6 11:05 author: egan  目前写死，这一块建议配置
        orderInfo.put("it_b_pay",  "30m");
        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        //  orderInfo.put("extern_token", extern_token );
        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        orderInfo.put("return_url", payConfigStorage.getReturnUrl());

        // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
//        if (order.getTransactionType().getType())
//          orderInfo.put("paymethod","expressGateway");

        return orderInfo;
    }




    @Override
    public Map<String, String> getParameter2Map(Map<String, String[]> parameterMap, InputStream is) {

        Map<String,String> params = new TreeMap<String,String>();
        for (Iterator iter = parameterMap.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = parameterMap.get(name);
            String valueStr = "";
            for (int i = 0,len =  values.length; i < len; i++) {
                valueStr += (i == len - 1) ?  values[i] : values[i] + ",";
            }
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
        formHtml.append("<form id=\"_alipaysubmit_\" name=\"alipaysubmit\" action=\"");
        if (null == orderInfo.get("method")) {
            formHtml.append(httpsReqUrlBefore)
                    .append("?_input_charset=")
                    .append(payConfigStorage.getInputCharset())
                    .append("\" method=\"")
                    .append(method.name().toLowerCase()).append("\">");
            for (String key : orderInfo.keySet()) {
                Object o = orderInfo.get(key);
                if (null == o || "null".equals(o) || "".equals(o)) {
                    continue;
                }

                formHtml.append("<input type=\"hidden\" name=\"" + key + "\" value=\"" + orderInfo.get(key) + "\"/>");

            }

        } else {
            String biz_content = (String)orderInfo.remove("biz_content");
            formHtml.append(getReqUrl()).append("?").append(ClientHttpRequest.getMapToParameters(orderInfo))
            .append("\" method=\"")
                    .append(method.name().toLowerCase()).append("\">");

            formHtml.append("<input type=\"hidden\" name=\"biz_content\" value=\"" + biz_content.replace("\"", "&quot;") + "\"/>");
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
    /**
     *  交易查询接口
     * @param tradeNo 支付平台订单号
     * @param outTradeNo 商户单号
     * @return
     */
    @Override
    public Map<String, Object> query(String tradeNo, String outTradeNo) {

        return  query(tradeNo, outTradeNo, new Callback<Map<String, Object>>() {
            @Override
            public Map<String, Object> perform(Map<String, Object> map) {
                return map;
            }
        });
    }

    /**
     *
     * @param tradeNo    支付平台订单号
     * @param outTradeNo 商户单号
     * @param callback 处理器
     * @param <T>
     * @return
     */
    @Override
    public <T> T query(String tradeNo, String outTradeNo, Callback<T> callback) {

        return secondaryInterface(tradeNo, outTradeNo, AliTransactionType.QUERY, callback);
    }


    @Override
    public Map<String, Object> close(String tradeNo, String outTradeNo) {

        return  close(tradeNo, outTradeNo, new Callback<Map<String, Object>>() {
            @Override
            public Map<String, Object> perform(Map<String, Object> map) {
                return map;
            }
        });
    }

    @Override
    public <T> T close(String tradeNo, String outTradeNo, Callback<T> callback) {
        return  secondaryInterface(tradeNo, outTradeNo, AliTransactionType.CLOSE, callback);
    }

    @Override
    public Map<String, Object> refund(String tradeNo, String outTradeNo, BigDecimal refundAmount, BigDecimal totalAmount) {

        return  refund(tradeNo, outTradeNo, refundAmount, totalAmount, new Callback<Map<String, Object>>() {
            @Override
            public Map<String, Object> perform(Map<String, Object> map) {
                return map;
            }
        });
    }

    @Override
    public <T> T refund(String tradeNo, String outTradeNo, BigDecimal refundAmount, BigDecimal totalAmount, Callback<T> callback) {
        //获取公共参数
        Map<String, Object> parameters = getPublicParameters(AliTransactionType.REFUND);

        Map<String, Object> bizContent = getBizContent(tradeNo, outTradeNo, null);
        bizContent.put("refund_amount", refundAmount.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        //设置请求参数的集合
        parameters.put("biz_content", JSON.toJSONString(bizContent));
        //设置签名
        setSign(parameters);
        return  callback.perform(requestTemplate.getForObject(getReqUrl() + "?" + ClientHttpRequest.getMapToParameters(parameters), JSONObject.class));
    }

    @Override
    public Map<String, Object> refundquery(String tradeNo, String outTradeNo) {
        return  refundquery(tradeNo, outTradeNo, new Callback<Map<String, Object>>() {
            @Override
            public Map<String, Object> perform(Map<String, Object> map) {
                return map;
            }
        });
    }

    @Override
    public <T> T refundquery(String tradeNo, String outTradeNo, Callback<T> callback) {
        return secondaryInterface(tradeNo, outTradeNo, AliTransactionType.REFUNDQUERY, callback);
    }

    /**
     * 目前只支持日账单
     * @param billDate 账单类型，商户通过接口或商户经开放平台授权后其所属服务商通过接口可以获取以下账单类型：trade、signcustomer；trade指商户基于支付宝交易收单的业务账单；signcustomer是指基于商户支付宝余额收入及支出等资金变动的帐务账单；
     * @param billType 账单时间：日账单格式为yyyy-MM-dd，月账单格式为yyyy-MM。
     * @return
     */
    @Override
    public Map<String, Object> downloadbill(Date billDate, String billType) {
        return  downloadbill(billDate, billType, new Callback<Map<String, Object>>() {
            @Override
            public Map<String, Object> perform(Map<String, Object> map) {
                return map;
            }
        });
    }

    /**
     *  目前只支持日账单
     * @param billDate 账单时间：具体请查看对应支付平台
     * @param billType 账单类型，具体请查看对应支付平台
     * @param callback 处理器
     * @param <T>
     * @return
     */
    @Override
    public <T> T downloadbill(Date billDate, String billType, Callback<T> callback) {

        //获取公共参数
        Map<String, Object> parameters = getPublicParameters(AliTransactionType.DOWNLOADBILL);

        Map<String, Object> bizContent = new TreeMap<>();
        bizContent.put("bill_type", billType);
        //目前只支持日账单
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        df.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        bizContent.put("bill_date", df.format(billDate));
        //设置请求参数的集合
        parameters.put("biz_content", JSON.toJSONString(bizContent));
        //设置签名
        setSign(parameters);
        return callback.perform(requestTemplate.getForObject(getReqUrl() + "?" + ClientHttpRequest.getMapToParameters(parameters), JSONObject.class));
    }

    /**
     *
     * @param tradeNoOrBillDate 支付平台订单号或者账单类型， 具体请
     *                          类型为{@link String }或者 {@link Date }，类型须强制限制，类型不对应则抛出异常{@link in.egan.pay.common.exception.PayErrorException}
     * @param outTradeNoBillType  商户单号或者 账单类型
     * @param transactionType 交易类型
     * @param callback 处理器
     * @param <T>
     * @return
     */
    @Override
    public <T> T secondaryInterface(Object tradeNoOrBillDate, String outTradeNoBillType, TransactionType transactionType, Callback<T> callback) {

        if (transactionType == AliTransactionType.REFUND){
            throw new PayErrorException(new PayException("failure", "通用接口不支持:" + transactionType));
        }

        if (transactionType == AliTransactionType.DOWNLOADBILL){
            if (tradeNoOrBillDate instanceof  Date){
                return downloadbill((Date) tradeNoOrBillDate, outTradeNoBillType, callback);
            }
            throw new PayErrorException(new PayException("failure", "非法类型异常:" + tradeNoOrBillDate.getClass()));
        }

        if (!(tradeNoOrBillDate instanceof  String)){
            throw new PayErrorException(new PayException("failure", "非法类型异常:" + tradeNoOrBillDate.getClass()));
        }

        //获取公共参数
        Map<String, Object> parameters = getPublicParameters(transactionType);
        //设置请求参数的集合
        parameters.put("biz_content", getContentToJson(tradeNoOrBillDate.toString(), outTradeNoBillType));
        //设置签名
        setSign(parameters);
        return  callback.perform(requestTemplate.getForObject(getReqUrl() + "?" + ClientHttpRequest.getMapToParameters(parameters), JSONObject.class));

    }

    /**
     *  获取biz_content。请求参数的集合 不包含下载账单
     * @param tradeNo 支付平台订单号
     * @param outTradeNo 商户单号
     * @param bizContent  请求参数的集合
     * @return
     */
    private  Map<String, Object> getBizContent(String tradeNo, String outTradeNo,  Map<String, Object> bizContent){
        if (null == bizContent){
           bizContent = new TreeMap<>();
        }
        if (null != outTradeNo){
            bizContent.put("out_trade_no", outTradeNo);
        }
        if (null != tradeNo){
            bizContent.put("trade_no", tradeNo);
        }
        return bizContent;
    }

    /**
     *  获取biz_content。不包含下载账单
     * @param tradeNo 支付平台订单号
     * @param outTradeNo 商户单号
     * @return
     */
    private String getContentToJson(String tradeNo, String outTradeNo){

        return JSON.toJSONString(getBizContent(tradeNo, outTradeNo, null));
    }

}
