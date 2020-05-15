package com.egzosn.pay.wx.bean;

import java.math.BigDecimal;

/**
 * @description:
 * @author: 保网 faymanwang 1057438332@qq.com
 * @time: 2020/5/15 12:40
 */
public class RedpackOrder {
    /**
     * 商户订单号（每个订单号必须唯一。取值范围：0~9，a~z，A~Z）
     * 接口根据商户订单号支持重入，如出现超时可再调用
     */
    private String mchBillno;

    /**
     * 商户名称:红包发送者名称
     */
    private String sendName;

    /**
     * 用户openid
     */
    private String reOpenid;

    /**
     * 付款金额 每个红包金额必须在默认额度内（默认大于1元，小于200元，可在产品设置中自行申请调高额度）
     */
    private BigDecimal totalAmount;

    /**
     * 红包发放总人数
     * 普通红包：1
     * 裂变：必须介于(包括)3到20之间
     */
    private int totalNum;

    /**
     * 红包祝福语
     */
    private String wishing;

    /**
     * 操作者ip，根据支付平台所需进行设置
     */
    private String ip;

    /**
     * 活动名称
     */
    private String actName;

    /**
     * 备注
     */
    private String remark;

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
     */
    private String sceneId;


    public String getMchBillno() {
        return mchBillno;
    }

    public void setMchBillno(String mchBillno) {
        this.mchBillno = mchBillno;
    }

    public String getSendName() {
        return sendName;
    }

    public void setSendName(String sendName) {
        this.sendName = sendName;
    }

    public String getReOpenid() {
        return reOpenid;
    }

    public void setReOpenid(String reOpenid) {
        this.reOpenid = reOpenid;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    public String getWishing() {
        return wishing;
    }

    public void setWishing(String wishing) {
        this.wishing = wishing;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getActName() {
        return actName;
    }

    public void setActName(String actName) {
        this.actName = actName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getSceneId() {
        return sceneId;
    }

    public void setSceneId(String sceneId) {
        this.sceneId = sceneId;
    }
}
