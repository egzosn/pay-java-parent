package com.egzosn.pay.fuiou.api;
import com.egzosn.pay.common.api.BasePayConfigStorage;

/**
 * @author Actinia
 *  <pre>
 * email hayesfu@qq.com
 * create 2017 2017/1/16 0016
 * </pre>
 */
public class FuiouPayConfigStorage extends BasePayConfigStorage {
    /**
     * 商户代码
     */
    private String mchntCd;

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

}
