package com.egzosn.pay.demo.entity;

import com.egzosn.pay.ali.api.AliPayConfigStorage;
import com.egzosn.pay.ali.api.AliPayService;
import com.egzosn.pay.ali.bean.AliTransactionType;
import com.egzosn.pay.common.api.PayService;
import com.egzosn.pay.common.bean.BasePayType;
import com.egzosn.pay.common.bean.CertStoreType;
import com.egzosn.pay.common.bean.MsgType;
import com.egzosn.pay.common.bean.TransactionType;
import com.egzosn.pay.common.http.HttpConfigStorage;
import com.egzosn.pay.demo.service.handler.WxPayMessageHandler;
import com.egzosn.pay.fuiou.api.FuiouPayConfigStorage;
import com.egzosn.pay.fuiou.api.FuiouPayService;
import com.egzosn.pay.fuiou.bean.FuiouTransactionType;
import com.egzosn.pay.payoneer.api.PayoneerConfigStorage;
import com.egzosn.pay.payoneer.api.PayoneerPayService;
import com.egzosn.pay.payoneer.bean.PayoneerTransactionType;
import com.egzosn.pay.paypal.api.PayPalConfigStorage;
import com.egzosn.pay.paypal.api.PayPalPayService;
import com.egzosn.pay.paypal.bean.PayPalTransactionType;
import com.egzosn.pay.union.api.UnionPayConfigStorage;
import com.egzosn.pay.union.api.UnionPayService;
import com.egzosn.pay.union.bean.UnionTransactionType;
import com.egzosn.pay.wx.api.WxPayConfigStorage;
import com.egzosn.pay.wx.api.WxPayService;
import com.egzosn.pay.wx.bean.WxTransactionType;
import com.egzosn.pay.wx.youdian.api.WxYouDianPayConfigStorage;
import com.egzosn.pay.wx.youdian.api.WxYouDianPayService;
import com.egzosn.pay.wx.youdian.bean.YoudianTransactionType;



/**
 * 支付类型
 *
 * @author egan
 * email egzosn@gmail.com
 * date 2016/11/20 0:30
 */
public enum PayType implements BasePayType {


