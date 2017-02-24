


package in.egan.pay.demo.dao;

import in.egan.pay.common.bean.MsgType;
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
        apyAccount1.setPartner("2088911944978307");
        apyAccount1.setAppid("2016052301431829");
        // TODO 2017/2/9 16:20 author: egan  sign_type只有单一key时public_key与private_key相等，比如sign_type=MD5的情况
        apyAccount1.setPublicKey("48gf0iwuhr***********r9weh9eiut9");
        apyAccount1.setPrivateKey("48gf0iwuhr***********r9weh9eiut9");
        apyAccount1.setNotifyUrl("http://pay.egan.in/payBack2.json");
        // 无需同步回调可不填
        apyAccount1.setReturnUrl("");
        apyAccount1.setInputCharset("UTF-8");
        apyAccount1.setSignType("MD5");
        apyAccount1.setPayType(PayType.wxPay);
        apyAccount1.setMsgType(MsgType.xml);
        apyAccounts.put(1, apyAccount1);

        ApyAccount apyAccount2 = new ApyAccount();
        apyAccount2.setPayId(2);
        apyAccount2.setPartner("2088****8307");
        apyAccount2.setPublicKey("MIGfMA0GCSqGSIb3DQEBAQUAA4*****IZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB");
        apyAccount2.setPrivateKey("MIICdwIBADANBgkqhkiG9w0BAQ**********g51Vx8BvyypnIfKgw=");
        apyAccount2.setNotifyUrl("http://pay.egan.in/payBack3.json");
        // 无需同步回调可不填  app填这个就可以
        apyAccount2.setReturnUrl("m.alipay.com");
        apyAccount2.setSeller("egzosn@gmail.com");
        apyAccount2.setInputCharset("UTF-8");
        apyAccount2.setSignType("RSA");
        apyAccount2.setPayType(PayType.aliPay);
        apyAccount2.setMsgType(MsgType.text);
        apyAccounts.put(2, apyAccount2);

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
