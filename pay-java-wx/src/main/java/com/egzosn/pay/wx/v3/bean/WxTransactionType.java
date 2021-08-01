package com.egzosn.pay.wx.v3.bean;

import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.egzosn.pay.common.bean.MethodType;
import com.egzosn.pay.common.bean.PayOrder;
import com.egzosn.pay.common.bean.TransactionType;
import com.egzosn.pay.common.util.MapGen;
import com.egzosn.pay.common.util.str.StringUtils;
import com.egzosn.pay.wx.v3.bean.order.H5Info;
import com.egzosn.pay.wx.v3.bean.order.SceneInfo;
import com.egzosn.pay.wx.v3.utils.WxConst;

/**
 * 微信交易类型
 *
 * @author egan
 * <p>
 * email egzosn@gmail.com
 * date 2016/10/19 22:58
 */
public enum WxTransactionType implements TransactionType {
    /**
     * 获取证书.
     */
    CERT("certificates", MethodType.GET),
    /**
     * 公众号支付
     */
    JSAPI("pay{partner}/transactions/jsapi", MethodType.POST) {
        @Override
        public void setAttribute(Map<String, Object> parameters, PayOrder order) {
            String key = parameters.containsKey("sub_mchid") ? "sub_openid" : "openid";
            MapGen<String, String> mapGen = new MapGen<String, String>(key, order.getOpenid());
            parameters.put("payer", mapGen.getAttr());
        }
    },
    /**
     * 二维码支付
     */
    NATIVE("pay{partner}/transactions/native", MethodType.POST, true),
    /**
     * 移动支付
     */
    APP("pay{partner}/transactions/app", MethodType.POST),
    /**
     * H5支付
     */
    H5("pay{partner}/transactions/h5", MethodType.POST, true) {
        @Override
        public void setAttribute(Map<String, Object> parameters, PayOrder order) {
            Object sceneInfoObj = parameters.get(WxConst.SCENE_INFO);
            SceneInfo sceneInfo = null;
            if (null == sceneInfoObj) {
                sceneInfo = new SceneInfo();
            }
            else if (sceneInfoObj instanceof SceneInfo) {
                sceneInfo = (SceneInfo) sceneInfoObj;
            }
            else {
                String jsonString = JSON.toJSONString(sceneInfoObj, SerializerFeature.WriteMapNullValue);
                sceneInfo = JSON.parseObject(jsonString, SceneInfo.class);
            }
            String billCreateIp = order.getSpbillCreateIp();
            if (StringUtils.isNotEmpty(billCreateIp)) {
                sceneInfo.setPayerClientIp(billCreateIp);
            }
            H5Info h5Info = sceneInfo.getH5Info();
            if (null == h5Info) {
                sceneInfo.setH5Info(new H5Info(order.getWapName(), order.getWapUrl()));
            }
        }

    },

    /**
     * 查询订单
     * 兼容V2的方式，通过入参来决定
     */
    QUERY("pay{partner}/transactions/", MethodType.GET),
    /**
     * 微信支付订单号查询
     */
    QUERY_TRANSACTION_ID("pay{partner}/transactions/id/{transaction_id}", MethodType.GET),
    /**
     * 商户订单号查询
     */
    QUERY_OUT_TRADE_NO("pay{partner}/transactions/out-trade-no/{out_trade_no}", MethodType.GET),
    /**
     * 关闭订单
     */
    CLOSE("pay{partner}/transactions/out-trade-no/{out_trade_no}/close", MethodType.POST),
    /**
     * 申请退款
     */
    REFUND("refund/domestic/refunds", MethodType.POST),
    /**
     * 查询退款
     */
    REFUND_QUERY("refund/domestic/refunds/{out_refund_no}", MethodType.GET),
    /**
     * 申请交易账单
     */
    TRADE_BILL("bill/tradebill", MethodType.GET),
    /**
     * 申请资金账单
     */
    FUND_FLOW_BILL("bill/fundflowbill", MethodType.GET),


    ;

    WxTransactionType(String type, MethodType method) {
        this(type, method, false);

    }

    WxTransactionType(String type, MethodType method, boolean back) {
        this.type = type;
        this.method = method;
        this.back = back;
    }

    private String type;
    private MethodType method;
    /**
     * 是否直接返回
     */
    private boolean back;


    @Override
    public String getType() {
        return type;
    }

    @Override
    public String getMethod() {
        return this.method.name();
    }

    /**
     * 是否直接返回
     *
     * @return 是否直接返回
     */
    public boolean isReturn() {
        return back;
    }

    public void setAttribute(Map<String, Object> parameters, PayOrder order) {

    }
}
