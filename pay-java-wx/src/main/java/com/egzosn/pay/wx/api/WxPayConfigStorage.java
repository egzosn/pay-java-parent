package com.egzosn.pay.wx.api;

import com.egzosn.pay.common.api.BasePayConfigStorage;

/**
 * 微信配置存储
 * @author  egan
 *
 * <pre>
 * email egzosn@gmail.com
 * date 2016-5-18 14:09:01
 * </pre>
 */
public class WxPayConfigStorage extends BasePayConfigStorage {


    public  String appSecret;
    /**
     * 应用id
     */
    public  String appid ;
    /**
     *  商户号 合作者id
     */
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
     */
    @Override
    public String getPid() {
        return mchId;
    }


    /**
     * 合作商唯一标识
     */
    public void setPid(String mchId) {
         this.mchId = mchId;
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
