package com.egzosn.pay.wx.bean;

import java.math.BigDecimal;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import com.egzosn.pay.common.bean.BaseRefundResult;
import com.egzosn.pay.common.bean.CurType;

/**
 * 微信退款结果
 * @author Egan
 * <pre>
 * email egzosn@gmail.com
 * date 2020/8/16 21:29
 * </pre>
 */
public class WxRefundResult extends BaseRefundResult {

    /**
     * 返回状态码
     * SUCCESS/FAIL
     * 此字段是通信标识，表示接口层的请求结果，并非退款状态。
     */
    @JSONField(name = "return_code")
    private String returnCode;
    /**
     * 返回信息
     * 当return_code为FAIL时返回信息为错误原因 ，例如
     * 签名失败
     * 参数格式校验错误
     */
    @JSONField(name = "return_msg")
    private String returnMsg;
    /**
     * 业务结果
     * SUCCESS/FAIL
     * SUCCESS退款申请接收成功，结果通过退款查询接口查询
     * FAIL 提交业务失败
     */
    @JSONField(name = "result_code")
    private String resultCode;
    /**
     * 错误代码
     * 名称 	描述 	原因 	解决方案
     * SYSTEMERROR 	接口返回错误 	系统超时等 	请不要更换商户退款单号，请使用相同参数再次调用API。
     * BIZERR_NEED_RETRY 	退款业务流程错误，需要商户触发重试来解决 	并发情况下，业务被拒绝，商户重试即可解决 	请不要更换商户退款单号，请使用相同参数再次调用API。
     * TRADE_OVERDUE 	订单已经超过退款期限 	订单已经超过可退款的最大期限(支付后一年内可退款) 	请选择其他方式自行退款
     * ERROR 	业务错误 	申请退款业务发生错误 	该错误都会返回具体的错误原因，请根据实际返回做相应处理。
     * USER_ACCOUNT_ABNORMAL 	退款请求失败 	用户帐号注销 	此状态代表退款申请失败，商户可自行处理退款。
     * INVALID_REQ_TOO_MUCH 	无效请求过多 	连续错误请求数过多被系统短暂屏蔽 	请检查业务是否正常，确认业务正常后请在1分钟后再来重试
     * NOTENOUGH 	余额不足 	商户可用退款余额不足 	此状态代表退款申请失败，商户可根据具体的错误提示做相应的处理。
     * INVALID_TRANSACTIONID 	无效transaction_id 	请求参数未按指引进行填写 	请求参数错误，检查原交易号是否存在或发起支付交易接口返回失败
     * PARAM_ERROR 	参数错误 	请求参数未按指引进行填写 	请求参数错误，请重新检查再调用退款申请
     * APPID_NOT_EXIST 	APPID不存在 	参数中缺少APPID 	请检查APPID是否正确
     * MCHID_NOT_EXIST 	MCHID不存在 	参数中缺少MCHID 	请检查MCHID是否正确
     * ORDERNOTEXIST 	订单号不存在 	缺少有效的订单号 	请检查你的订单号是否正确且是否已支付，未支付的订单不能发起退款
     * REQUIRE_POST_METHOD 	请使用post方法 	未使用post传递参数  	请检查请求参数是否通过post方法提交
     * SIGNERROR 	签名错误 	参数签名结果不正确 	请检查签名参数和方法是否都符合签名算法要求
     * XML_FORMAT_ERROR 	XML格式错误 	XML格式错误 	请检查XML参数格式是否正确
     * FREQUENCY_LIMITED 	频率限制 	2个月之前的订单申请退款有频率限制 	该笔退款未受理，请降低频率后重试
     * NOAUTH 	异常IP请求不予受理 	请求ip异常 	如果是动态ip，请登录商户平台后台关闭ip安全配置；
     * 如果是静态ip，请确认商户平台配置的请求ip 在不在配的ip列表里
     * CERT_ERROR 	证书校验错误 	请检查证书是否正确，证书是否过期或作废。 	请检查证书是否正确，证书是否过期或作废。
     * REFUND_FEE_MISMATCH 	订单金额或退款金额与之前请求不一致，请核实后再试 	订单金额或退款金额与之前请求不一致，请核实后再试 	订单金额或退款金额与之前请求不一致，请核实后再试
     * INVALID_REQUEST 	请求参数符合参数格式，但不符合业务规则 	此状态代表退款申请失败，商户可根据具体的错误提示做相应的处理。 	此状态代表退款申请失败，商户可根据具体的错误提示做相应的处理。
     */
    @JSONField(name = "err_code")
    private String errCode;
    /**
     * 错误代码描述
     */
    @JSONField(name = "err_code_des")
    private String errCodeDes;
    /**
     * 应用id
     * 公众账号ID
     * 微信分配的公众账号ID
     */
    @JSONField(name = "appid")
    private String appid;
    /**
     * 商户号
     * 微信支付分配的商户号
     */
    @JSONField(name = "mch_id")
    private String mchId;
    /**
     *  	随机字符串，不长于32位
     */
    @JSONField(name = "nonce_str")
    private String nonceStr;
    /**
     * 签名
     */
    @JSONField(name = "sign")
    private String sign;
    /**
     * 微信订单号
     */
    @JSONField(name = "transaction_id")
    private String transactionId;
    /**
     * 商户订单号
     */
    @JSONField(name = "out_trade_no")
    private String outTradeNo;
    /**
     * 商户退款单号
     */
    @JSONField(name = "out_refund_no")
    private String outRefundNo;
    /**
     * 微信退款单号
     */
    @JSONField(name = "refund_id")
    private String refundId;
    /**
     * 退款金额
     * 退款总金额,单位为分,可以做部分退款
     */
    @JSONField(name = "refund_fee")
    private BigDecimal refundFee;
    /**
     * 应结退款金额
     * 去掉非充值代金券退款金额后的退款金额，退款金额=申请退款金额-非充值代金券退款金额，退款金额&lt;=申请退款金额
     */
    @JSONField(name = "settlement_refund_fee")
    private BigDecimal settlementRefundFee;
    /**
     * 标价金额
     * 订单总金额，单位为分，只能为整数，详见支付金额
     */
    @JSONField(name = "total_fee")
    private BigDecimal totalFee;
    /**
     * 应结订单金额
     * 去掉非充值代金券金额后的订单总金额，应结订单金额=订单金额-非充值代金券金额，应结订单金额&lt;=订单金额。
     */
    @JSONField(name = "settlement_total_fee")
    private BigDecimal settlementTotalFee;
    /**
     * 标价币种
     * 订单金额货币类型，符合ISO 4217标准的三位字母代码，默认人民币：CNY，其他值列表详见货币类型
     */
    @JSONField(name = "fee_type")
    private CurType feeType;
    /**
     * 现金支付金额
     * 现金支付金额，单位为分，只能为整数，详见支付金额
     */
    @JSONField(name = "cash_fee")
    private BigDecimal cashFee;
    /**
     * 现金支付币种
     * 货币类型，符合ISO 4217标准的三位字母代码，默认人民币：CNY，其他值列表详见货币类型
     */
    @JSONField(name = "cash_fee_type")
    private CurType cashFeeType;
    /**
     * 现金退款金额
     * 现金退款金额，单位为分，只能为整数，详见支付金额
     */
    @JSONField(name = "cash_refund_fee")
    private BigDecimal cashRefundFee;
    /**
     * 代金券类型
     * CASH--充值代金券
     *
     * NO_CASH---非充值代金券
     *
     * 订单使用代金券时有返回（取值：CASH、NO_CASH）。$n为下标,从0开始编号，举例：coupon_type_0
     * 这里只接收0的，其余请自行获取
     */
    @JSONField(name = "coupon_type_0")
    private String couponType0;
    /**
     * 代金券退款总金额
     * 代金券退款金额&lt;=退款金额，退款金额-代金券或立减优惠退款金额为现金，说明详见代金券或立减优惠
     */
    @JSONField(name = "coupon_refund_fee")
    private BigDecimal couponRefundFee;
    /**
     * 单个代金券退款金额
     * 代金券退款金额&lt;=退款金额，退款金额-代金券或立减优惠退款金额为现金，说明详见代金券或立减优惠
     * 这里只接收0的，其余请自行获取
     */
    @JSONField(name = "coupon_refund_fee_0")
    private BigDecimal couponRefundFee0;
    @JSONField(name = "coupon_refund_count")
    /**
     * 退款代金券使用数量
     * 退款代金券使用数量
     */
    private Integer couponRefundCount;
    /**
     * 退款代金券ID, $n为下标，从0开始编号
     * 这里只接收0的，其余请自行获取
     */
    @JSONField(name = "coupon_refund_id_0")
    private String couponRefundId0;
    /**
     * 获取退款请求结果状态码
     *
     * @return 状态码
     */
    @Override
    public String getCode() {
        return returnCode;
    }

