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
    public static final String SUB_APPID = "sub_appid";
    public static final String TRANSACTION_ID = "transaction_id";
    public static final String OUT_ORDER_NO = "out_order_no";
    public static final String TYPE = "type";
    public static final String ACCOUNT = "account";
    public static final String NAME = "name";
    public static final String RELATION_TYPE = "relationType";
    public static final String CUSTOM_RELATION = "customRelation";
    public static final String DESCRIPTION = "description";
    public static final String BILL_DATE = "bill_date";
    public static final String TAR_TYPE = "tar_type";

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
    public static final String RECEIVERS = "receivers";
    public static final String UNFREEZE_UNSPLIT = "unfreeze_unsplit";

    public static final String TOKEN_PATTERN = "mchid=\"%s\",nonce_str=\"%s\",timestamp=\"%d\",serial_no=\"%s\",signature=\"%s\"";

    public static final String SCHEMA = "WECHATPAY2-SHA256-RSA2048 ";

    public static final String CODE = "code";
    public static final String MESSAGE = "message";
    public static final String SCENE_INFO = "scene_info";
    public static final String FAILURE = "failure";

    public static final String RESP_BODY = WxPayService.class.getName() + "$RESP_BODY";
    public static final String OUT_BATCH_NO = "out_batch_no";
    public static final String OUT_DETAIL_NO = "out_detail_no";
    public static final String DETAIL_ID = "detail_id";
    public static final String BATCH_NAME = "batch_name";
    public static final String BATCH_REMARK = "batch_remark";
    public static final String TRANSFER_DETAIL_LIST = "transfer_detail_list";
    public static final String TOTAL_AMOUNT = "total_amount";
    public static final String TOTAL_NUM = "total_num";
    public static final String TRANSFER_SCENE_ID = "transfer_scene_id";
    public static final String BATCH_ID = "batch_id";
    public static final String NEED_QUERY_DETAIL = "need_query_detail";
    public static final String OFFSET = "offset";
    public static final String LIMIT = "limit";
    public static final String DETAIL_STATUS = "detail_status";
    public static final String WECHATPAY_SERIAL = "Wechatpay-Serial";
    public static final String AUTHORIZATION_CODE = "authorization_code";
}
