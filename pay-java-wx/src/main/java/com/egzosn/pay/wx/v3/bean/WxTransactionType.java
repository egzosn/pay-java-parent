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
 * 微信V3交易类型
 *
 * @author egan
 * <pre>
 * email egan@egzosn.com
 * date 2016/10/19 22:58
 * </pre>
 */
public enum WxTransactionType implements TransactionType {
    /**
     * 获取证书.
     */
    CERT("/v3/certificates", MethodType.GET),

    //-----------------------------------------------------------------
    //以下为直连与服务商支付方式
    /**
     * 微信公众号支付或者小程序支付
     */
    JSAPI("/v3/pay{partner}/transactions/jsapi", MethodType.POST) {
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
    NATIVE("/v3/pay{partner}/transactions/native", MethodType.POST, true),
    /**
     * 移动支付
     */
    APP("/v3/pay{partner}/transactions/app", MethodType.POST),
    /**
     * H5支付
     */
    H5("/v3/pay{partner}/transactions/h5", MethodType.POST, true) {
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
            parameters.put("scene_info", sceneInfo);
        }

    },
    /**
     * H5支付
     * 兼容 后期会抛弃
     */
    @Deprecated
    MWEB("/v3/pay{partner}/transactions/h5", MethodType.POST, true) {
        @Override
        public void setAttribute(Map<String, Object> parameters, PayOrder order) {
            H5.setAttribute(parameters, order);
        }

    },

    /**
     * 查询订单
     * 兼容V2的方式，通过入参来决定
     */
    QUERY("/v3/pay{partner}/transactions/", MethodType.GET),
    /**
     * 微信支付订单号查询
     */
    QUERY_TRANSACTION_ID("/v3/pay{partner}/transactions/id/{transaction_id}", MethodType.GET),
    /**
     * 商户订单号查询
     */
    QUERY_OUT_TRADE_NO("/v3/pay{partner}/transactions/out-trade-no/{out_trade_no}", MethodType.GET),
    /**
     * 关闭订单
     */
    CLOSE("/v3/pay{partner}/transactions/out-trade-no/{out_trade_no}/close", MethodType.POST),
    /**
     * 申请退款
     */
    REFUND("/v3/refund/domestic/refunds", MethodType.POST),
    /**
     * 查询退款
     */
    REFUND_QUERY("/v3/refund/domestic/refunds/{out_refund_no}", MethodType.GET),
    /**
     * 申请交易账单
     */
    TRADE_BILL("/v3/bill/tradebill", MethodType.GET),
    /**
     * 申请资金账单
     */
    FUND_FLOW_BILL("/v3/bill/fundflowbill", MethodType.GET),

    //-----------------------------------------------------------------
    //以下为合并支付
    /**
     * 合单下单-APP支付
     */
    COMBINE_APP("/v3/combine-transactions/app", MethodType.POST),

    /**
     * 合单下单-微信公众号支付或者小程序支付.
     */
    COMBINE_JSAPI("/v3/combine-transactions/jsapi", MethodType.POST),
    /**
     * 合单下单-H5支付
     */
    COMBINE_H5("/v3/combine-transactions/h5", MethodType.POST, true),
    /**
     * 合单下单-Native支付
     */
    COMBINE_NATIVE("/v3/combine-transactions/native", MethodType.POST, true),
    /**
     * 合单查询订单
     */
    COMBINE_TRANSACTION("/v3/combine-transactions/out-trade-no/{combine_out_trade_no}", MethodType.GET),

    /**
     * 合单关闭订单
     */
    COMBINE_CLOSE("/v3/combine-transactions/out-trade-no/{combine_out_trade_no}/close", MethodType.POST)
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
