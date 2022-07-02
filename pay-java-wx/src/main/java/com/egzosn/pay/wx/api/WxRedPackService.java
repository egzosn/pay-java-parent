package com.egzosn.pay.wx.api;

import com.egzosn.pay.wx.bean.RedpackOrder;

import java.util.Map;

/**
 * 微信红包服务
 * @author egan
 * <pre>
 * email egzosn@gmail.com
 * date 2020/5/17 22:24
 * </pre>
 */
public interface WxRedPackService {
    /**
     * 微信发红包
     *
     * @param redpackOrder 红包实体
     * @return 返回发红包实体后的结果
     * @see #sendRedPack(RedpackOrder)
     */
    @Deprecated
    Map<String, Object> sendredpack(RedpackOrder redpackOrder);
    /**
     * 微信发红包
     *
     * @param redpackOrder 红包实体
     * @return 返回发红包实体后的结果
     */
    Map<String, Object> sendRedPack(RedpackOrder redpackOrder);

    /**
     * 查询红包记录
     * 用于商户对已发放的红包进行查询红包的具体信息，可支持普通红包和裂变包
     * 查询红包记录API只支持查询30天内的红包订单，30天之前的红包订单请登录商户平台查询。
     *
     * @param mchBillno 商户发放红包的商户订单号
     * @return 返回查询结果
     * @see #getHbInfo(String)
     */
    @Deprecated
    Map<String, Object> gethbinfo(String mchBillno);
    /**
     * 查询红包记录
     * 用于商户对已发放的红包进行查询红包的具体信息，可支持普通红包和裂变包
     * 查询红包记录API只支持查询30天内的红包订单，30天之前的红包订单请登录商户平台查询。
     *
     * @param mchBillNo 商户发放红包的商户订单号
     * @return 返回查询结果
     */
    Map<String, Object> getHbInfo(String mchBillNo);
}