    /**
     * 获取退款请求结果状态提示信息
     *
     * @return 提示信息
     */
    @Override
    public String getMsg() {
        return returnMsg;
    }

    /**
     * 返回业务结果状态码
     *
     * @return 业务结果状态码
     */
    @Override
    public String getResultCode() {
        return resultCode;
    }

    /**
     * 返回业务结果状态提示信息
     *
     * @return 业务结果状态提示信息
     */
    @Override
    public String getResultMsg() {
        return errCodeDes;
    }

    /**
     * 退款金额
     *
     * @return 退款金额
     */
    @Override
    public BigDecimal getRefundFee() {
        return refundFee;
    }

    /**
     * 退款币种信息
     *
     * @return 币种信息
     */
    @Override
    public CurType getRefundCurrency() {
        return feeType;
    }

    /**
     * 支付平台交易号
     * 发起支付时 支付平台(如支付宝)返回的交易订单号
     *
     * @return 支付平台交易号
     */
    @Override
    public String getTradeNo() {
        return transactionId;
    }

    /**
     * 支付订单号
     * 发起支付时，用户系统的订单号
     *
     * @return 支付订单号
     */
    @Override
    public String getOutTradeNo() {
        return outTradeNo;
    }

    /**
     * 商户退款单号
     *
     * @return 商户退款单号
     */
    @Override
    public String getRefundNo() {
        return outRefundNo;
    }

