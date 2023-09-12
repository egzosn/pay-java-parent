package com.egzosn.pay.wx.api;

/**
 * 常量
 * @author egan
 * <pre>
 * email egzosn@gmail.com
 * date 2020/3/10 21:22
 * </pre>
 */
public interface WxConst {
    /**
     * 微信请求地址
     */
    String URI = "https://api.mch.weixin.qq.com/";
    /**
     * 沙箱
     */
    String SANDBOXNEW = "xdc/apiv2sandbox/";

    String SUCCESS = "SUCCESS";
    String FAIL = "FAIL";
    String RETURN_CODE = "return_code";
    String SIGN = "sign";
    String CIPHER_ALGORITHM = "RSA/ECB/OAEPWITHSHA-1ANDMGF1PADDING";
    String FAILURE = "failure";
    String APPID = "appid";
    String HMAC_SHA256 = "HMAC-SHA256";
    String HMACSHA256 = "HMACSHA256";
    String RETURN_MSG_CODE = "return_msg";
    String RESULT_CODE = "result_code";
    String MCH_ID = "mch_id";
    String NONCE_STR = "nonce_str";
    String OUT_TRADE_NO = "out_trade_no";
    String GZIP = "GZIP";
    String BILL_DATE = "bill_date";
    String REQ_INFO = "req_info";
    String NOTIFY_URL = "notify_url";

}
