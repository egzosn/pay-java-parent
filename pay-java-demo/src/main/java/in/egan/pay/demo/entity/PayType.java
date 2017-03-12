package in.egan.pay.demo.entity;

import in.egan.pay.ali.api.AliPayConfigStorage;
import in.egan.pay.ali.api.AliPayService;
import in.egan.pay.ali.bean.AliTransactionType;
import in.egan.pay.common.api.PayService;
import in.egan.pay.common.bean.BasePayType;
import in.egan.pay.common.bean.TransactionType;
import in.egan.pay.wx.api.WxPayConfigStorage;
import in.egan.pay.wx.api.WxPayService;
import in.egan.pay.wx.bean.WxTransactionType;
import in.egan.pay.wx.youdian.api.WxYouDianPayConfigStorage;
import in.egan.pay.wx.youdian.api.WxYouDianPayService;
import in.egan.pay.wx.youdian.bean.YoudianTransactionType;


/**
 * 支付类型
 * @author egan
 * @email egzosn@gmail.com
 * @date 2016/11/20 0:30
 */
public enum PayType implements BasePayType {


    aliPay{
        /**
         *  @see in.egan.pay.ali.before.api.AliPayService 17年更新的版本,旧版本请自行切换
         * @param apyAccount
         * @return
         */
        @Override
        public PayService getPayService(ApyAccount apyAccount) {
            AliPayConfigStorage aliPayConfigStorage = new AliPayConfigStorage();
            aliPayConfigStorage.setPid(apyAccount.getPartner());
            aliPayConfigStorage.setAppId(apyAccount.getAppid());
            aliPayConfigStorage.setAliPublicKey(apyAccount.getPublicKey());
            aliPayConfigStorage.setKeyPrivate(apyAccount.getPrivateKey());
            aliPayConfigStorage.setNotifyUrl(apyAccount.getNotifyUrl());
            aliPayConfigStorage.setReturnUrl(apyAccount.getReturnUrl());
            aliPayConfigStorage.setSignType(apyAccount.getSignType());
            aliPayConfigStorage.setSeller(apyAccount.getSeller());
            aliPayConfigStorage.setPayType(apyAccount.getPayType().toString());
            aliPayConfigStorage.setMsgType(apyAccount.getMsgType());
            aliPayConfigStorage.setInputCharset(apyAccount.getInputCharset());
            aliPayConfigStorage.setTest(apyAccount.isTest());
            return new AliPayService(aliPayConfigStorage);
        }

        @Override
        public TransactionType getTransactionType(String transactionType) {
            // in.egan.pay.ali.before.bean.AliTransactionType 17年更新的版本,旧版本请自行切换
            return AliTransactionType.valueOf(transactionType);
        }


    },wxPay {
        @Override
        public PayService getPayService(ApyAccount apyAccount) {
            WxPayConfigStorage wxPayConfigStorage = new WxPayConfigStorage();
            wxPayConfigStorage.setMchId(apyAccount.getPartner());
            wxPayConfigStorage.setAppSecret(apyAccount.getPublicKey());
            wxPayConfigStorage.setKeyPublic(apyAccount.getPublicKey());
            wxPayConfigStorage.setAppid(apyAccount.getAppid());
            wxPayConfigStorage.setKeyPrivate(apyAccount.getPrivateKey());
            wxPayConfigStorage.setNotifyUrl(apyAccount.getNotifyUrl());
            wxPayConfigStorage.setSignType(apyAccount.getSignType());
            wxPayConfigStorage.setPayType(apyAccount.getPayType().toString());
            wxPayConfigStorage.setMsgType(apyAccount.getMsgType());
            wxPayConfigStorage.setInputCharset(apyAccount.getInputCharset());
            wxPayConfigStorage.setTest(apyAccount.isTest());
            return  new WxPayService(wxPayConfigStorage);
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
    };

    public abstract PayService getPayService(ApyAccount apyAccount);


}
