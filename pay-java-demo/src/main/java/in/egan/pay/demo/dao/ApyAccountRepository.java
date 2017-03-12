


package in.egan.pay.demo.dao;

import in.egan.pay.common.bean.MsgType;
import in.egan.pay.common.util.sign.SignUtils;
import in.egan.pay.demo.entity.ApyAccount;
import in.egan.pay.demo.entity.PayType;

import java.util.HashMap;
import java.util.Map;

/**
 * 账户
 * @author: egan
 * @email egzosn@gmail.com
 * @date 2016/11/18 1:21
 */
//@Repository
public class ApyAccountRepository {

    // 这里简单模拟，引入orm等框架之后可自行删除
    public static   Map<Integer, ApyAccount > apyAccounts = new HashMap<>();

    /**
     * 这里简单初始化，引入orm等框架之后可自行删除
     */
    {
        ApyAccount apyAccount1 = new ApyAccount();
        apyAccount1.setPayId(1);
        apyAccount1.setPartner("2088102169916436");
        apyAccount1.setAppid("2016080400165436");
        // TODO 2017/2/9 16:20 author: egan  sign_type只有单一key时public_key与private_key相等，比如sign_type=MD5的情况
        apyAccount1.setPublicKey("MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDIgHnOn7LLILlKETd6BFRJ0GqgS2Y3mn1wMQmyh9zEyWlz5p1zrahRahbXAfCfSqshSNfqOmAQzSHRVjCqjsAw1jyqrXaPdKBmr90DIpIxmIyKXv4GGAkPyJ/6FTFY99uhpiq0qadD/uSzQsefWo0aTvP/65zi3eof7TcZ32oWpwIDAQAB");
        apyAccount1.setPrivateKey("MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAKroe/8h5vC4L6T+B2WdXiVwGsMvUKgb2XsKix6VY3m2wcf6tyzpNRDCNykbIwGtaeo7FshN+qZxdXHLiIam9goYncBit/8ojfLGy2gLxO/PXfzGxYGs0KsDZ+ryVPPmE34ZZ8jiJpR0ygzCFl8pN3QJPJRGTJn5+FTT9EF/9zyZAgMBAAECgYAktngcYC35u7cQXDk+jMVyiVhWYU2ULxdSpPspgLGzrZyG1saOcTIi/XVX8Spd6+B6nmLQeF/FbU3rOeuD8U2clzul2Z2YMbJ0FYay9oVZFfp5gTEFpFRTVfzqUaZQBIjJe/xHL9kQVqc5xHlE/LVA27/Kx3dbC35Y7B4EVBDYAQJBAOhsX8ZreWLKPhXiXHTyLmNKhOHJc+0tFH7Ktise/0rNspojU7o9prOatKpNylp9v6kux7migcMRdVUWWiVe+4ECQQC8PqsuEz7B0yqirQchRg1DbHjh64bw9Kj82EN1/NzOUd53tP9tg+SO97EzsibK1F7tOcuwqsa7n2aY48mQ+y0ZAkBndA2xcRcnvOOjtAz5VO8G7R12rse181HjGfG6AeMadbKg30aeaGCyIxN1loiSfNR5xsPJwibGIBg81mUrqzqBAkB+K6rkaPXJR9XtzvdWb/N3235yPkDlw7Z4MiOVM3RzvR/VMDV7m8lXoeDde2zQyeMOMYy6ztwA6WgE1bhGOnQRAkEAouUBv1sVdSBlsexX15qphOmAevzYrpufKgJIRLFWQxroXMS7FTesj+f+FmGrpPCxIde1dqJ8lqYLTyJmbzMPYw==\n");
        apyAccount1.setNotifyUrl("http://pay.egan.in/payBack2.json");
        // 无需同步回调可不填
//        apyAccount1.setReturnUrl("");
        apyAccount1.setInputCharset("UTF-8");
        apyAccount1.setSeller("2088102169916436");
        apyAccount1.setSignType(SignUtils.RSA.name());
        apyAccount1.setPayType(PayType.aliPay);
        apyAccount1.setMsgType(MsgType.text);
        apyAccounts.put(apyAccount1.getPayId(), apyAccount1);

        ApyAccount apyAccount2 = new ApyAccount();
        apyAccount2.setPayId(2);
        apyAccount2.setPartner("208******978307");
        apyAccount2.setAppid("201*******73474");
        apyAccount2.setPublicKey("MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQ*********VGi60j8Ue1efIlzPXV9je9Hf8sxHi/fEsraprwCtzvzQETrNRwVxLO5jVmR");
        apyAccount2.setPrivateKey("MIICdwIBADANm**********Zpmz2oK8dAxH/iqyqxb4r9IODsMgzziNBgkqhkiG9w0BAQEFAASCAcmBhTDnqQZE0jeJ9yTAemSqJefIMHBTG+MvONrWg");
        apyAccount2.setNotifyUrl("http://pay.egan.in/payBack2.json");
        // 无需同步回调可不填  app填这个就可以
        apyAccount2.setReturnUrl("m.alipay.com");
        apyAccount2.setSeller("egzosn@gmail.com");
        apyAccount2.setInputCharset("UTF-8");
        apyAccount2.setSignType(SignUtils.RSA2.name());
        apyAccount2.setPayType(PayType.aliPay);
        apyAccount2.setMsgType(MsgType.text);
        apyAccounts.put(apyAccount2.getPayId(), apyAccount2);

        ApyAccount apyAccount3 = new ApyAccount();
        apyAccount3.setPayId(3);
        apyAccount3.setPartner("12****601");
        apyAccount3.setAppid("wxa39*****ba9e9");
        apyAccount3.setPublicKey("48gf0i************h9eiut9");
        apyAccount3.setPrivateKey("48gf0i************h9eiut9");
        apyAccount3.setNotifyUrl("http://pay.egan.in/payBack3.json");
        // 无需同步回调可不填  app填这个就可以
        apyAccount3.setReturnUrl("http://pay.egan.in/payBack3.json");
        apyAccount3.setSeller("12****601");
        apyAccount3.setInputCharset("UTF-8");
        apyAccount3.setSignType(SignUtils.MD5.name());
        apyAccount3.setPayType(PayType.wxPay);
        apyAccount3.setMsgType(MsgType.xml);
        apyAccounts.put(apyAccount3.getPayId(), apyAccount3);

    }
    //_____________________________________________________________


    /**
     * 根据id获取对应的账户信息
     * @param payId 账户id
     * @return
     */
    public ApyAccount findByPayId(Integer payId){
        // TODO 2016/11/18 1:23 author: egan  这里简单模拟 具体实现 略。。
        return apyAccounts.get(payId);
    }
}
