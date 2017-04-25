package in.egan.pay.fuiou.api;/**
 * Created by Fuzx on 2017/1/16 0016.
 */

import in.egan.pay.common.api.BasePayConfigStorage;

/**
 * @author Fuzx
 * @create 2017 2017/1/16 0016
 */
public class FuiouPayConfigStorage extends BasePayConfigStorage {

    public String mchntCd;//商户代码
    public String mchntKey;//商户密钥

    /**
     *  应用id
     * @return 空
     */
    @Override
    public String getAppid() {
        return null;
    }

    /**
     * 合作商唯一标识
     *
     * @see #getPid()  代替者
     */
    @Override
    public String getPartner () {
        return mchntCd;
    }

    /**
     * 合作商唯一标识
     *
     * @see #getPartner()  代替者
     */
    @Override
    public String getPid () {
        return mchntCd;
    }

    public String getMchntCd () {
        return mchntCd;
    }

    public void setMchntCd (String mchntCd) {
        this.mchntCd = mchntCd;
    }

    @Override
    public String getSeller() {
        return null;
    }

    @Override
    public String getSecretKey() {
        return super.getKeyPrivate();
    }

    public void setSecretKey(String mchntKey){
        this.mchntKey = mchntKey;
    }
}
