package com.egzosn.pay.yiji.bean;

import com.egzosn.pay.common.bean.Bank;

/**
 * 对应的银行列表
 *
 * @author egan
 *         <pre>
 *         email egzosn@gmail.com
 *         date 2018/1/31
 *         </pre>
 */
public enum YiJiBank implements Bank {
    ABC("中国农业银行"),
    BOC("中国银行"),
    COMM("交通银行"),
    CCB("中国建设银行"),
    CEB("中国光大银行"),
    CIB("兴业银行"),
    CMB("招商银行"),
    CMBC("民生银行"),
    CITIC("中信银行"),
    CQRCB("重庆农村商业银行"),
    ICBC("中国工商银行"),
    PSBC("中国邮政储蓄银行"),
    SPDB("浦发银行"),
    UNION("中国银联"),
    CQCB("重庆银行"),
    GDB("广东发展银行"),
    SDB("深圳发展银行"),
    HXB("华夏银行"),
    CQTGB("重庆三峡银行"),
    PINGANBANK("平安银行"),
    BANKSH("上海银行"),;

    private String name;


    YiJiBank(String name) {
        this.name = name;
    }

    /**
     * 获取银行的代码
     *
     * @return 银行的代码
     */
    @Override
    public String getCode() {
        return this.name();
    }

    /**
     * 获取银行的名称
     *
     * @return 银行的名称
     */
    @Override
    public String getName() {
        return name;
    }
}
