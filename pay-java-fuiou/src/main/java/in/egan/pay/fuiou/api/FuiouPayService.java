package in.egan.pay.fuiou.api;/**
 * Created by Fuzx on 2017/1/16 0016.
 */

import com.alibaba.fastjson.JSONObject;
import in.egan.pay.common.api.BasePayService;
import in.egan.pay.common.api.Callback;
import in.egan.pay.common.api.PayConfigStorage;
import in.egan.pay.common.bean.MethodType;
import in.egan.pay.common.bean.PayOrder;
import in.egan.pay.common.bean.PayOutMessage;
import in.egan.pay.common.bean.TransactionType;
import in.egan.pay.common.exception.PayErrorException;
import in.egan.pay.common.http.HttpConfigStorage;
import in.egan.pay.common.util.sign.SignUtils;
import in.egan.pay.common.util.str.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Fuzx
 * @create 2017 2017/1/16 0016
 */
public class FuiouPayService extends BasePayService {
    //日志
    protected final Log log = LogFactory.getLog(FuiouPayService.class);
    //正式域名
    public final static String URL_FuiouBaseDomain = "https://pay.fuiou.com/";
    //测试域名
//    public final static String URL_FuiouBaseDomain = "http://www-1.fuiou.com:8888/wg1_run/";
    //B2C/B2B支付
    public final static String URL_FuiouSmpGate  = URL_FuiouBaseDomain + "smpGate.do";
    //B2C/B2B支付(跨境支付)
    public final static String URL_FuiouNewSmpGate = URL_FuiouBaseDomain + "newSmpGate.do";
    //订单退款
    public final static String URL_FuiouSmpRefundGate = URL_FuiouBaseDomain + "newSmpRefundGate.do";
    //3.2	支付结果查询
    public final static String URL_FuiouSmpQueryGate = URL_FuiouBaseDomain + "smpQueryGate.do";
    //3.3	支付结果查询(直接返回)
    public final static String URL_FuiouSmpAQueryGate = URL_FuiouBaseDomain + "smpAQueryGate.do";
    //3.4订单退款
    public final static String URL_NewSmpRefundGate = URL_FuiouBaseDomain +  "newSmpRefundGate.do";

    /**
     * 构造函数，初始化时候使用
     * @param payConfigStorage
     * @param configStorage
     */
    public FuiouPayService (PayConfigStorage payConfigStorage, HttpConfigStorage configStorage) {
        super(payConfigStorage, configStorage);
    }


