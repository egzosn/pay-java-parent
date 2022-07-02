package com.egzosn.pay.wx.youdian.api;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.locks.Lock;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.egzosn.pay.common.api.BasePayService;
import com.egzosn.pay.common.bean.AssistOrder;
import com.egzosn.pay.common.bean.BaseRefundResult;
import com.egzosn.pay.common.bean.BillType;
import com.egzosn.pay.common.bean.CurType;
import com.egzosn.pay.common.bean.MethodType;
import com.egzosn.pay.common.bean.NoticeParams;
import com.egzosn.pay.common.bean.PayMessage;
import com.egzosn.pay.common.bean.PayOrder;
import com.egzosn.pay.common.bean.PayOutMessage;
import com.egzosn.pay.common.bean.RefundOrder;
import com.egzosn.pay.common.bean.RefundResult;
import com.egzosn.pay.common.bean.TransactionType;
import com.egzosn.pay.common.bean.result.PayError;
import com.egzosn.pay.common.exception.PayErrorException;
import com.egzosn.pay.common.http.HttpConfigStorage;
import com.egzosn.pay.common.util.Util;
import com.egzosn.pay.common.util.sign.SignTextUtils;
import com.egzosn.pay.common.util.sign.SignUtils;
import com.egzosn.pay.common.util.str.StringUtils;
import com.egzosn.pay.wx.youdian.bean.WxYoudianPayMessage;
import com.egzosn.pay.wx.youdian.bean.YdPayError;
import com.egzosn.pay.wx.youdian.bean.YoudianTransactionType;

/**
 * 友店支付服务
 *
 * @author egan
 * <p>
 * email egzosn@gmail.com
 * date 2017/01/12 22:58
 */
public class WxYouDianPayService extends BasePayService<WxYouDianPayConfigStorage> {

    private static final String URL = "http://life.51youdian.com/Api/CheckoutCounter/";
    private static final String ACCESS_TOKEN = "access_token";
    private static final String RETURN_CODE = "return_code";
    private static final String ERROR_CODE = "errorcode";
    private static final String ORDER_SN = "order_sn";

    /**
     * 获取请求token
     *
     * @return 授权令牌
     */
    private String getAccessToken() {
        return getAccessToken(false);
    }

    /**
     * 获取授权令牌
     *
     * @param forceRefresh 是否重新获取， true重新获取
     * @return 新的授权令牌
     * @throws PayErrorException 支付异常
     */
    private String getAccessToken(boolean forceRefresh) throws PayErrorException {
        Lock lock = payConfigStorage.getAccessTokenLock();
        try {
            lock.lock();

            if (forceRefresh) {
                payConfigStorage.expireAccessToken();
            }

            if (payConfigStorage.isAccessTokenExpired()) {
                if (null == payConfigStorage.getAccessToken()) {
                    login();
                    return payConfigStorage.getAccessToken();
                }
                String apbNonce = SignTextUtils.randomStr();
                StringBuilder param = new StringBuilder().append("access_token=").append(payConfigStorage.getAccessToken());
                String sign = createSign(param.toString() + apbNonce, payConfigStorage.getInputCharset());
                param.append("&apb_nonce=").append(apbNonce).append("&sign=").append(sign);
                JSONObject json = execute(getReqUrl(YoudianTransactionType.RESET_LOGIN) + "?" + param.toString(), MethodType.GET, null);
                int errorcode = json.getIntValue(ERROR_CODE);
                if (0 == errorcode) {
                    payConfigStorage.updateAccessToken(payConfigStorage.getAccessToken(), 7200);
                }
                else {
                    throw new PayErrorException(new YdPayError(errorcode, json.getString("msg"), json.toJSONString()));
                }
            }
        }
        finally {
            lock.unlock();
        }
        return payConfigStorage.getAccessToken();
    }


    /**
     * 登录 并获取登陆信息(授权码)
     *
     * @return 登陆信息
     * @throws PayErrorException 支付异常
     */
    private JSONObject login() throws PayErrorException {
        TreeMap<String, String> data = new TreeMap<>();
        data.put("username", payConfigStorage.getSeller());
        data.put("password", payConfigStorage.getKeyPrivate());
        String apbNonce = SignTextUtils.randomStr();
//         1、确定请求主体为用户登录，即需要传登录的用户名username和密码password并且要生成唯一的随机数命名为apb_nonce，长度为32位
//         2、将所有的参数集进行key排序
//         3、将排序后的数组从起始位置拼接成字符串如：password=XXXXXXXusername=XXXXX
//         4、将拼接出来的字符串连接上apb_nonce的值即AAAAAAAAAA。再连接  password=XXXXXXXusername=XXXXXAAAAAAAAAA
        String sign = createSign(SignTextUtils.parameterText(data, "") + apbNonce, payConfigStorage.getInputCharset());
        String queryParam = SignTextUtils.parameterText(data) + "&apb_nonce=" + apbNonce + "&sign=" + sign;

        JSONObject json = execute(getReqUrl(YoudianTransactionType.LOGIN) + "?" + queryParam, MethodType.GET, null);
        payConfigStorage.updateAccessToken(json.getString(ACCESS_TOKEN), json.getLongValue("viptime"));
        return json;
    }


