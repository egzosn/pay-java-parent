package com.egzosn.pay.ali.bean;

/**
 * 常量
 * @author Egan
 * <pre>
 * email egzosn@gmail.com
 * date 2020/10/8
 * </pre>
 */
public final class AliPayConst {
    private AliPayConst() {
    }


    /**
     * 正式测试环境
     */
    public static final String HTTPS_REQ_URL = "https://openapi.alipay.com/gateway.do";
    /**
     * 沙箱测试环境账号
     */
    public static final String DEV_REQ_URL = "https://openapi-sandbox.dl.alipaydev.com/gateway.do";

    public static final String SIGN = "sign";

    public static final String SUCCESS_CODE = "10000";

    public static final String CODE = "code";
    /**
     * 附加参数
     */
    public static final String PASSBACK_PARAMS = "passback_params";
    /**
     * 产品代码
     */
    public static final String PRODUCT_CODE = "product_code";
    /**
     * 返回地址
     */
    public static final String RETURN_URL = "return_url";
    /**
     * 回调地址
     */
    public static final String NOTIFY_URL = "notify_url";

    /**
     * 请求内容
     */
    public static final String BIZ_CONTENT = "biz_content";
    /**
     * 应用授权概述
     */
    public static final String APP_AUTH_TOKEN = "app_auth_token";
    /**
     * 收款方信息
     */
    public static final String PAYEE_INFO = "payee_info";
    /**
     * 收款方信息
     */
    public static final String ALIPAY_CERT_SN_FIELD = "alipay_cert_sn";
    /**
     * 业务扩展参数
     */
    public static final String EXTEND_PARAMS = "extend_params";
    public static final String BIZ_TYPE = "biz_type";
    public static final String REFUND_REASON = "refund_reason";
    public static final String QUERY_OPTIONS = "query_options";
    public static final String OUT_REQUEST_NO = "out_request_no";
    /**
     *  	用户付款中途退出返回商户网站的地址
     */
    public static final String QUIT_URL = "quit_url";
    /**
     * 请求来源地址。如果使用ALIAPP的集成方式，用户中途取消支付会返回该地址。
     */
    public static final String REQUEST_FROM_URL = "request_from_url";
    public static final String DBACK_AMOUNT = "dback_amount";
}
