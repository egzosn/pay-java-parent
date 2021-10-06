package com.egzosn.pay.wx.v3.utils;

import com.egzosn.pay.wx.v3.api.WxPayService;

/**
 * 微信所需常量
 *
 * @author Egan
 * email egzosn@gmail.com
 * date 2021/8/1
 */
public final class WxConst {

    private WxConst() {
    }

    /**
     * 微信默认请求地址
     */
    public static final String URI = "https://api.mch.weixin.qq.com";
    /**
     * 证书别名
     */
    public static final String CERT_ALIAS = "Tenpay Certificate";
    /**
     * 加密算法提供方 - BouncyCastle
     */
    public static final String BC_PROVIDER = "BC";

    /**
     * 沙箱
     */
    public static final String SANDBOXNEW = "sandboxnew/";
    public static final String COMBINE = "combine_";
    public static final String APPID = "appid";

    public static final String COMBINE_APPID = COMBINE + APPID;
    public static final String MCH_ID = "mchid";
    public static final String COMBINE_MCH_ID = COMBINE + MCH_ID;
    public static final String SUB_MCH_ID = "sub_mchid";
    public static final String SP_MCH_ID = "sp_mchid";
    public static final String OUT_TRADE_NO = "out_trade_no";
    public static final String COMBINE_OUT_TRADE_NO = COMBINE + OUT_TRADE_NO;
    public static final String NOTIFY_URL = "notify_url";
    public static final String TIME_START = "time_start";
    public static final String TIME_EXPIRE = "time_expire";
    public static final String SUB_ORDERS = "sub_orders";

    public static final String TOKEN_PATTERN = "mchid=\"%s\",nonce_str=\"%s\",timestamp=\"%d\",serial_no=\"%s\",signature=\"%s\"";

    public static final String SCHEMA = "WECHATPAY2-SHA256-RSA2048 ";

    public static final String CODE = "code";
    public static final String MESSAGE = "message";
    public static final String SCENE_INFO = "scene_info";
    public static final String FAILURE = "failure";

    public static final String RESP_BODY = WxPayService.class.getName() + "$RESP_BODY";
}