    /**
     * 回调校验
     *
     * @param params 回调回来的参数集
     * @return 签名校验 true通过
     */
    @Deprecated
    @Override
    public boolean verify(Map<String, Object> params) {

        return verify(new NoticeParams(params));
    }

    /**
     * 回调校验
     *
     * @param noticeParams 回调回来的参数集
     * @return 签名校验 true通过
     */
    @Override
    public boolean verify(NoticeParams noticeParams) {
        final Map<String, Object> params = noticeParams.getBody();

        if (!"SUCCESS".equals(params.get(RETURN_CODE))) {
            LOG.debug("友店微信支付异常：return_code={},参数集={}", params.get(RETURN_CODE), params);
            return false;
        }
        if (params.get("sign") == null) {
            LOG.debug("友店微信支付异常：签名为空！out_trade_no={}", params.get("out_trade_no"));
        }

        return signVerify(params, (String) params.get("sign")) && verifySource((String) params.get("out_trade_no"));
    }

    /**
     * 根据反馈回来的信息，生成签名结果
     *
     * @param params 通知返回来的参数数组
     * @param sign   比对的签名结果
     * @return 生成的签名结果
     */
    private boolean signVerify(Map<String, Object> params, String sign) {
        return SignUtils.valueOf(payConfigStorage.getSignType()).verify(params, sign, "&key=" + payConfigStorage.getKeyPublic(), payConfigStorage.getInputCharset());
    }


    /**
     * 验证链接来源是否有效
     * 校验数据来源
     *
     * @param id id 商户订单号（扫码收款返回的order_sn）
     * @return true通过
     */
    private boolean verifySource(String id) {

        try {
            JSONObject jsonObject = (JSONObject) query(id, null);

            return 0 == jsonObject.getIntValue(ERROR_CODE);
        }
        catch (PayErrorException e) {
            if (Integer.parseInt(e.getPayError().getErrorCode()) >= 400) {
                throw e;
            }
            return false;
        }

    }


    /**
     * 向友店端发送请求，在这里执行的策略是当发生access_token过期时才去刷新，然后重新执行请求，而不是全局定时请求
     *
     * @param uri     请求地址
     * @param method  请求方式
     * @param request 请求内容，GET无需
     * @return 请求成功后的结果
     * @throws PayErrorException 支付异常
     * @see MethodType#GET
     * @see MethodType#POST
     */
    private JSONObject execute(String uri, MethodType method, Object request) throws PayErrorException {
        int retryTimes = 0;
        do {
            try {
                JSONObject result = requestTemplate.doExecute(uri, request, JSONObject.class, method);
                if (0 != result.getIntValue(ERROR_CODE)) {
                    throw new PayErrorException(new YdPayError(result.getIntValue(ERROR_CODE), result.getString("msg"), result.toJSONString()));
                }
                return result;
            }
            catch (PayErrorException e) {
                PayError error = e.getPayError();
                if ("401".equals(error.getErrorCode()) || "500".equals(error.getErrorCode())) {
                    try {
                        int sleepMillis = retrySleepMillis * (1 << retryTimes);
                        LOG.debug(String.format("友店微信系统繁忙，(%s)ms 后重试(第%s次)", sleepMillis, retryTimes + 1));
                        Thread.sleep(sleepMillis);
                    }
                    catch (InterruptedException e1) {
                        throw new PayErrorException(new YdPayError(-1, "友店支付服务端重试失败", e1.getMessage()));
                    }
                    // 强制设置wxMpConfigStorage它的access token过期了，这样在下一次请求里就会刷新access token
                    payConfigStorage.expireAccessToken();
                    //进行重新登陆授权
                    login();
                }
                else {
                    throw e;
                }
            }

        } while (++retryTimes < maxRetryTimes);

        throw new PayErrorException(new YdPayError(-1, "友店微信服务端异常，超出重试次数"));
    }


