package in.egan.pay.fuiou.api;/**
 * Created by Fuzx on 2017/1/16 0016.
 */

import com.alibaba.fastjson.JSONObject;
import in.egan.pay.common.api.BasePayService;
import in.egan.pay.common.api.PayConfigStorage;
import in.egan.pay.common.api.RequestExecutor;
import in.egan.pay.common.bean.MethodType;
import in.egan.pay.common.bean.PayOrder;
import in.egan.pay.common.bean.PayOutMessage;
import in.egan.pay.common.bean.result.PayError;
import in.egan.pay.common.exception.PayErrorException;
import in.egan.pay.common.util.XML;
import in.egan.pay.common.util.sign.SignUtils;
import in.egan.pay.common.util.str.StringUtils;
import in.egan.pay.fuiou.utils.SimplePostRequestExecutor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.message.BasicNameValuePair;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.util.*;

/**
 * @author Fuzx
 * @create 2017 2017/1/16 0016
 */
public class FuiouPayService extends BasePayService {

    protected final Log log = LogFactory.getLog(FuiouPayService.class);

    //    public final static String fuiouBaseDomain = "https://pay.fuiou.com/";//正式域名
    public final static String fuiouBaseDomain = "http://www-1.fuiou.com:8888/wg1_run/";//测试域名

    public final static String fuiouSmpGate = fuiouBaseDomain + "smpGate.do";//B2C/B2B支付

    public final static String fuiouNewSmpGate = fuiouBaseDomain + "newSmpGate.do";//B2C/B2B支付(跨境支付)
    public final static String fuiouSmpRefundGate = fuiouBaseDomain + "newSmpRefundGate.do";//订单退款

    public final static String fuiouSmpQueryGate = fuiouBaseDomain + "smpQueryGate.do";//3.2	支付结果查询
    public final static String fuiouSmpAQueryGate = fuiouBaseDomain + "smpAQueryGate.do";//3.3	支付结果查询(直接返回)

    public FuiouPayService(PayConfigStorage payConfigStorage) {
        setPayConfigStorage(payConfigStorage);
    }


    @Override
    public String getHttpsVerifyUrl() {
        return null;
    }


