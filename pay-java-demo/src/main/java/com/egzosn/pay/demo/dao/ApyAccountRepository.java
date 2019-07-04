


package com.egzosn.pay.demo.dao;

import com.egzosn.pay.common.bean.MsgType;
import com.egzosn.pay.common.util.sign.SignUtils;
import com.egzosn.pay.demo.entity.ApyAccount;
import com.egzosn.pay.demo.entity.PayType;

import java.util.HashMap;
import java.util.Map;

/**
 * 账户
 * @author: egan
 * email egzosn@gmail.com
 * date 2016/11/18 1:21
 */
//@Repository
public class ApyAccountRepository {

    // 这里简单模拟，引入orm等框架之后可自行删除
    public static   Map<Integer, ApyAccount> apyAccounts = new HashMap<>();

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
        apyAccount1.setPrivateKey("MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAKroe/8h5vC4L6T+B2WdXiVwGsMvUKgb2XsKix6VY3m2wcf6tyzpNRDCNykbIwGtaeo7FshN+qZxdXHLiIam9goYncBit/8ojfLGy2gLxO/PXfzGxYGs0KsDZ+ryVPPmE34ZZ8jiJpR0ygzCFl8pN3QJPJRGTJn5+FTT9EF/9zyZAgMBAAECgYAktngcYC35u7cQXDk+jMVyiVhWYU2ULxdSpPspgLGzrZyG1saOcTIi/XVX8Spd6+B6nmLQeF/FbU3rOeuD8U2clzul2Z2YMbJ0FYay9oVZFfp5gTEFpFRTVfzqUaZQBIjJe/xHL9kQVqc5xHlE/LVA27/Kx3dbC35Y7B4EVBDYAQJBAOhsX8ZreWLKPhXiXHTyLmNKhOHJc+0tFH7Ktise/0rNspojU7o9prOatKpNylp9v6kux7migcMRdVUWWiVe+4ECQQC8PqsuEz7B0yqirQchRg1DbHjh64bw9Kj82EN1/NzOUd53tP9tg+SO97EzsibK1F7tOcuwqsa7n2aY48mQ+y0ZAkBndA2xcRcnvOOjtAz5VO8G7R12rse181HjGfG6AeMadbKg30aeaGCyIxN1loiSfNR5xsPJwibGIBg81mUrqzqBAkB+K6rkaPXJR9XtzvdWb/N3235yPkDlw7Z4MiOVM3RzvR/VMDV7m8lXoeDde2zQyeMOMYy6ztwA6WgE1bhGOnQRAkEAouUBv1sVdSBlsexX15qphOmAevzYrpufKgJIRLFWQxroXMS7FTesj+f+FmGrpPCxIde1dqJ8lqYLTyJmbzMPYw==");
        apyAccount1.setNotifyUrl("http://pay.egzosn.com/payBack1.json");
        // 无需同步回调可不填
        apyAccount1.setReturnUrl("http://pay.egzosn.com/payBack1.json");
        apyAccount1.setInputCharset("UTF-8");
        apyAccount1.setSeller("2088102169916436");
        apyAccount1.setSignType(SignUtils.RSA.name());
        apyAccount1.setPayType(PayType.aliPay);
        apyAccount1.setMsgType(MsgType.text);
        //设置测试环境
        apyAccount1.setTest(true);
        apyAccounts.put(apyAccount1.getPayId(), apyAccount1);

        ApyAccount apyAccount2 = new ApyAccount();
        apyAccount2.setPayId(2);
        apyAccount2.setPartner("1469188802");
        apyAccount2.setAppid("wx3344f4aed352deae");
        // TODO 2017/2/9 16:20 author: egan  sign_type只有单一key时public_key与private_key相等，比如sign_type=MD5的情况
        apyAccount2.setPublicKey("991ded080***************f7fc61095");
        apyAccount2.setPrivateKey("991ded080***************f7fc61095");
        apyAccount2.setNotifyUrl("http://pay.egzosn.com/payBack2.json");
        // 无需同步回调可不填
        apyAccount2.setReturnUrl("http://pay.egzosn.com");
        apyAccount2.setInputCharset("UTF-8");
        apyAccount2.setSeller("1469188802");
        apyAccount2.setSignType(SignUtils.MD5.name());
        apyAccount2.setPayType(PayType.wxPay);
        apyAccount2.setMsgType(MsgType.xml);
        //设置测试环境
        apyAccount2.setTest(false);
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

        ApyAccount apyAccount4 = new ApyAccount();
        apyAccount4.setPayId(4);
        apyAccount4.setPartner("700000000000001");
        //公钥，验签证书链格式： 中级证书路径;根证书路径
        apyAccount4.setPublicKey("D:/certs/acp_test_middle.cer;D:/certs/acp_test_root.cer");
        //私钥, 私钥证书格式： 私钥证书路径;私钥证书对应的密码
        apyAccount4.setPrivateKey("D:/certs/acp_test_sign.pfx;000000");
        apyAccount4.setNotifyUrl("http://127.0.0.1/payBack4.json");
        // 无需同步回调可不填  app填这个就可以
        apyAccount4.setReturnUrl("http://127.0.0.1/payBack4.json");
        apyAccount4.setSeller("");
        apyAccount4.setInputCharset("UTF-8");
        apyAccount4.setSignType(SignUtils.RSA2.name());
        apyAccount4.setPayType(PayType.unionPay);
        apyAccount4.setMsgType(MsgType.json);
        apyAccount4.setTest(true);
        apyAccounts.put(apyAccount4.getPayId(), apyAccount4);

        ApyAccount apyAccount5 = new ApyAccount();
        apyAccount5.setPayId(5);
        apyAccount5.setPartner("100086190");//Program ID
        apyAccount5.setSeller("egan6190");//Username
        apyAccount5.setStorePassword("12BkDT8152Zj");//API password
        apyAccount5.setInputCharset("UTF-8");
        apyAccount5.setPayType(PayType.payoneer);
        apyAccount5.setMsgType(MsgType.json);
        apyAccount5.setTest(true);
        apyAccounts.put(apyAccount5.getPayId(), apyAccount5);

        ApyAccount apyAccount6 = new ApyAccount();
        apyAccount6.setPayId(6);
        apyAccount6.setAppid("1AZ7HTcvrEAxYbzYx_iDZAi06GdqbjhqqQzFgPBFLxm2VUMzwlmiNUBk_y_5QNP4zWKblTuM6ZBAmxScd");//Program ID
        apyAccount6.setPrivateKey("1EBMIjAag6NiRdXZxteTv0amEsmKN345xJv3bN7f_HRXSqcRJlW7PXhYXjI9sk5I4nKYOHgeqzhXCXKFo");//API password
        apyAccount6.setInputCharset("UTF-8");
        apyAccount6.setPayType(PayType.payPal);
        apyAccount6.setMsgType(MsgType.json);
        apyAccount6.setTest(true);
        apyAccounts.put(apyAccount6.getPayId(), apyAccount6);
    }
    //_____________________________________________________________


    /**
     * 根据id获取对应的账户信息
     * @param payId 账户id
     * @return 账户信息
     */
    public ApyAccount findByPayId(Integer payId){
        // TODO 2016/11/18 1:23 author: egan  这里简单模拟 具体实现 略。。
        return apyAccounts.get(payId);
    }
}
