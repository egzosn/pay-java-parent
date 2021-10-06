package com.egzosn.pay.wx.v3.bean.sharing;

/**
 * 子商户与接收方的关系
 *
 * @author Egan
 * <pre>
 * email egan@egzosn.com
 * date 2021/10/6
 * </pre>
 */
public enum RelationType {
        /**
         * 门店.
         */
        STORE,
        /**
         * 员工.
         */
        STAFF,
        /**
         * 店主.
         */
        STORE_OWNER,
        /**
         * 合作伙伴.
         */
        PARTNER,
        /**
         * 总部.
         */
        HEADQUARTER,
        /**
         * 品牌方.
         */
        BRAND,
        /**
         * 分销商.
         */
        DISTRIBUTOR,
        /**
         * 用户.
         */
        USER,
        /**
         * 供应商.
         */
        SUPPLIER,
        /**
         * 自定义.
         */
        CUSTOM
    }