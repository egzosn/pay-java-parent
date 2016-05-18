package in.egan.pay.ali.api;

import in.egan.pay.common.api.PayConfigStorage;

/**
 * 支付客户端配置存储
 * @author  egan
 * @email egzosn@gmail.com
 * @date 2016-5-18 14:09:01
 */
public class AliPayConfigStorage implements PayConfigStorage{

    // 商户PID
    public static final String partner = "";
    // 商户收款账号
    public static final String seller = "";
    // 商户私钥，pkcs8格式
    public static final String rsa_private = "";
    // 支付宝公钥
    public static final String rsa_public = "";

    private static final int sdk_pay_flag = 1;
    private static final String notify_url = "";


    @Override
    public String getAppid() {
        return null;
    }

    @Override
    public String getPartner() {
        return partner;
    }

    @Override
    public String getToken() {
        return null;
    }

    @Override
    public String getNotify_url() {
        return notify_url;
    }

    @Override
    public String getSign_type() {
        return null;
    }
}
