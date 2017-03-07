


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
        apyAccount1.setAppid("2016041301292728");
        // TODO 2017/2/9 16:20 author: egan  sign_type只有单一key时public_key与private_key相等，比如sign_type=MD5的情况
        apyAccount1.setPublicKey("MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB");
        apyAccount1.setPrivateKey("MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBANJ480HqtM0gcAFfU1Mi9D/U8VCb51nUIQkxAcbCABh1awhVte7cVRU5Qf+iJr9OewPVRSEyFzAbstrSDggv5Igq7qiQr3HeM265FIMdfY5snxe4u+BBrFDmdpewSAa6WpSJRtcKW2uO6Ui5ycD6n61j2B6TvCCKsDijTzFcxOcpAgMBAAECgYEAorOJKL2tQpz+uLDeEviEZAvS+ANtFo3bc+WEgAdcV4v9T5YibCG7TArVMC5DUcPzaIUnSYAVWMUwv+N9oWqUeB9sue04sdfaBpHPAaOSezzb4J/TtEaJoX72HcjWhgrG1AIAKyaw1P1nQ99zr2EbS1wIGXw4FNLWuo92tKDCsZkCQQD19Feh5Xm8N49Mqt8of+Mpez2dbxKVcU3yZLZFTDj47SZVto32q+kRqngRmgM037c2ms6Rnb9qyw6tbvzTUr8rAkEA2xGdPBu5Fmm2/+Jm8/1hxfqzwVATyc/TOAaeVAK7bS2GaWNoe4wJ4Xea/U2XPl+8+qP5Q+RYJ98IUOLC+e5o+wJBAKynEmEWikcipVhHVpHCfH8hARaj4uK+/92Y7w5kPFYZe2CN3sf604hQJysL9xZFuQH+1+UGXFmmIy6CC754hrMCQCmeoBPCZJiofvcAaXTjZ8b4SVxpvUizLjrPrxbg6gDlDEtLcpc+VMY8Nfr32csk3z9zFbFbWZBBpE/RtR9Mty8CQFqhVygF3FIiz5Sc38sz12RqOT7kUQK3R0FiATOtXkKHLzb/QyolOXi/avhLp/gIl7+IqZg51Vx8BvyypnIfKgw=");
        apyAccount1.setNotifyUrl("http://pay.egan.in/payBack2.json");
        // 无需同步回调可不填
        apyAccount1.setReturnUrl("");
        apyAccount1.setInputCharset("UTF-8");
        apyAccount1.setSignType("RSA");
        apyAccount1.setPayType(PayType.aliPay);
        apyAccount1.setMsgType(MsgType.text);
        apyAccounts.put(1, apyAccount1);

        ApyAccount apyAccount2 = new ApyAccount();
        apyAccount2.setPayId(2);
        apyAccount2.setPartner("2088911944978307");
        apyAccount2.setAppid("2017030105973474");
        apyAccount2.setPublicKey("MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDDI6d306Q8fIfCOaTXyiUeJHkrIvYISRcc73s3vF1ZT7XN8RNPwJxo8pWaJMmvyTn9N4HQ632qJBVHf8sxHi/fEsraprwCtzvzQETrNRwVxLO5jVmRGi60j8Ue1efIlzPXV9je9mkjzOmdssymZkh2QhUrCmZYI/FCEa3/cNMW0QIDAQAB");
        apyAccount2.setPrivateKey("MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAI6D7p2Dza+JYQEnLUKTeGTkJ/QSWA7dulJJS/8ETTQDEwb5ODwKXK3ItOLjeJ9yTAemSqJefIMHBTG+MvONrWgZpmz2oK8dAxH/iqyqxb4r9IODsMgzziNb9EjCs3vfHx9zgcmBhTDnqQZE0jnY2MchOXwnUDz45gDdxwhBHZEpAgMBAAECgYBzKU1CX2VwOVT3t46tQ7l/3ZYjdkdODAIpyCzu76zrIKjZc73rVnw5ufvhc+re/V9OtyOMPUnkH2IlJgrKIGxEg2OuBFdl8rDmNYjBdiFSykjMmsyj2s5uLZ73iEfXxtNDVrahh5ISdNYJAaANL6pMLE7mGoTinc/jv0cUKS1aAQJBAMrfLx/vnu5dvDE7+NvJ8xv8xLhR94NTzpGZl9tNYa2FgxsrD9n9j7SBf5AbWOcZzc5eRYMSDFt18DS5jpjx+2ECQQCz1l2AWWbWHwW+JcDgHI3CXUjOv4f1mltOc+tGA2aqKkXV6Euy5r5kla9BXCFr/b1XGGt4dhXDczWWLTfV7nLJAkAfMMkyA8lPpKG2gB32zMnP18D3BcMkMdJkmFS8pt0JrKlHmAtL3KwneHGAWnQEs/bsb5oIWNIFjdBsisrdwPaBAkEAm8ymOpC5Z91+cypv+3ihU3bPodYK6rssST5h4MkHg3qV/+h81GPVJH0NVPmFNvKfuzm7uaPL3QUNsqpACkXHCQJBAIfxCQjvmmj5HrXh0HF+VLYJVsVg6WVcLRNOLAi2xP39Ep1JN/Gsq7D/O7K9RJhs8g+69jKCVW83C4Wss/O7AQM=");
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