    /**
     * 回调校验
     * @param params 回调回来的参数集
     * @return 返回检验结果 0000 成功 其他失败
     */
    @Override
    public boolean verify(Map<String, String> params) {
        if (!"0000".equals(params.get("order_pay_code"))) {
            log.debug(String.format("富友支付异常：order_pay_code=%s,错误原因=%s,参数集=%s", params.get("order_pay_code"), params.get("order_pay_error"), params));
            return false;
        }
        try {
            //返回参数校验  和 重新请求订单检查数据是否合法
            return (signVerify(params, params.get("md5")) && verifySource(params.get("order_id")));
        } catch (PayErrorException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 回调签名校验
     *
     * @param params 参数集
     * @param responseSign   响应的签名串
     * @return 校验结果
     */
    @Override
    public boolean signVerify (Map<String, String> params, String responseSign   ) {
        LinkedHashSet<String> keySet = new LinkedHashSet<>();
        keySet.add("mchnt_cd");//商户代码
        keySet.add("order_id");//商户订单号
        keySet.add("order_date");//订单日期
        keySet.add("order_amt");//交易金额
        keySet.add("order_st");//订单状态
        keySet.add("order_pay_code");//错误代码
        keySet.add("order_pay_error");//错误中文描述
        keySet.add("resv1");//保留字段
        keySet.add("fy_ssn");//富友流水号
        StringBuilder verifyMD5Str = new StringBuilder();
        for (String keyStr : keySet) {
            String keyValue = params.get(keyStr);
            if (null == keyValue){
                log.debug(String.format("富友支付返回结果校验:<参数:%s>不能为空,",keyStr));
            }
            verifyMD5Str.append(keyValue).append("|");
        }
        String sign  = createSign(verifyMD5Str.deleteCharAt(verifyMD5Str.length() -1).toString(),payConfigStorage.getInputCharset());
//        System.out.println("加密串"+verifyMD5Str+",,返回参数生成MD5="+sign+",,返回MD5摘要值"+returnSign);
        return responseSign.equals(sign);
    }

    /**
     * 校验回调数据来源是否合法
     *
     * @param order_id 业务id, 数据的真实性.
     * @return 返回校验结果
     */
    @Override
    public boolean verifySource (String order_id) {
        LinkedHashMap<String ,Object> params = new LinkedHashMap<>();
        params.put("mchnt_cd",payConfigStorage.getPid());
        params.put("order_id",order_id);
        params.put("md5",createSign(SignUtils.parameters2MD5Str(params,"|"),payConfigStorage.getInputCharset()));
        JSONObject resultJson   = getHttpRequestTemplate().postForObject(URL_FuiouSmpAQueryGate,params,JSONObject.class);
        return resultJson.getString("order_pay_code").equals("0000");
    }

    /**
     * 将支付请求参数加密成md5
     * @param order 支付订单
     * @return 返回支付请求参数集合
     */
    @Override
    public Map<String, Object> orderInfo(PayOrder order) {
        LinkedHashMap<String, Object> parameters = getOrderInfo(order);
        String sign = createSign(SignUtils.parameters2MD5Str(parameters, "|"), payConfigStorage.getInputCharset());
        parameters.put("md5", sign);
        return parameters;
    }

    /**
     * 按序添加请求参数
     * @param order 支付订单
     * @return 返回支付请求参数集合
     */
    private LinkedHashMap<String, Object> getOrderInfo(PayOrder order) {
        LinkedHashMap<String, Object> parameters = new LinkedHashMap<String, Object>();
        parameters.put("mchnt_cd", payConfigStorage.getPartner());//商户代码
        parameters.put("order_id", order.getTradeNo());//商户订单号
        parameters.put("order_amt", order.getPrice());//交易金额
//        parameters.put("cur_type", null == order.getCurType() ? FuiouCurType.CNY:order.getCurType());//交易币种
        parameters.put("order_pay_type", order.getTransactionType());//支付类型
        parameters.put("page_notify_url", payConfigStorage.getReturnUrl());//商户接受支付结果通知地址
        parameters.put("back_notify_url", StringUtils.isBlank(payConfigStorage.getNotifyUrl()) ? "" : payConfigStorage.getNotifyUrl());//商户接受的支付结果后台通知地址 //非必填
        parameters.put("order_valid_time", "30m");//超时时间 1m-15天，m：分钟、h：小时、d天、1c当天有效，
        parameters.put("iss_ins_cd", order.getBankType());//银行代码
        parameters.put("goods_name", order.getSubject());
        parameters.put("goods_display_url", "1");//商品展示网址 //非必填
        parameters.put("rem", "1");//备注 //非必填
        parameters.put("ver", "1.0.1");//版本号
        return parameters;
    }

    /**
     * 对内容进行加密
     * @param content           需要加密的内容
     * @param characterEncoding 字符编码
     * @return 加密后的字符串
     */
    @Override
    public String createSign(String content, String characterEncoding) {
        return SignUtils.valueOf(payConfigStorage.getSignType().toUpperCase()).createSign(content, "|" + payConfigStorage.getSecretKey(), characterEncoding);
    }

    /**
     * 将请求参数或者请求流转化为 Map
     * @param parameterMap 请求参数
     * @param is           请求流
     * @return 返回参数集合
     */
    @Override
    public Map<String, String> getParameter2Map(Map<String, String[]> parameterMap, InputStream is) {
        return null;
    }

    /**
     *  获取输出消息，用户返回给支付端
     * @param code 返回代码
     * @param message 返回信息
     * @return 消息实体
     */

    @Override
    public PayOutMessage getPayOutMessage(String code, String message) {
        return PayOutMessage.TEXT().content(code.toLowerCase()).build();
    }

    /**
     * 发送支付请求（form表单）
     * @param orderInfo 发起支付的订单信息
     * @param method    请求方式  "post" "get",
     * @return form表单提交的html字符串
     */

    @Override
    public String buildRequest(Map<String, Object> orderInfo, MethodType method) {
        return getFormString(orderInfo, method,URL_FuiouSmpGate );
    }

    /**
     * 获取输出二维码，用户返回给支付端,
     *
     * @param order 发起支付的订单信息
     * @return 空
     */
    @Override
    public BufferedImage genQrPay (PayOrder order) {
        return null;
    }

    /**
     * 根据参数形成form表单
     * @param param 参数
     * @param method 请求方式 get_post
     * @param url 支付请求url地址
     * @return form表单html代码
     */
    private String getFormString(Map<String, Object> param, MethodType method,String url) {
        StringBuffer formHtml = new StringBuffer();

        formHtml.append("<form id=\"fuiousubmit\" name=\"fuiousubmit\" action=\"")
                .append(url)
                .append("\" accept-charset=\"UTF-8\" onsubmit=\"document.charset='UTF-8';")
//                .append( payConfigStorage.getInputCharset())
                .append("\" method=\"")
                .append(method.name().toLowerCase()).append("\">");

        for (String key : param.keySet()) {
            Object o = param.get(key);
            if (null == o || "null".equals(o) || "".equals(o)) {
                continue;
            }
            formHtml.append("<input type=\"hidden\" name=\"" + key + "\" value=\"" + param.get(key) + "\"/>");
        }


        //submit按钮控件请不要含有name属性
//        formHtml.append("<input type=\"submit\" value=\"\" style=\"display:none;\">");
        formHtml.append("</form>");
        formHtml.append("<script>document.forms['fuiousubmit'].submit();</script>");

        return formHtml.toString();
    }

    /**
     * 交易查询接口
     * @param tradeNo    支付平台订单号
     * @param outTradeNo 商户单号
     * @return 空
     */
    @Override
    public Map<String, Object> query(String tradeNo, String outTradeNo) {
        return null;
    }

    /**
     * 交易查询接口
     *
     * @param tradeNo    支付平台订单号
     * @param outTradeNo 商户单号
     * @param callback   处理器
     * @return 空
     */
    @Override
    public <T> T query (String tradeNo, String outTradeNo, Callback<T> callback) {
        return null;
    }

    /**
     * 交易关闭接口
     * @param tradeNo    支付平台订单号
     * @param outTradeNo 商户单号
     * @return 空
     */
    @Override
    public Map<String, Object> close(String tradeNo, String outTradeNo) {
        return null;
    }

    /**
     * 交易关闭接口
     *
     * @param tradeNo    支付平台订单号
     * @param outTradeNo 商户单号
     * @param callback   处理器
     * @return 空
     */
    @Override
    public <T> T close (String tradeNo, String outTradeNo, Callback<T> callback) {
        return null;
    }

    /**
     * 申请退款接口
     *
     * @param tradeNo      支付平台订单号
     * @param outTradeNo   商户单号
     * @param refundAmount 退款金额
     * @param totalAmount  总金额
     * @return 退款返回结果集
     */
    @Override
    public Map<String, Object> refund (String tradeNo, String outTradeNo, BigDecimal refundAmount, BigDecimal totalAmount) {
        Map<String ,Object> params = new HashMap<>();
        params.put("mchnt_cd",payConfigStorage.getSecretKey());//商户代码
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        df.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        params.put("origin_order_date",df.format(new Date()));//原交易日期
        params.put("origin_order_id",tradeNo);//原订单号
        params.put("refund_amt",refundAmount);//退款金额
        params.put("rem","");//备注
        params.put("md5",createSign(SignUtils.parameters2MD5Str(params,"|"),payConfigStorage.getInputCharset()));
        JSONObject resultJson   = getHttpRequestTemplate().postForObject(URL_FuiouSmpRefundGate,params,JSONObject.class);
        //5341标识退款成功
        return resultJson;

    }

    /**
     * 申请退款接口
     *
     * @param tradeNo      支付平台订单号
     * @param outTradeNo   商户单号
     * @param refundAmount 退款金额
     * @param totalAmount  总金额
     * @param callback     处理器
     * @return 空
     */
    @Override
    public <T> T refund (String tradeNo, String outTradeNo, BigDecimal refundAmount, BigDecimal totalAmount, Callback<T> callback) {
        return null;
    }

    /**
     *  查询退款
     * @param tradeNo    支付平台订单号
     * @param outTradeNo 商户单号
     * @return  空
     *
     */

    @Override
    public Map<String, Object> refundquery(String tradeNo, String outTradeNo) {
        return null;
    }

    /**
     * 查询退款
     *
     * @param tradeNo    支付平台订单号
     * @param outTradeNo 商户单号
     * @param callback   处理器
     * @return　空
     */
    @Override
    public <T> T refundquery (String tradeNo, String outTradeNo, Callback<T> callback) {
        return null;
    }

    /**
     * 下载对账单
     * @param billDate 账单时间：日账单格式为yyyy-MM-dd，月账单格式为yyyy-MM。
     * @param billType 账单类型，商户通过接口或商户经开放平台授权后其所属服务商通过接口可以获取以下账单类型：trade、signcustomer；trade指商户基于支付宝交易收单的业务账单；signcustomer是指基于商户支付宝余额收入及支出等资金变动的帐务账单；
     * @return 空
     */
    @Override
    public Object downloadbill(Date billDate, String billType) {
        return null;
    }

    /**
     * 下载对账单
     *
     * @param billDate 账单时间：具体请查看对应支付平台
     * @param billType 账单类型，具体请查看对应支付平台
     * @param callback 处理器
     * @return 空
     */
    @Override
    public <T> T downloadbill (Date billDate, String billType, Callback<T> callback) {
        return null;
    }

    /**
     * 通用查询接口
     *
     * @param tradeNoOrBillDate  支付平台订单号或者账单日期， 具体请 类型为{@link String }或者 {@link Date }，类型须强制限制，类型不对应则抛出异常{@link PayErrorException}
     * @param outTradeNoBillType 商户单号或者 账单类型
     * @param transactionType    交易类型
     * @param callback           处理器
     * @return 空
     */
    @Override
    public <T> T secondaryInterface (Object tradeNoOrBillDate, String outTradeNoBillType, TransactionType transactionType, Callback<T> callback) {
        return null;
    }


}