    public String getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    public String getReturnMsg() {
        return returnMsg;
    }

    public void setReturnMsg(String returnMsg) {
        this.returnMsg = returnMsg;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getErrCodeDes() {
        return errCodeDes;
    }

    public void setErrCodeDes(String errCodeDes) {
        this.errCodeDes = errCodeDes;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getMchId() {
        return mchId;
    }

    public void setMchId(String mchId) {
        this.mchId = mchId;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getOutRefundNo() {
        return outRefundNo;
    }

    public void setOutRefundNo(String outRefundNo) {
        this.outRefundNo = outRefundNo;
    }

    public String getRefundId() {
        return refundId;
    }

    public void setRefundId(String refundId) {
        this.refundId = refundId;
    }

    public void setRefundFee(BigDecimal refundFee) {
        this.refundFee = refundFee;
    }

    public BigDecimal getSettlementRefundFee() {
        return settlementRefundFee;
    }

    public void setSettlementRefundFee(BigDecimal settlementRefundFee) {
        this.settlementRefundFee = settlementRefundFee;
    }

    public BigDecimal getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(BigDecimal totalFee) {
        this.totalFee = totalFee;
    }

    public BigDecimal getSettlementTotalFee() {
        return settlementTotalFee;
    }

    public void setSettlementTotalFee(BigDecimal settlementTotalFee) {
        this.settlementTotalFee = settlementTotalFee;
    }

    public CurType getFeeType() {
        return feeType;
    }

    public void setFeeType(CurType feeType) {
        this.feeType = feeType;
    }

    public BigDecimal getCashFee() {
        return cashFee;
    }

    public void setCashFee(BigDecimal cashFee) {
        this.cashFee = cashFee;
    }

    public CurType getCashFeeType() {
        return cashFeeType;
    }

    public void setCashFeeType(CurType cashFeeType) {
        this.cashFeeType = cashFeeType;
    }

    public BigDecimal getCashRefundFee() {
        return cashRefundFee;
    }

    public void setCashRefundFee(BigDecimal cashRefundFee) {
        this.cashRefundFee = cashRefundFee;
    }

    public String getCouponType0() {
        return couponType0;
    }

    public void setCouponType0(String couponType0) {
        this.couponType0 = couponType0;
    }

    public BigDecimal getCouponRefundFee() {
        return couponRefundFee;
    }

    public void setCouponRefundFee(BigDecimal couponRefundFee) {
        this.couponRefundFee = couponRefundFee;
    }

    public BigDecimal getCouponRefundFee0() {
        return couponRefundFee0;
    }

    public void setCouponRefundFee0(BigDecimal couponRefundFee0) {
        this.couponRefundFee0 = couponRefundFee0;
    }

    public Integer getCouponRefundCount() {
        return couponRefundCount;
    }

    public void setCouponRefundCount(Integer couponRefundCount) {
        this.couponRefundCount = couponRefundCount;
    }

    public String getCouponRefundId0() {
        return couponRefundId0;
    }

    public void setCouponRefundId0(String couponRefundId0) {
        this.couponRefundId0 = couponRefundId0;
    }


    public static final WxRefundResult create(Map<String, Object> result){
        WxRefundResult refundResult = new JSONObject(result).toJavaObject(WxRefundResult.class);
        refundResult.setAttrs(result);
        return refundResult;
    }
}
