


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
 * @email egzosn@gmail.com
 * @date 2016/11/18 1:21
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
        apyAccount1.setPrivateKey("MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAKroe/8h5vC4L6T+B2WdXiVwGsMvUKgb2XsKix6VY3m2wcf6tyzpNRDCNykbIwGtaeo7FshN+qZxdXHLiIam9goYncBit/8ojfLGy2gLxO/PXfzGxYGs0KsDZ+ryVPPmE34ZZ8jiJpR0ygzCFl8pN3QJPJRGTJn5+FTT9EF/9zyZAgMBAAECgYAktngcYC35u7cQXDk+jMVyiVhWYU2ULxdSpPspgLGzrZyG1saOcTIi/XVX8Spd6+B6nmLQeF/FbU3rOeuD8U2clzul2Z2YMbJ0FYay9oVZFfp5gTEFpFRTVfzqUaZQBIjJe/xHL9kQVqc5xHlE/LVA27/Kx3dbC35Y7B4EVBDYAQJBAOhsX8ZreWLKPhXiXHTyLmNKhOHJc+0tFH7Ktise/0rNspojU7o9prOatKpNylp9v6kux7migcMRdVUWWiVe+4ECQQC8PqsuEz7B0yqirQchRg1DbHjh64bw9Kj82EN1/NzOUd53tP9tg+SO97EzsibK1F7tOcuwqsa7n2aY48mQ+y0ZAkBndA2xcRcnvOOjtAz5VO8G7R12rse181HjGfG6AeMadbKg30aeaGCyIxN1loiSfNR5xsPJwibGIBg81mUrqzqBAkB+K6rkaPXJR9XtzvdWb/N3235yPkDlw7Z4MiOVM3RzvR/VMDV7m8lXoeDde2zQyeMOMYy6ztwA6WgE1bhGOnQRAkEAouUBv1sVdSBlsexX15qphOmAevzYrpufKgJIRLFWQxroXMS7FTesj+f+FmGrpPCxIde1dqJ8lqYLTyJmbzMPYw==\n");
        apyAccount1.setNotifyUrl("http://pay.egan.in/payBack1.json");
        // 无需同步回调可不填
        apyAccount1.setReturnUrl("http://pay.egan.in/payBack1.json");
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
        apyAccount2.setPartner("2088102169916436");
        apyAccount2.setAppid("2016080400165436");
        // TODO 2017/2/9 16:20 author: egan  sign_type只有单一key时public_key与private_key相等，比如sign_type=MD5的情况
        apyAccount2.setPublicKey("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAtIlhP1ju6g7fMIcp5cR+v7bk7RUI+irR3HUm0en39K/UnGByAulGFLZU9//IwIu3xCTfGkRHWV8v9iMJhNmxWqbxwehblTCl4f4uEDz9nECc1QzOesGPx6nMsbAx8+3j/Z2p1OCk+Gszs/nUBEjVY/l8NOQoE5kENyWl0WBTFPPONWm8V02YO8dNx0u29egh0dk17OeS+i5G0F+OY7xWnfjSrOqPYAtoo9ccEQUIbhiWz5X3TqmkzjNL3JojsvrymLym97z0COYfl1RlP8tqYhNKLM0FmVndRfcO9HgTx1mO4tj2hsDZN/AnAb3vULaMlESxIg+i6yUBpEOiwj+zLQIDAQAB");
        apyAccount2.setPrivateKey("MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCKA8XdHGExkLnb2Z3mF+6HQUorznXT0XE0GDiRtR+bIM/lE1o6UvAi2vSL5VT1Ob2+yzaSB2zDbfZJrzWcWveU8emN/xVd/yodUbzWnSQkI0k7suLeZDDCcxjT3g5JFSbcWmUec+OFt4n3Ymvrz0rriXfMV4MYlAmru+2UpiyA18SPXUiWWtHXhM4mJ5qAK6Xy2Xpg8fbqcbBLnV5GMipzno2VU0K1WKSe0qhOjPN7vvyZcRXRo5HGd76/Dhc/LhWgM/Ff2oaqQWhSo0tj33/2xmOLxueZaJw5Tx6sBxaBpIaX1huASmeFNgRNb1hSUwbVqkJKuvyEMQvTcz+9wqd9AgMBAAECggEAbtec33ndbUtImZLqx1LNFP+QrCICa324JxiUxiDrI1F05gtm2ZSmm7ex03D5jIE1LLhUOW+RIFt+A6udqNc+I/ctlgcZiAZYMnfk65AE6KkWNcXzgpyAtIpvlCiw3G0KasqGj7CPOGMxgOcguaAgEY60Ef7uoJy0L6KcqlfTQh3X26lHSyduTT/eFQmGstNnfZSnoSgdWoGYJcYwxeI/FZM/AF+qQxqurrB/MWihA+41dko2s+pG/gsnv3JtPiU7uxmzIhkKKo34bNgne6YtLibzamSe2RQNnqRykasrKEibtrUtFoyyzHRhUEjhW4la9je0H3weeyEIqYYrwq3kmQKBgQDUpKh2rmj54ovN7YAk0RkFj495od7IKN4EXRfjeiZHVrsJ97LNzYh2ldh8LRuHOGNP1jI5lBA6UL+VdjObtAiP0a3i5mLgREQ6vU4aQVEgVXiFTiFpgiwURHk3uX7S7DiOolvChmO9UTR91vaATMsTo4/uBLAkC0tQhxLrP/LAIwKBgQCmJ7tBQ9PgKeZCVcfEJQTSUwYeQD+TozjmkBCyn4S6U3pntZAj9XTUVKRiu1pvRCZZ8xjSHQ+xut8wcO4cBCUxSslDWD84HAAj5Aae80Ws7vBq6gS9G3ko1kVxLOOfA/74rB49BUEOcx9P6E2MWVSYhvtzyPvYi0PvLDQGRx2j3wKBgCRzPTVq3C/Jd7GK/qZQ+XsMismMx8WDy9rvokKqE5my8kjZHttMWhIJyZwvl0Jslgl/bAiWqtl0MgMKyfnsuHL+vFHisBxFV3TCMnspqyBhxlEDfZK6b5fhzO2SbHz3ZRJ+HkCQDNTM8LSJfqOrhjwNk4R3ZUIodXaOUN3mjlQFAoGASUJ7betaIBxZSiZITlOELLgummf5oD73d7FNq3RqMT1dWxzS0QgI2xX25RF2bli+ECr/ZqUpplOe1Nz2H6Q0QeeXlfny5epypWCFCtB4iCdSGdHVBQx3/2l6dMw2EIbShRJewsjuRlC8HZ9vkdJRWm8pr4OOh4vgCDSVO69fgkECgYEA0sjX815xMCwO1PDf+7/DNfJ5a2bYdMkVRe6y6a/Dzf83bJDdYZsOKlmSim49kPKEYU9MAT6Mst0Et9GpbzYDIZ4wj6aL01ckYvI42nhn2iylzYz+rOsg8mircFVcwiOh27BoNm8xpMFNzaY743gn37B8n3PaFboXWMO96y2rl8k=");
        apyAccount2.setNotifyUrl("http://pay.egan.in/payBack1.json");
        // 无需同步回调可不填
        apyAccount2.setReturnUrl("http://192.168.1.58:9096/payBack2.json");
        apyAccount2.setInputCharset("UTF-8");
        apyAccount2.setSeller("2088102169916436");
        apyAccount2.setSignType(SignUtils.RSA2.name());
        apyAccount2.setPayType(PayType.aliPay);
        apyAccount2.setMsgType(MsgType.text);
        //设置测试环境
        apyAccount2.setTest(true);
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
