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
    public  volatile String partner ;
    // 商户收款账号
    public  volatile String seller;
    private  volatile String ali_public_key;



    public String getAli_public_key() {
        return ali_public_key;
    }

    public void setAli_public_key(String ali_public_key) {
        this.ali_public_key = ali_public_key;
    }


    @Override
    public String getSecretKey() {
        return ali_public_key;
    }



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
