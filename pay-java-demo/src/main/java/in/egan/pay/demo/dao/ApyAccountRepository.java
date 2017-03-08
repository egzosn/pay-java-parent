


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
        apyAccount1.setPartner("2088****78307");
        apyAccount1.setAppid("20160****2728");
        // TODO 2017/2/9 16:20 author: egan  sign_type只有单一key时public_key与private_key相等，比如sign_type=MD5的情况
        apyAccount1.setPublicKey("MIGfMA0GCSqGSIb3DQE************LCUYuLkxpLQIDAQAB");
        apyAccount1.setPrivateKey("MIICdwIBADANBg************ZBBpE/RtR9Mty8CQFqhVygF3FIiz5Sc38sz12RqOT7kUQK3R0FiATOtXkKHLzb/QyolOXi/avhLp/gIl7+IqZg51Vx8BvyypnIfKgw=");
        apyAccount1.setNotifyUrl("http://pay.egan.in/payBack2.json");
        // 无需同步回调可不填
        apyAccount1.setReturnUrl("");
        apyAccount1.setInputCharset("UTF-8");
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