    aliPay{
        /**
         *  @see com.egzosn.pay.ali.api.AliPayService 17年更新的版本,旧版本请自行切换
         * @param apyAccount
         * @return
         */
        @Override
        public PayService getPayService(ApyAccount apyAccount) {
            AliPayConfigStorage configStorage = new AliPayConfigStorage();
            //配置的附加参数的使用
            configStorage.setAttach(apyAccount.getPayId());
            configStorage.setPid(apyAccount.getPartner());
            configStorage.setAppid(apyAccount.getAppid());
            configStorage.setKeyPublic(apyAccount.getPublicKey());
            configStorage.setKeyPrivate(apyAccount.getPrivateKey());
            configStorage.setNotifyUrl(apyAccount.getNotifyUrl());
            configStorage.setReturnUrl(apyAccount.getReturnUrl());
            configStorage.setSignType(apyAccount.getSignType());
            configStorage.setSeller(apyAccount.getSeller());
            configStorage.setPayType(apyAccount.getPayType().toString());
            configStorage.setMsgType(apyAccount.getMsgType());
            configStorage.setInputCharset(apyAccount.getInputCharset());
            configStorage.setTest(apyAccount.isTest());
            //请求连接池配置
            HttpConfigStorage httpConfigStorage = new HttpConfigStorage();
            //最大连接数
            httpConfigStorage.setMaxTotal(20);
            //默认的每个路由的最大连接数
            httpConfigStorage.setDefaultMaxPerRoute(10);
            return new AliPayService(configStorage, httpConfigStorage);
        }

        @Override
        public TransactionType getTransactionType(String transactionType) {
            // com.egzosn.pay.ali.before.bean.AliTransactionType 17年更新的版本,旧版本请自行切换

            // AliTransactionType 17年更新的版本,旧版本请自行切换
            return AliTransactionType.valueOf(transactionType);
        }


    },wxPay {
        @Override
        public PayService getPayService(ApyAccount apyAccount) {
            WxPayConfigStorage wxPayConfigStorage = new WxPayConfigStorage();
            wxPayConfigStorage.setMchId(apyAccount.getPartner());
            wxPayConfigStorage.setAppid(apyAccount.getAppid());
            //转账公钥，转账时必填
            wxPayConfigStorage.setKeyPublic(apyAccount.getPublicKey());
            wxPayConfigStorage.setSecretKey(apyAccount.getPrivateKey());
            wxPayConfigStorage.setNotifyUrl(apyAccount.getNotifyUrl());
            wxPayConfigStorage.setReturnUrl(apyAccount.getReturnUrl());
            wxPayConfigStorage.setSignType(apyAccount.getSignType());
            wxPayConfigStorage.setPayType(apyAccount.getPayType().toString());
            wxPayConfigStorage.setMsgType(apyAccount.getMsgType());
            wxPayConfigStorage.setInputCharset(apyAccount.getInputCharset());
            wxPayConfigStorage.setTest(apyAccount.isTest());

            //https证书设置 方式一
        /*    HttpConfigStorage httpConfigStorage = new HttpConfigStorage();
             //TODO 这里也支持输入流的入参。
//          httpConfigStorage.setKeystore(PayType.class.getResourceAsStream("/证书文件"));
            httpConfigStorage.setKeystore("证书信息串");
            httpConfigStorage.setStorePassword("证书密码");
            //设置ssl证书对应的存储方式，这里默认为文件地址
            httpConfigStorage.setCertStoreType(CertStoreType.PATH);
            return  new WxPayService(wxPayConfigStorage, httpConfigStorage);*/
            WxPayService wxPayService = new WxPayService(wxPayConfigStorage);
            wxPayService.setPayMessageHandler(new WxPayMessageHandler(1));
            return wxPayService;
        }

        /**
         * 根据支付类型获取交易类型
         * @param transactionType 类型值
         * @see WxTransactionType
         * @return
         */
        @Override
        public TransactionType getTransactionType(String transactionType) {

            return WxTransactionType.valueOf(transactionType);
        }
    },youdianPay {
        @Override
        public PayService getPayService(ApyAccount apyAccount) {
            // TODO 2017/1/23 14:12 author: egan  集群的话,友店可能会有bug。暂未测试集群环境
            WxYouDianPayConfigStorage wxPayConfigStorage = new WxYouDianPayConfigStorage();
            wxPayConfigStorage.setKeyPrivate(apyAccount.getPrivateKey());
            wxPayConfigStorage.setKeyPublic(apyAccount.getPublicKey());
//            wxPayConfigStorage.setNotifyUrl(apyAccount.getNotifyUrl());
//            wxPayConfigStorage.setReturnUrl(apyAccount.getReturnUrl());
            wxPayConfigStorage.setSignType(apyAccount.getSignType());
            wxPayConfigStorage.setPayType(apyAccount.getPayType().toString());
            wxPayConfigStorage.setMsgType(apyAccount.getMsgType());
            wxPayConfigStorage.setSeller(apyAccount.getSeller());
            wxPayConfigStorage.setInputCharset(apyAccount.getInputCharset());
            wxPayConfigStorage.setTest(apyAccount.isTest());
            return  new WxYouDianPayService(wxPayConfigStorage);
        }

        /**
         * 根据支付类型获取交易类型
         * @param transactionType 类型值
         * @see YoudianTransactionType
         * @return
         */
        @Override
        public TransactionType getTransactionType(String transactionType) {

            return YoudianTransactionType.valueOf(transactionType);
        }
    },fuiou{

        @Override
        public PayService getPayService(ApyAccount apyAccount) {
            FuiouPayConfigStorage fuiouPayConfigStorage = new FuiouPayConfigStorage();
            fuiouPayConfigStorage.setKeyPublic(apyAccount.getPublicKey());
            fuiouPayConfigStorage.setKeyPrivate(apyAccount.getPrivateKey());
            fuiouPayConfigStorage.setNotifyUrl(apyAccount.getNotifyUrl());
            fuiouPayConfigStorage.setReturnUrl(apyAccount.getReturnUrl());
            fuiouPayConfigStorage.setSignType(apyAccount.getSignType());
            fuiouPayConfigStorage.setPayType(apyAccount.getPayType().toString());
            fuiouPayConfigStorage.setMsgType(apyAccount.getMsgType());
            fuiouPayConfigStorage.setInputCharset(apyAccount.getInputCharset());
            fuiouPayConfigStorage.setTest(apyAccount.isTest());
            return new FuiouPayService(fuiouPayConfigStorage);
        }

        @Override
        public TransactionType getTransactionType(String transactionType) {
            return FuiouTransactionType.valueOf(transactionType);
        }


    },unionPay{

        @Override
        public PayService getPayService(ApyAccount apyAccount) {
            UnionPayConfigStorage unionPayConfigStorage = new UnionPayConfigStorage();
            unionPayConfigStorage.setMerId(apyAccount.getPartner());
            unionPayConfigStorage.setCertSign(true);
//            unionPayConfigStorage.setKeyPublic(apyAccount.getPublicKey());
//            unionPayConfigStorage.setKeyPrivate(apyAccount.getPrivateKey());

            //中级证书路径
            unionPayConfigStorage.setAcpMiddleCert("D:/certs/acp_test_middle.cer");
            //根证书路径
            unionPayConfigStorage.setAcpRootCert("D:/certs/acp_test_root.cer");
            // 私钥证书路径
            unionPayConfigStorage.setKeyPrivateCert("D:/certs/acp_test_sign.pfx");
            //私钥证书对应的密码
            unionPayConfigStorage.setKeyPrivateCertPwd("000000");
            //设置证书对应的存储方式，这里默认为文件地址
            unionPayConfigStorage.setCertStoreType(CertStoreType.PATH);

            unionPayConfigStorage.setNotifyUrl(apyAccount.getNotifyUrl());
            unionPayConfigStorage.setReturnUrl(apyAccount.getReturnUrl());
            unionPayConfigStorage.setSignType(apyAccount.getSignType());
            unionPayConfigStorage.setPayType(apyAccount.getPayType().toString());
            unionPayConfigStorage.setMsgType(apyAccount.getMsgType());
            unionPayConfigStorage.setInputCharset(apyAccount.getInputCharset());
            unionPayConfigStorage.setTest(apyAccount.isTest());
            return new UnionPayService(unionPayConfigStorage);
        }

        @Override
        public TransactionType getTransactionType(String transactionType) {
            return UnionTransactionType.valueOf(transactionType);
        }


    },payoneer{
        @Override
        public PayService getPayService(ApyAccount apyAccount) {
            PayoneerConfigStorage configStorage = new PayoneerConfigStorage();
            //设置商户Id
            configStorage.setProgramId(apyAccount.getPartner());
            configStorage.setMsgType(MsgType.json);
            configStorage.setInputCharset("utf-8");
            //"PayoneerPay 用户名"
            configStorage.setUserName(apyAccount.getSeller());
            //PayoneerPay API password
            configStorage.setApiPassword(apyAccount.getPrivateKey());
            //是否为沙箱
            configStorage.setTest(true);
            return new PayoneerPayService(configStorage);

            //以下不建议进行使用，会引起两次请求的问题
            //Basic Auth
           /* HttpConfigStorage httpConfigStorage = new  HttpConfigStorage();
            httpConfigStorage.setAuthUsername("PayoneerPay 用户名");
            httpConfigStorage.setAuthPassword("PayoneerPay API password");
            return new PayoneerPayService(configStorage, httpConfigStorage);*/
        }

        @Override
        public TransactionType getTransactionType(String transactionType) {
            return PayoneerTransactionType.valueOf(transactionType);
        }


    },payPal{
        @Override
        public PayService getPayService(ApyAccount apyAccount) {
            PayPalConfigStorage storage = new PayPalConfigStorage();
            //配置的附加参数的使用
            storage.setAttach(apyAccount.getPayId());
            storage.setClientID(apyAccount.getAppid());
            storage.setClientSecret(apyAccount.getPrivateKey());
            storage.setTest(true);
            //发起付款后的页面转跳地址
            storage.setReturnUrl(apyAccount.getReturnUrl());
            //取消按钮转跳地址,这里兼容的做法
            storage.setNotifyUrl(apyAccount.getNotifyUrl());
            return new PayPalPayService(storage);
        }

        @Override
        public TransactionType getTransactionType(String transactionType) {
            return PayPalTransactionType.valueOf(transactionType);
        }


    };

    public abstract PayService getPayService(ApyAccount apyAccount);


}
