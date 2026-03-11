package com.egzosn.pay.wx.v3.bean.transfer;

public enum TransferSceneType {
    CASH_MARKETING(1000, "现金营销"),
    CORPORATE_COMPENSATION(1011, "企业赔付"),
    COMMISSION_PAYMENT(1005, "佣金报酬"),
    PURCHASE_PAYMENT(1009, "采购货款"),
    SECOND_HAND_RECYCLING(1010, "二手回收"),
    PUBLIC_WELFARE_SUBSIDY(1013, "公益补助"),
    ADMINISTRATIVE_SUBSIDY(1002, "行政补贴"),
    INSURANCE_CLAIM(1004, "保险理赔")
    ;
    private int code;
    private String desc;
    TransferSceneType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

}