    /**
     * 获取支付平台所需的订单信息
     *
     * @param order 支付订单
     * @return 订单信息
     * @see PayOrder 支付订单信息
     */
    @Override
    public JSONObject orderInfo(PayOrder order) {
        Map<String, Object> data = new TreeMap<>();
        data.put(ACCESS_TOKEN, getAccessToken());
        data.put("paymoney", Util.conversionAmount(order.getPrice()).toString());
        data.putAll(order.getAttrs());
        data = preOrderHandler(data, order);
        String apbNonce = SignTextUtils.randomStr();
        String sign = createSign(SignTextUtils.parameterText(data, "") + apbNonce, payConfigStorage.getInputCharset());
        data.put("PayMoney", data.remove("paymoney"));
        String params = SignTextUtils.parameterText(data) + "&apb_nonce=" + apbNonce + "&sign=" + sign;
        JSONObject json = execute(getReqUrl(order.getTransactionType()) + "?" + params, MethodType.GET, null);
        //友店比较特殊，需要在下完预订单后，自己存储 order_sn 对应 微信官方文档 out_trade_no
        order.setTradeNo(json.getString(ORDER_SN));
        return json;
    }


    /**
     * 签名
     *
     * @param content           需要签名的内容
     * @param characterEncoding 字符编码
     *                          <p>
     *                          1、确定请求主体为用户登录，即需要传登录的用户名username和密码password并且要生成唯一的随机数命名为apb_nonce，长度为32位
     *                          2、将所有的参数集进行key排序
     *                          3、将排序后的数组从起始位置拼接成字符串如：password=XXXXXXXusername=XXXXX
     *                          4、将拼接出来的字符串连接上apb_nonce的值即AAAAAAAAAA。再连接  password=XXXXXXXusername=XXXXXAAAAAAAAAA
     * @return 签名结果
     */
    @Override
    public String createSign(String content, String characterEncoding) {
        return SignUtils.valueOf(payConfigStorage.getSignType().toUpperCase()).createSign(content, "&source=http://life.51youdian.com", characterEncoding);
    }


    /**
     * 具体需要返回的数据为
     * return_code 返回码只有SUCCESS和FAIL
     * return_msg 返回具体信息
     * nonce_str 您的服务器新生成随机生成32位字符串
     * sign 为签名，签名规则是您需要发送的所有数据(除了sign)按照字典升序排列后加上&amp;key=xxxxxxxx您的密钥后md5加密，最后转成小写
     * 最后把得到的所有需要返回的数据用json格式化成json对象格式如下
     * {&quot;return_code&quot;:&quot;SUCCESS&quot;,&quot;return_msg&quot;:&quot;ok&quot;,&quot;nonce_str&quot;:&quot;dddddddddddddddddddd’,’sign’:’sdddddddddddddddddd&quot;}
     *
     * @param code    return_code
     * @param message return_msg
     * @return 返回输出消息
     */
    @Override
    public PayOutMessage getPayOutMessage(String code, String message) {
        Map<String, Object> builder = new TreeMap<>();
        builder.put(RETURN_CODE, code.toUpperCase());
        builder.put("return_msg", message);
        builder.put("nonce_str", SignTextUtils.randomStr());
        String sgin = SignUtils.valueOf(payConfigStorage.getSignType()).sign(builder, "&key=" + payConfigStorage.getKeyPrivate(), payConfigStorage.getInputCharset());
        return PayOutMessage.TEXT().content("{\"return_code\":\"" + builder.get(RETURN_CODE) + "\",\"return_msg\":\"" + builder.get("return_msg") + "\",\"nonce_str\":\"" + builder.get("nonce_str") + "\",\"sign\":\"" + sgin + "\"}").build();
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
        return PayOutMessage.TEXT().content(JSON.toJSONString(payMessage.getPayMessage())).build();
    }

    /**
     * 针对web端的即时付款
     * 暂未实现或无此功能
     *
     * @param orderInfo 发起支付的订单信息
     * @param method    请求方式  &quot;post&quot; &quot;get&quot;,
     * @return 获取输出消息，用户返回给支付端, 针对于web端
     * @see MethodType 请求类型
     */
    @Override
    public String buildRequest(Map<String, Object> orderInfo, MethodType method) {
        throw new UnsupportedOperationException();
    }


    @Override
    public String getQrPay(PayOrder order) {
        order.setTransactionType(YoudianTransactionType.NATIVE);
        JSONObject orderInfo = orderInfo(order);
        return (String) orderInfo.get("code_url");
    }

    /**
     * 暂未实现或无此功能
     *
     * @param order 发起支付的订单信息
     * @return 返回支付结果
     */
    @Override
    public Map<String, Object> microPay(PayOrder order) {
        order.setTransactionType(YoudianTransactionType.MICROPAY);
        return orderInfo(order);
    }

