package com.egzosn.pay.wx.v3.bean.order;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 商户门店信息
 * @author Egan0
 * email egzosn@gmail.com
 * date 2021/8/1
 */
public class StoreInfo {
    /**
     * 商户侧门店编号
     */
    private String id;
    /**
     * 商户侧门店名称
     */
    private String name;
    /**
     * 地区编码，详细请见省市区编号对照表。
     * https://pay.weixin.qq.com/wiki/doc/apiv3/terms_definition/chapter1_1_3.shtml#part-5
     */
    @JSONField(name = "area_code")
    private String areaCode;
    /**
     * 详细的商户门店地址
     */
    private String address;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
