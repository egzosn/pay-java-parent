package com.egzosn.pay.common.bean;

/**
 * 默认的国家地区代码
 * @author egan
 *         email egzosn@gmail.com
 *         date 2019/4/16.22:43
 */
public enum  DefaultCountryCode implements CountryCode{

    CHN("中国"),
    USA("美国"),
    JPN("日本"),
    HKG("香港"),
    GBR("英国"),
    MAC("澳门"),
    TWN("中国台湾"),
    ;
    /**
     * 国家名称
     */
    private String name;

    DefaultCountryCode(String name) {
        this.name = name;
    }

    /**
     * 获取国家代码
     *
     * @return 国家代码
     */
    @Override
    public String getCode() {
        return this.name();
    }

    /**
     * 获取国家名称
     *
     * @return 国家名称
     */
    @Override
    public String getName() {
        return name;
    }
}
