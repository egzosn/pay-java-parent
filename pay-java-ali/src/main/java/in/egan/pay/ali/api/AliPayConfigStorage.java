package in.egan.pay.ali.api;

import in.egan.pay.common.api.BasePayConfigStorage;

/**
 * 支付客户端配置存储
 * @author  egan
 * @email egzosn@gmail.com
 * @date 2016-5-18 14:09:01
 */
public class AliPayConfigStorage extends BasePayConfigStorage {

    // 商户PID
    public volatile  String partner ;
    // 商户收款账号
    public volatile  String seller;
    //公钥
    private volatile String aliPublicKey;


    public String getAliPublicKey() {
        return aliPublicKey;
    }

    public void setAliPublicKey(String aliPublicKey) {
        setKeyPublic(aliPublicKey);
        this.aliPublicKey = aliPublicKey;
    }

    @Override
    public String getSecretKey() {
        return aliPublicKey;
    }



    @Override
    public String getAppid() {
        return null;
    }

    @Override
    public String getPartner() {
        return partner;
    }


    public void setPartner(String partner) {
        this.partner = partner;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }




}