    @Override
    public boolean verify(Map<String, String> params) {
        // TODO 2017/2/9 17:24 author: egan  需要校验签名，签名通过后，再校验订单的真实(根据单号查询对应的订单)
        if (!"0000".equals(params.get("order_pay_code"))) {
            log.debug(String.format("富友支付异常：order_pay_code=%s,错误原因=%s,参数集=%s", params.get("order_pay_code"), params.get("order_pay_error"), params));
            return false;
        }
        try {
            return getSignVerify(params, params.get("md5")) && "0000".equals(verifyUrl(params.get("order_id")));//返回参数校验  和 重新请求订单检查是否真实支付成功
        } catch (PayErrorException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean getSignVerify(Map<String, String> params, String returnSign) {
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
        if(returnSign.equals(sign)){
            return true;
        }
        return false;
    }

    @Override
    public String verifyUrl(String order_id) throws PayErrorException {
//        LinkedHashMap param = new LinkedHashMap();
//        param.put("mchnt_cd",payConfigStorage.getPartner());
//        param.put("order_id",order_id);
//        param.put("md5",createSign(SignUtils.parameters2MD5Str(param,"|"),payConfigStorage.getInputCharset()));
        List<BasicNameValuePair> pairList = new ArrayList<BasicNameValuePair>();
        pairList.add(new BasicNameValuePair("mchnt_cd",payConfigStorage.getPartner()));
        pairList.add(new BasicNameValuePair("order_id",order_id));
        pairList.add(new BasicNameValuePair("md5",createSign(SignUtils.parameters2MD5Str(pairList,"|"),payConfigStorage.getInputCharset())));
        return execute(new SimplePostRequestExecutor(), fuiouSmpAQueryGate,pairList);
//        JSONObject jsonObject = XML.toJSONObject(responseContent);

//        return getFormString(param,MethodType.POST,fuiouSmpAQueryGate);
    }

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
                        log.debug(String.format("富友支付系统错误，错误信息:<%s>,(%s)ms 后重试(第%s次)",e.getMessage(),sleepMillis, retryTimes + 1));
                        Thread.sleep(sleepMillis);
                    } catch (InterruptedException e1) {
                        throw new RuntimeException(e1);
                    }
                } else {
                    throw e;
                }
            }
        } while (++retryTimes < maxRetryTimes);

        throw new RuntimeException("富友支付服务端异常，超出重试次数");
    }

    @Override
    public Map<String, Object> orderInfo(PayOrder order) {
        LinkedHashMap<String, Object> parameters = getOrderInfo(order);
        String sign = createSign(SignUtils.parameters2MD5Str(parameters, "|"), payConfigStorage.getInputCharset());
        parameters.put("md5", sign);
        return parameters;
    }

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

    @Override
    public String createSign(String content, String characterEncoding) {
        return SignUtils.valueOf(payConfigStorage.getSignType().toUpperCase()).createSign(content, "|" + payConfigStorage.getSecretKey(), characterEncoding);
    }

    @Override
    public Map<String, String> getParameter2Map(Map<String, String[]> parameterMap, InputStream is) {
        Map<String, String> params = new TreeMap<String, String>();
        for (Iterator iter = parameterMap.keySet().iterator(); iter.hasNext(); ) {
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

    @Override
    public PayOutMessage getPayOutMessage(String code, String message) {
        return PayOutMessage.TEXT().content(code.toLowerCase()).build();
    }

    @Override
    public String buildRequest(Map<String, Object> orderInfo, MethodType method) {
        return getFormString(orderInfo, method,fuiouSmpGate);
    }

    /**
     * 根据参数行程form表单
     * @param param 参数
     * @param method 请求方式 get_post
     * @param url
     * @return
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
     * 生成二维码支付
     * 暂未实现或无此功能
     *
     * @param orderInfo 发起支付的订单信息
     * @return
     */
    @Override
    public BufferedImage genQrPay(Map<String, Object> orderInfo) {
        throw new UnsupportedOperationException();
    }


    /**
     * 支付结果查询(直接返回)
     * <p>
     * 返回结果例子
     * <?xml version="1.0" encoding="UTF-8"?>
     * <ap><plain><order_pay_code>错误代码(0000表示成功 其他失败)</order_pay_code><order_pay_error>错误中文描述</order_pay_error><order_id>商户订单号</order_id><order_st>订单状态(‘00’ – 订单已生成(初始状态) ‘01’ – 订单已撤消 ‘02’ – 订单已合并 ‘03’ – 订单已过期 ‘04’ – 订单已确认(等待支付) ‘05’ – 订单支付失败 ‘11’ – 订单已支付 ‘18’ – 已发货 ‘19’ – 已确认收货)</order_st><fy_ssn>富友流水号</fy_ssn><resv1>保留字段</resv1></plain><md5>md5</md5></ap>
     * <p>
     * md5为plain域的内容+商户密钥做md5，不包括plain标签
     * 以下是MD5的内容
     * <order_pay_code>错误代码(0000表示成功 其他失败)</order_pay_code><order_pay_error>错误中文描述</order_pay_error><order_id>商户订单号</order_id><order_st>订单状态(‘00’ – 订单已生成(初始状态) ‘01’ – 订单已撤消 ‘02’ – 订单已合并 ‘03’ – 订单已过期 ‘04’ – 订单已确认(等待支付) ‘05’ – 订单支付失败 ‘11’ – 订单已支付 ‘18’ – 已发货 ‘19’ – 已确认收货)</order_st><fy_ssn>富友流水号</fy_ssn><resv1>保留字段</resv1>商户密钥
     *
     * @param order_id
     * @return
     */
    public JSONObject vaildatePayResult(String order_id) {
        LinkedHashMap param = new LinkedHashMap();
        param.put("mchnt_cd", payConfigStorage.getPartner());
        param.put("order_id", order_id);
        param.put("md5", createSign(SignUtils.parameters2MD5Str(param, "|"), payConfigStorage.getInputCharset()));
        try {
            String result = execute(new SimplePostRequestExecutor(), fuiouSmpAQueryGate, param);
            JSONObject object = XML.toJSONObject(result);
            return object;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


}
