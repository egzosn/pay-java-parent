package com.egzosn.pay.demo.entity;

import com.egzosn.pay.ali.api.AliPayConfigStorage;
import com.egzosn.pay.ali.api.AliPayService;
import com.egzosn.pay.ali.bean.AliTransactionType;
import com.egzosn.pay.common.api.PayService;
import com.egzosn.pay.common.bean.BasePayType;
import com.egzosn.pay.common.bean.CertStoreType;
import com.egzosn.pay.common.bean.TransactionType;
import com.egzosn.pay.common.util.sign.SignUtils;
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


    aliPay {
        /**
         *  @see com.egzosn.pay.ali.api.AliPayService
         * @param apyAccount
         * @return
         */
        @Override
        public PayService getPayService(ApyAccount apyAccount) {
            AliPayConfigStorage aliPayConfigStorage = new AliPayConfigStorage();
            aliPayConfigStorage.setPid("2088102169916436");
            aliPayConfigStorage.setAppId("2016080400165436");
            aliPayConfigStorage.setCertSign(true);
            //设置证书存储方式，这里为路径
            aliPayConfigStorage.setCertStoreType(CertStoreType.CLASS_PATH);
            aliPayConfigStorage.setMerchantCert("ali/appCertPublicKey_2016080400165436.crt");
            aliPayConfigStorage.setAliPayCert("ali/alipayCertPublicKey_RSA2.crt");
            aliPayConfigStorage.setAliPayRootCert("ali/alipayRootCert.crt");
            aliPayConfigStorage.setKeyPrivate("MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQCw7MD2Cwv/jnXssFjXnGx3JlGF57gJa2aYbJRV8MnNiPVpX4Ha+8ZjnQDhvkrWH4hHmzcujOr213HqloMpUSYBzCPiXGVRUUvdimejcHHTod7nI4g6nztzzfey/TXNDHmp7vY3pOIcjB0Zn0pkNAz2tKAFkqb4raHOqTB0QA0zD24Cn+26J2UJyYRcgeH0GtSQuUrm7yaGsuKakh+qtgWF6R71n5PMGOTQ5LH3i0WVHfCBkNGgJC6yC96HR4D7cosoyKD0+lp8UB/NVUWl7Tt/KLOgFUwh0GKSYFfv56O/VBV2+xqCGE4PlZESfVuOqz5vjjxzw3xDAUJrV8hSX/AJAgMBAAECggEBAKE0d3U4B4yo/2XUIH8EdgfykCFUSum6RFbpyBauORHfksyaSzV+ZvtomN8XhhSn0oJ8OMFfgM+86nz2+zdwSxMkMCYWTfLUAi4v59KRqAVO3kz4oS3Y3FDeAK3D7XuRvGFL7GgzAhtEx1cLPrsiehVn6s5pG15GxsIIgq/JlL1J88wn1zENLrVHmD6z/JpXvfb/RS1yR+5lyoohp4g0Ph9jJ3bCyUbRpK0QkPEzgAuWL0K2ITCL7PYHNAplI8d2xHHOLF9Qdjyx+ZrQ/RxtqzfyWzhqjsmp2qlgNCxWlt3woS9UhDB+nRvjEoWTJmIOszAMYuj8wGlX+3Ui3ALOdQECgYEA25EqnFPFinUnzgNvB6NYmh5STmZun6s4bUOLqwefKtEvrOtRwTu7sB7NIf37fizG3/MJUWHxiLy2/3ub4d2JxdDNBtJoEqnp6QB12qglCNa4CajdjtJa1dR81F9QvytsqEkmPYXFPPyviB0FcSIDAGMb3IbwvIfzBPY9WY8dJnECgYEAzkg3yKEFBZ8BU0WQ+3hyfKUoAhBEnxouxRSTBcXxwstJRiqaGTVe5aoJGQI+0xS7Z6q07XDtN2t97s6DnRLWbljsX6B64itzNhXRyzjdD3iZDU/KSw7khjhXf8XOZaj9eXmACDiUnkEn1xsM8bLiRGqB8y5f3aMY/RpuACGXnxkCgYEAx/zwT9Vpr1RIfjfYcJ+Su0X0994K0roUukj0tUJK8qf4gcsQ+y1aJe/YLib1ZBaKyj7G9O5+HmqtUAUZld/AdoJZzOXmz2EeYhD+R7wxh1xz4rCBpW3qOKvDS3jJxmZaIOoHv6/RWFxb0WGFrGcrTrX3EaWDLmWxr4pNlP5qsbECgYATllntrBR8/ycyEAX/SuWcHlaZM5BAh0zvm8+GGdCmDYWMqxjs0duL9URd4o+ynWJaKqR5c2KjA4r2tRdcP+Cqo7j2L5fbiAKtnQ7JvEGJaYsm72+nBuf+MrVkRZUepBhFg5r7rNu31zoAO+pTvQetNWvXeozRz93ckrjlPEtYaQKBgQDFwbV92rlRMLjZzlY+o0knoeJBjPQmPdiBTpGNimdy9L4c2Ure7affjcUiYhkKqrK5k5SScJTATgyQ7JF346FdtUtZ/6Kkj1RwJmmprPrDa9CATLoTle7g9OVd4sHT2ITHZMzPaF3ILvzcwJ70AD1xcxCQb+/7sDPmw7Mc8gOA7Q==");
            aliPayConfigStorage.setNotifyUrl("http://pay.egzosn.com/payBack.json");
            aliPayConfigStorage.setReturnUrl("http://pay.egzosn.com/payBack.html");
            aliPayConfigStorage.setSignType(SignUtils.RSA2.name());
            aliPayConfigStorage.setSeller("2088102169916436");
            aliPayConfigStorage.setInputCharset("utf-8");
            //是否为测试账号，沙箱环境
            aliPayConfigStorage.setTest(true);
            return new AliPayService(aliPayConfigStorage);
        }

        @Override
        public TransactionType getTransactionType(String transactionType) {
            // com.egzosn.pay.ali.before.bean.AliTransactionType 17年更新的版本,旧版本请自行切换

            // AliTransactionType 17年更新的版本,旧版本请自行切换
            return AliTransactionType.valueOf(transactionType);
        }


    }, wxPay {
        @Override
        public PayService getPayService(ApyAccount apyAccount) {
            WxPayConfigStorage wxPayConfigStorage = new WxPayConfigStorage();
            wxPayConfigStorage.setMchId(apyAccount.getPartner());
            wxPayConfigStorage.setAppId(apyAccount.getAppId());
            //转账公钥，转账时必填
            wxPayConfigStorage.setKeyPublic(apyAccount.getPublicKey());
            wxPayConfigStorage.setSecretKey(apyAccount.getPrivateKey());
            wxPayConfigStorage.setNotifyUrl(apyAccount.getNotifyUrl());
            wxPayConfigStorage.setReturnUrl(apyAccount.getReturnUrl());
            wxPayConfigStorage.setSignType(apyAccount.getSignType());
            wxPayConfigStorage.setPayType(apyAccount.getPayType().toString());
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
    }, youdianPay {
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
            wxPayConfigStorage.setSeller(apyAccount.getSeller());
            wxPayConfigStorage.setInputCharset(apyAccount.getInputCharset());
            wxPayConfigStorage.setTest(apyAccount.isTest());
            return new WxYouDianPayService(wxPayConfigStorage);
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
    }, fuiou {
        @Override
        public PayService getPayService(ApyAccount apyAccount) {
            FuiouPayConfigStorage fuiouPayConfigStorage = new FuiouPayConfigStorage();
            fuiouPayConfigStorage.setKeyPublic(apyAccount.getPublicKey());
            fuiouPayConfigStorage.setKeyPrivate(apyAccount.getPrivateKey());
            fuiouPayConfigStorage.setNotifyUrl(apyAccount.getNotifyUrl());
            fuiouPayConfigStorage.setReturnUrl(apyAccount.getReturnUrl());
            fuiouPayConfigStorage.setSignType(apyAccount.getSignType());
            fuiouPayConfigStorage.setPayType(apyAccount.getPayType().toString());
            fuiouPayConfigStorage.setInputCharset(apyAccount.getInputCharset());
            fuiouPayConfigStorage.setTest(apyAccount.isTest());
            return new FuiouPayService(fuiouPayConfigStorage);
        }

        @Override
        public TransactionType getTransactionType(String transactionType) {
            return FuiouTransactionType.valueOf(transactionType);
        }


    }, unionPay {
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
            unionPayConfigStorage.setInputCharset(apyAccount.getInputCharset());
            unionPayConfigStorage.setTest(apyAccount.isTest());
            return new UnionPayService(unionPayConfigStorage);
        }

        @Override
        public TransactionType getTransactionType(String transactionType) {
            return UnionTransactionType.valueOf(transactionType);
        }


    }, payoneer {
        @Override
        public PayService getPayService(ApyAccount apyAccount) {
            PayoneerConfigStorage configStorage = new PayoneerConfigStorage();
            //设置商户Id
            configStorage.setProgramId(apyAccount.getPartner());
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


    }, payPal {
        @Override
        public PayService getPayService(ApyAccount apyAccount) {
            PayPalConfigStorage storage = new PayPalConfigStorage();
            //配置的附加参数的使用
            storage.setAttach(apyAccount.getPayId());
            storage.setClientID(apyAccount.getAppId());
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