    /**
     * 交易查询接口
     *
     * @param tradeNo    支付平台订单号
     * @param outTradeNo 商户单号
     * @return 返回查询回来的结果集，支付方原值返回
     */
    @Override
    public Map<String, Object> query(String tradeNo, String outTradeNo) {
        return query(new AssistOrder(tradeNo, outTradeNo));
    }

    /**
     * 交易查询接口
     *
     * @param assistOrder 查询条件
     * @return 返回查询回来的结果集，支付方原值返回
     */
    @Override
    public Map<String, Object> query(AssistOrder assistOrder) {
        String apbNonce = SignTextUtils.randomStr();
        TreeMap<String, String> data = new TreeMap<>();
        data.put(ACCESS_TOKEN, payConfigStorage.getAccessToken());

        if (StringUtils.isEmpty(assistOrder.getTradeNo())) {
            data.put(ORDER_SN, assistOrder.getOutTradeNo());
        }
        else {
            data.put(ORDER_SN, assistOrder.getTradeNo());
        }
        String sign = createSign(SignTextUtils.parameterText(data, "") + apbNonce, payConfigStorage.getInputCharset());
        String queryParam = SignTextUtils.parameterText(data) + "&apb_nonce=" + apbNonce + "&sign=" + sign;
        return execute(getReqUrl(YoudianTransactionType.NATIVE_STATUS) + "?" + queryParam, MethodType.GET, null);
    }


    @Override
    public Map<String, Object> close(String tradeNo, String outTradeNo) {
        return Collections.emptyMap();
    }

    /**
     * 交易关闭接口
     *
     * @param assistOrder 关闭订单
     * @return 返回支付方交易关闭后的结果
     */
    @Override
    public Map<String, Object> close(AssistOrder assistOrder) {
        return Collections.emptyMap();
    }

    /**
     * 申请退款接口
     *
     * @param refundOrder 退款订单信息
     * @return 返回支付方申请退款后的结果
     */
    @Override
    public RefundResult refund(RefundOrder refundOrder) {
        String apbNonce = SignTextUtils.randomStr();
        TreeMap<String, String> data = new TreeMap<>();
        data.put(ACCESS_TOKEN, payConfigStorage.getAccessToken());

        if (StringUtils.isEmpty(refundOrder.getOutTradeNo())) {
            data.put(ORDER_SN, refundOrder.getOutTradeNo());
        }
        else {
            data.put(ORDER_SN, refundOrder.getTradeNo());
        }
        //支付类型刷卡为3扫码为4
        data.put("type", "4");
        data.put("refund_fee", refundOrder.getRefundAmount().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        String sign = createSign(SignTextUtils.parameterText(data, "") + apbNonce, payConfigStorage.getInputCharset());
        String queryParam = SignTextUtils.parameterText(data) + "&apb_nonce=" + apbNonce + "&sign=" + sign;
        JSONObject jsonObject = execute(getReqUrl(YoudianTransactionType.REFUND) + "?" + queryParam, MethodType.GET, null);
        return new BaseRefundResult(jsonObject) {
            @Override
            public String getCode() {
                return getAttrString(ERROR_CODE);
            }

            @Override
            public String getMsg() {
                return getAttrString("msg");
            }

            @Override
            public String getResultCode() {
                return null;
            }

            @Override
            public String getResultMsg() {
                return null;
            }

            @Override
            public BigDecimal getRefundFee() {
                return null;
            }

            @Override
            public CurType getRefundCurrency() {
                return null;
            }

            @Override
            public String getTradeNo() {
                return null;
            }

            @Override
            public String getOutTradeNo() {
                return null;
            }

            @Override
            public String getRefundNo() {
                return null;
            }
        };
    }


    /**
     * 查询退款
     *
     * @param refundOrder 退款订单单号信息
     * @return 返回支付方查询退款后的结果
     */
    @Override
    public Map<String, Object> refundquery(RefundOrder refundOrder) {
        return Collections.emptyMap();
    }


    @Override
    public Map<String, Object> downloadBill(Date billDate, BillType billType) {
        return Collections.emptyMap();
    }


    public WxYouDianPayService(WxYouDianPayConfigStorage payConfigStorage) {
        super(payConfigStorage);
    }

    public WxYouDianPayService(WxYouDianPayConfigStorage payConfigStorage, HttpConfigStorage configStorage) {
        super(payConfigStorage, configStorage);
    }

    /**
     * 根据交易类型获取请求地址
     *
     * @param type 交易类型
     * @return 请求地址
     */
    @Override
    public String getReqUrl(TransactionType type) {
        return URL + type.getMethod();

    }

    /**
     * 创建消息
     *
     * @param message 支付平台返回的消息
     * @return 支付消息对象
     */
    @Override
    public PayMessage createMessage(Map<String, Object> message) {
        return WxYoudianPayMessage.create(message);
    }
}
