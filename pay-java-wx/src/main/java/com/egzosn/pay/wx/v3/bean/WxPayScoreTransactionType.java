package com.egzosn.pay.wx.v3.bean;

import com.egzosn.pay.common.bean.MethodType;
import com.egzosn.pay.common.bean.TransactionType;

public enum WxPayScoreTransactionType implements TransactionType {



    PERMISSIONS("/v3/payscore/permissions", MethodType.POST),

    QUERY_PERMISSIONS_AUTHORIZATION_CODE("/v3/payscore/permissions/authorization-code/{authorization_code}", MethodType.POST),

    UNBIND_PERMISSIONS_AUTHORIZATION_CODE("/v3/payscore/permissions/authorization-code/{authorization_code}/terminate", MethodType.POST),

    QUERY_PERMISSIONS_OPENID("/v3/payscore/permissions/openid/{openid}", MethodType.POST),

    UNBIND_PERMISSIONS_OPENID("/v3/payscore/permissions/openid/{openid}/terminate", MethodType.POST),

    CREATE("/v3/payscore/serviceorder", MethodType.POST),

    CANCEL("/v3/payscore/serviceorder/{out_order_no}/cancel", MethodType.POST),

    COMPLETE("/v3/payscore/serviceorder/{out_order_no}/complete", MethodType.POST),

    SYNC("/v3/payscore/serviceorder/{out_order_no}/sync", MethodType.POST),

    MODIFY("/v3/payscore/serviceorder/{out_order_no}/modify", MethodType.POST),

    QUERY("/v3/payscore/serviceorder", MethodType.GET),

    ;

    WxPayScoreTransactionType(String type, MethodType method) {
        this.type = type;
        this.method = method;
    }

    private String type;
    private MethodType method;



    @Override
    public String getType() {
        return type;
    }

    @Override
    public String getMethod() {
        return this.method.name();
    }

}
