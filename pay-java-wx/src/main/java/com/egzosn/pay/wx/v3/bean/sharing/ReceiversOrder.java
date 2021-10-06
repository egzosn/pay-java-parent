package com.egzosn.pay.wx.v3.bean.sharing;

import com.egzosn.pay.common.bean.PayOrder;
import com.egzosn.pay.wx.v3.utils.WxConst;

/**
 * 添加分账接收方
 *
 * @author Egan
 * <pre>
 * email egan@egzosn.com
 * date 2021/10/6
 * </pre>
 */
public class ReceiversOrder extends PayOrder {

    /**
     * 子商户号，选填
     */
    private String subMchid;
    /**
     * 子商户应用ID，选填
     * <p>
     * 分账接收方类型包含{@code PERSONAL_SUB_OPENID}时必填
     */
    private String subAppid;
    /**
     * 分账接收方类型，必填
     */
    private ReceiverType type;
    /**
     * 分账接收方帐号，必填
     */
    private String account;
    /**
     * 分账个人接收方姓名，选填
     * <p>
     * 分账接收方类型是{@code MERCHANT_ID}时，是商户全称（必传），当商户是小微商户或个体户时，是开户人姓名 分账接收方类型是{@code PERSONAL_OPENID}时，是个人姓名（选传，传则校验）
     * <ol>
     * <li>分账接收方类型是{@code PERSONAL_OPENID}，是个人姓名的密文（选传，传则校验） 此字段的加密方法详见：敏感信息加密说明</li>
     * <li>使用微信支付平台证书中的公钥</li>
     * <li>使用RSAES-OAEP算法进行加密</li>
     * <li>将请求中HTTP头部的Wechatpay-Serial设置为证书序列号</li>
     * </ol>
     */
    private String name;
    /**
     * 与分账方的关系类型，必填
     */
    private RelationType relationType;
    /**
     * 自定义的分账关系，选填
     */
    private String customRelation;

    public String getSubMchid() {
        return subMchid;
    }

    public void setSubMchid(String subMchid) {
        this.subMchid = subMchid;
        addAttr(WxConst.SUB_MCH_ID, subMchid);
    }

    public String getSubAppid() {
        return subAppid;
    }

    public void setSubAppid(String subAppid) {
        this.subAppid = subAppid;
        addAttr(WxConst.SUB_APPID, subAppid);
    }

    public ReceiverType getType() {
        return type;
    }

    public void setType(ReceiverType type) {
        this.type = type;
        addAttr(WxConst.TYPE, type);
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
        addAttr(WxConst.ACCOUNT, account);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        addAttr(WxConst.NAME, name);
    }

    public RelationType getRelationType() {
        return relationType;
    }

    public void setRelationType(RelationType relationType) {
        this.relationType = relationType;
        addAttr(WxConst.RELATION_TYPE, relationType);
    }

    public String getCustomRelation() {
        return customRelation;
    }

    public void setCustomRelation(String customRelation) {
        this.customRelation = customRelation;
        addAttr(WxConst.CUSTOM_RELATION, customRelation);
    }


}
