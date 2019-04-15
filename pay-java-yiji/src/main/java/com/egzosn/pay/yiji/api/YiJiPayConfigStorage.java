package com.egzosn.pay.yiji.api;

import com.egzosn.pay.common.api.BasePayConfigStorage;

/**
 * 易极付配置存储
 * @author  egan
 *
 * <pre>
 * email egzosn@gmail.com
 * date 2019/04/15 22:50
 * </pre>
 */
public class YiJiPayConfigStorage extends BasePayConfigStorage {


    /**
     *  	易极付分配的商户号 合作者id
     */
    private String partnerId;

    /**
     * 卖家id
     */
    private String  sellerUserId;

    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    @Override
    public String getAppid() {
        return null;
    }


    /**
     * 合作商唯一标识
     */
    @Override
    public String getPid() {
        return partnerId;
    }




    @Override
    public String getSeller() {
        return sellerUserId;
    }

    public String getSellerUserId() {
        return sellerUserId;
    }

    public void setSellerUserId(String sellerUserId) {
        this.sellerUserId = sellerUserId;
    }

    /**
     *  为商户平台设置的密钥key
     * @return 密钥
     */
    public String getSecretKey() {
        return getKeyPrivate();
    }

    public void setSecretKey(String secretKey) {
         setKeyPrivate(secretKey);
    }


}
