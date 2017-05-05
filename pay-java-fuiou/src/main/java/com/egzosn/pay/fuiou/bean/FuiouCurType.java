package com.egzosn.pay.fuiou.bean;/**
 * Created by Fuzx on 2017/1/24 0024.
 */

import com.egzosn.pay.common.bean.CurType;

/**
 * 货币类型
 * @author Fuzx
 * @create 2017 2017/1/24 0024
 */
public enum FuiouCurType  implements CurType {

        CNY("人民币"),
     	USD("美元"),
     	HKD("港币"),
    	MOP("澳门元"),
        EUR("欧元"),
    	TWD("新台币"),
      	KRW("韩元"),
        JPY("日元"),
    	SGD("新加坡元"),
        AUD("澳大利亚元");
    /**
     * 币种名称
     */
    private String name;
    //索引
    private int index;

    /**
     * 构造函数
     * @param name
     */
    FuiouCurType(String name) {
        this.name = name;
    }

    /**
     * 获取币种名称
     * @return 币种名称
     */
    @Override
    public String getCurType(){
        return this.name();
    }

    }
