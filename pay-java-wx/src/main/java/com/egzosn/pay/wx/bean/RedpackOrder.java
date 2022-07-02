package com.egzosn.pay.wx.bean;

import java.math.BigDecimal;

import com.egzosn.pay.common.bean.TransferOrder;

/**
 * 发红包订单
 *
 * @author 保网 faymanwang 1057438332@qq.com
 * 2020/5/15 12:40
 */
public class RedpackOrder extends TransferOrder {
    /**
     * 微信为发放红包商户分配的公众账号ID，接口传入的appid应该为公众号的appid或小程序的appid（在mp.weixin.qq.com申请的）或APP的appid（在open.weixin.qq.com申请的）。
     * 校验规则：
     * 1、该appid需要与接口传入中的re_openid有对应关系；
     * 2、该appid需要与发放红包商户号有绑定关系，若未绑定，可参考该指引完成绑定（商家商户号与AppID账号关联管理）
     */
    private String wxAppId;
    /**
     * 商户订单号（每个订单号必须唯一。取值范围：0~9，a~z，A~Z）
     * 接口根据商户订单号支持重入，如出现超时可再调用。
     */
    private String mchBillNo;

    /**
     * 商户订单号（每个订单号必须唯一。取值范围：0~9，a~z，A~Z）
     * 接口根据商户订单号支持重入，如出现超时可再调用
     *
     * @return 商户订单号
     */
    @Deprecated
    public String getMchBillno() {
        return getMchBillNo();
    }

    @Deprecated
    public void setMchBillno(String mchBillno) {
        setMchBillNo(mchBillno);
    }

    public String getMchBillNo() {
        return mchBillNo;
    }

    public void setMchBillNo(String mchBillNo) {
        setOutNo(mchBillNo);
        this.mchBillNo = mchBillNo;
    }

    /**
     * 商户名称:红包发送者名称
     *
     * @return 红包发送者名称
     */
    public String getSendName() {
        return getPayerName();
    }

    public void setSendName(String sendName) {
        super.setPayerName(sendName);
    }

    /**
     * 用户openid
     *
     * @return 用户openid
     */
    public String getReOpenid() {
        return getPayeeAccount();
    }

    public void setReOpenid(String reOpenid) {
        super.setPayeeAccount(reOpenid);
    }

    /**
     * 付款金额 每个红包金额必须在默认额度内（默认大于1元，小于200元，可在产品设置中自行申请调高额度）
     *
     * @return 付款金额
     */
    public BigDecimal getTotalAmount() {
        return getAmount();
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        super.setAmount(totalAmount);
    }

    /**
     * 红包发放总人数
     * 普通红包：1
     * 裂变：必须介于(包括)3到20之间
     *
     * @return 红包发放总人数
     */
    public int getTotalNum() {
        Object totalNum = getAttr("total_num");
        return null == totalNum ? 1 : (Integer) totalNum;
    }

    public void setTotalNum(int totalNum) {
        addAttr("total_num", totalNum);
    }

    /**
     * 红包祝福语
     *
     * @return 红包祝福语
     */
    public String getWishing() {
        return (String) getAttr("wishing");

    }

    public void setWishing(String wishing) {
        addAttr("wishing", wishing);
    }


    /**
     * 活动名称
     *
     * @return 活动名称
     */
    public String getActName() {
        return (String) getAttr("act_name");
    }

    public void setActName(String actName) {
        addAttr("act_name", actName);
    }

    public String getSceneId() {
        return (String) getAttr("scene_id");
    }

    /**
     * 发放红包使用场景，红包金额大于200或者小于1元时必传
     * PRODUCT_1:商品促销
     * PRODUCT_2:抽奖
     * PRODUCT_3:虚拟物品兑奖
     * PRODUCT_4:企业内部福利
     * PRODUCT_5:渠道分润
     * PRODUCT_6:保险回馈
     * PRODUCT_7:彩票派奖
     * PRODUCT_8:税务刮奖
     *
     * @param sceneId 红包使用场景
     */
    public void setSceneId(String sceneId) {
        addAttr("scene_id", sceneId);
    }


    public void setTransferType(WxSendredpackType transferType) {
        super.setTransferType(transferType);
    }

    public String getWxAppId() {
        return wxAppId;
    }

    public void setWxAppId(String wxAppId) {
        addAttr("wxappid", wxAppId);
        this.wxAppId = wxAppId;
    }
}
