package com.egzosn.pay.baidu.bean.type;

public enum AuditStatus {
    SUCCESS(1, "审核通过可退款"),
    FAIL(2, "审核不通过，不能退款"),
    UNKNOWN(3, "审核结果不确定，待重试");
    private final int code;
    private final String desc;
    
    AuditStatus(int code, String desc) {
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
