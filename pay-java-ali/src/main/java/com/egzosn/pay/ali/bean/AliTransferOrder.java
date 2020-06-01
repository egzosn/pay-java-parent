package com.egzosn.pay.ali.bean;

import com.egzosn.pay.common.bean.TransferOrder;

import java.math.BigDecimal;
import java.util.TreeMap;

/**
 * 支付转账(红包)订单
 *
 * @author egan
 * date 2020/5/18 21:08
 * email egzosn@gmail.com
 */
public class AliTransferOrder extends TransferOrder {

    private String identity;
    private String identityType;

    /**
     * 商户端的唯一订单号，对于同一笔转账请求，商户需保证该订单号唯一。
     *
     * @return 商户端的唯一订单号
     */
    public String getOutBizNo() {
        return getOutNo();
    }

    public void setOutBizNo(String outBizNo) {
        setOutNo(outBizNo);
    }

    /**
     * 订单总金额，单位为元，精确到小数点后两位，STD_RED_PACKET产品取值范围[0.01,100000000]；
     * TRANS_ACCOUNT_NO_PWD产品取值范围[0.1,100000000]
     *
     * @return 订单总金额
     */
    public BigDecimal getTransAmount() {
        return getAmount();
    }

    public void setTransAmount(BigDecimal transAmount) {
        setAmount(transAmount);
    }

    /**
     * 转账业务的标题，用于在支付宝用户的账单里显示
     *
     * @return 转账业务的标题
     */
    public String getOrderTitle() {
        return (String) getAttr("order_title");
    }

    public void setOrderTitle(String orderTitle) {
        addAttr("order_title", orderTitle);
    }

    /**
     * 描述特定的业务场景，可传的参数如下：
     * DIRECT_TRANSFER：单笔无密转账到支付宝/银行卡, B2C现金红包;
     * PERSONAL_COLLECTION：C2C现金红包-领红包
     *
     * @return 描述特定的业务场景
     */
    public String getBizScene() {
        return (String) getAttr("biz_scene");
    }

    public void setBizScene(String bizScene) {
        addAttr("biz_scene", bizScene);
    }

    /**
     * 收款方信息
     *
     * @return 收款方信息
     */
    private TreeMap<String, Object> getPayeeinfo() {
        Object payeeInfo = getAttr("payee_info");
        if (null != payeeInfo && payeeInfo instanceof TreeMap){
            return (TreeMap<String, Object>) payeeInfo;
        }
        TreeMap<String, Object> payee = new TreeMap<>();
        addAttr("payee_info", payee);
        return payee;

    }

    /**
     * 参与方的唯一标识
     *
     * @return 参与方的唯一标识
     */
    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
        getPayeeinfo().put("identity", identity);
    }

    /**
     * 参与方的标识类型，目前支持如下类型：
     * 1、ALIPAY_USER_ID 支付宝的会员ID
     * 2、ALIPAY_LOGON_ID：支付宝登录号，支持邮箱和手机号格式
     *
     * @return 参与方的标识类型
     */
    public String getIdentityType() {
        return identityType;
    }

    public void setIdentityType(String identityType) {
        this.identityType = identityType;
        getPayeeinfo().put("identity_type", identityType);
    }


    /**
     * 参与方真实姓名
     *
     * @return 参与方真实姓名
     */
    public String getName() {
        return getPayeeName();
    }

    public void setName(String name) {
        setPayeeName(name);
        getPayeeinfo().put("name", name);
    }

    /**
     * 转账业务请求的扩展参数，支持传入的扩展参数如下：
     * 1、sub_biz_scene 子业务场景，红包业务必传，取值REDPACKET，C2C现金红包、B2C现金红包均需传入；
     * <p>
     * 2、withdraw_timeliness为转账到银行卡的预期到账时间，可选（不传入则默认为T1），取值T0表示预期T+0到账，取值T1表示预期T+1到账，因到账时效受银行机构处理影响，支付宝无法保证一定是T0或者T1到账；
     *
     * @return 转账业务请求的扩展参数
     */
    public String getBusinessParams() {
        return (String) getAttr("business_params");
    }

    public void setBusinessParams(String businessParams) {
        addAttr("business_params", businessParams);
    }


}
