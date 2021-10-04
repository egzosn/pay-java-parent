package com.egzosn.pay.wx.v3.bean.order;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 支付场景信息描述
 * @author Egan
 * <pre>
 * email egzosn@gmail.com
 * date 2021/8/1
 * </pre>
 */
public class SceneInfo {

    /**
     * 用户的客户端IP，支持IPv4和IPv6两种格式的IP地址。
     */
    @JSONField(name = "payer_client_ip")
    private String payerClientIp;
    /**
     * 商户端设备号（门店号或收银设备ID）。
     */
    @JSONField(name = "device_id")
    private String deviceId;

    /**
     * 商户门店信息
     */
    @JSONField(name = "store_info ")
    private StoreInfo storeInfo;
    /**
     * H5场景信息
     */
    @JSONField(name = "h5_info")
    private H5Info h5Info;



    public String getPayerClientIp() {
        return payerClientIp;
    }

    public void setPayerClientIp(String payerClientIp) {
        this.payerClientIp = payerClientIp;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public StoreInfo getStoreInfo() {
        return storeInfo;
    }

    public void setStoreInfo(StoreInfo storeInfo) {
        this.storeInfo = storeInfo;
    }

    public H5Info getH5Info() {
        return h5Info;
    }

    public void setH5Info(H5Info h5Info) {
        this.h5Info = h5Info;
    }
}
