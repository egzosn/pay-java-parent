package com.egzosn.pay.wx.api;

import com.egzosn.pay.common.api.BasePayConfigStorage;

/**
 * 支付客户端配置存储
 * @author  egan
 *
 * <pre>
 * email egzosn@gmail.com
 * date 2016-5-18 14:09:01
 * </pre>
 */
public class WxPayConfigStorage extends BasePayConfigStorage {


    public  String appSecret;
    public  String appid ;
    // 商户号 合作者id
    public  String mchId;




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
     * 合作商唯一标识
     *  @see #getPid()  代替者
     */
    public String getPartner() {
        return mchId;
    }

    /**
     * 合作商唯一标识
     * @see #getPartner()  代替者
     */
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
