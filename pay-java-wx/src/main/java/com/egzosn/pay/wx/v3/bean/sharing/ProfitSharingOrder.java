package com.egzosn.pay.wx.v3.bean.sharing;

import java.util.List;

import com.egzosn.pay.common.bean.PayOrder;
import com.egzosn.pay.wx.v3.utils.WxConst;

/**
 * 服务商请求分账订单
 * @author Egan
 * <pre>
 * email egan@egzosn.com
 * date 2021/10/6
 * </pre>
 */
public class ProfitSharingOrder extends PayOrder {

    /**
     * 子商户号，选填，服务商必填
     */
    private String subMchid;

    /**
     * 子商户应用ID，选填
     * <p>
     * 分账接收方类型包含{@code PERSONAL_SUB_OPENID}时必填
     */
    private String subAppid;

    /**
     * 分账接收方列表，选填
     * <p>
     * 可以设置出资商户作为分账接受方，最多可有50个分账接收方
     */
    private List<Receiver> receivers;
    /**
     * 是否解冻剩余未分资金，必填
     * <ol>
     *     <li>如果为{@code true}，该笔订单剩余未分账的金额会解冻回分账方商户；</li>
     *     <li>如果为{@code false}，该笔订单剩余未分账的金额不会解冻回分账方商户，可以对该笔订单再次进行分账。</li>
     * </ol>
     */
    private Boolean unfreezeUnsplit;

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

    /**
     * 微信支付订单号
     * @return 微信支付订单号
     */
    public String getTransactionId() {
        return getTradeNo();
    }

    public void setTransactionId(String transactionId) {
     setTradeNo(transactionId);
    }
    /**
     * 商户分账单号，必填
     * <p>
     * 商户系统内部的分账单号，在商户系统内部唯一，同一分账单号多次请求等同一次。
     * 只能是数字、大小写字母_-|*@
     * @return 商户分账单号，必填
     */
    public String getOutOrderNo() {
        return getOutTradeNo();
    }

    public void setOutOrderNo(String outOrderNo) {
      setOutTradeNo(outOrderNo);
    }

    public List<Receiver> getReceivers() {
        return receivers;
    }

    public void setReceivers(List<Receiver> receivers) {
        this.receivers = receivers;
        addAttr(WxConst.RECEIVERS, receivers);
    }

    public Boolean getUnfreezeUnsplit() {
        return unfreezeUnsplit;
    }

    public void setUnfreezeUnsplit(Boolean unfreezeUnsplit) {
        this.unfreezeUnsplit = unfreezeUnsplit;
        addAttr(WxConst.UNFREEZE_UNSPLIT, unfreezeUnsplit);
    }
}
