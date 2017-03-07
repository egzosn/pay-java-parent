package in.egan.pay.fuiou.api;/**
 * Created by Fuzx on 2017/1/16 0016.
 */

import in.egan.pay.common.before.api.BasePayConfigStorage;

/**
 * @author Fuzx
 * @create 2017 2017/1/16 0016
 */
public class FuiouPayConfigStorage extends BasePayConfigStorage {

    public String mchntCd;//商户代码
    public String mchntKey;//商户密钥


    @Override
    public String getAppid() {
        return null;
    }

    @Override
    public String getPartner() {
        return mchntCd;
    }

    public void setPartner(String partner) {
        this.mchntCd = partner;
    }

    @Override
    public String getSeller() {
        return null;
    }

    @Override
    public String getSecretKey() {
        return mchntKey;
    }

    public void setSecretKey(String mchntKey){
        this.mchntKey = mchntKey;
    }
}
