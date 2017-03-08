package in.egan.pay.wx.api;

import in.egan.pay.common.api.BasePayConfigStorage;

/**
 * 支付客户端配置存储
 * @author  egan
 * @email egzosn@gmail.com
 * @date 2016-5-18 14:09:01
 */
public class WxPayConfigStorage extends BasePayConfigStorage {


    public  String appSecret;
    public  String appid ;
    // 商户号 合作者id
    public  String mchId;


    @Override
    public String getSecretKey() {
        return appSecret;
    }

    public void setAppSecret(String appSecret) {
        this.appSecret = appSecret;
    }

    @Override
    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    /**
     *
     * @return
     *
     */
    @Deprecated
    @Override
    public String getPartner() {
        return mchId;
    }

    @Override
    public String getPid() {
        return mchId;
    }


    @Override
    public String getSeller() {
        return null;
    }


    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }




}
