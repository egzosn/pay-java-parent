import com.egzosn.pay.ali.api.AliPayConfigStorage;
import com.egzosn.pay.ali.api.AliPayService;
import com.egzosn.pay.ali.bean.AliTransactionType;
import com.egzosn.pay.common.api.PayService;
import com.egzosn.pay.common.bean.CertStoreType;
import com.egzosn.pay.common.bean.MethodType;
import com.egzosn.pay.common.bean.PayOrder;

import java.awt.image.BufferedImage;
import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

/**
 *
 *
 * 支付宝测试
 * @author egan
 * email egzosn@gmail.com
 * date 2017/8/18
 */
public class PayTest {

    /**
     * 设置普通公钥的方式
     * 普通公钥方式与证书公钥方式为两者取其一的方式
     * @param aliPayConfigStorage 支付宝配置信息
     *
     */
    private static void keyPublic(AliPayConfigStorage aliPayConfigStorage){
        aliPayConfigStorage.setKeyPublic("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEApTUBNm4Mf2K+PPWlluFUBhxu8ML3XgF7PoWHfLa470nTS3L5P9spfpFUbF4rjozBNblZ2QYWaskF92zSDegSvAzrgTMveHpv6+0G9uGgGLqObkPz3J8oQNioEL5Jtro2Zw7cVl+vpTIVWC9ChZE4acr1EDic4HJkWiA13OuyUx0Jl8wX9RXZPUp6nQ6LAz4FXGwfhy6zVveHvdkoeLxAo7ibcJh5eTjzDW0Ks4D32PwjU/uVIxeWAN4FybvF3wXOfl/RVYtbs9EZi63zlZI9h52Wj0hfFNcTNyGt/TBmYG1dRHRSkAGOGnza2ofBlV9y1PX9qL6O7Hoyu+BcTQRmCwIDAQAB");
    }

    /**
     * 设置证书公钥信息
     * 普通公钥方式与证书公钥方式为两者取其一的方式
     * @param aliPayConfigStorage 支付宝配置信息
     */
    private static void certKeyPublic(AliPayConfigStorage aliPayConfigStorage){
        //设置为证书方式
        aliPayConfigStorage.setCertSign(true);
        //设置证书存储方式，这里为路径
        aliPayConfigStorage.setCertStoreType(CertStoreType.PATH);
        aliPayConfigStorage.setMerchantCert("请填写您的应用公钥证书文件路径，例如：d:/appCertPublicKey_2019051064521003.crt");
        aliPayConfigStorage.setAliPayCert("请填写您的支付宝公钥证书文件路径，例如：d:/alipayCertPublicKey_RSA2.crt");
        aliPayConfigStorage.setAliPayRootCert("请填写您的支付宝根证书文件路径，例如：d:/alipayRootCert.crt");
    }

    public static void main(String[] args) {

        AliPayConfigStorage aliPayConfigStorage = new AliPayConfigStorage();
//        aliPayConfigStorage.setPid("合作者id");
        aliPayConfigStorage.setAppId("2021005104613925");
        //普通公钥方式与证书公钥方式为两者取其一的方式
        keyPublic(aliPayConfigStorage);
//        certKeyPublic(aliPayConfigStorage);
        aliPayConfigStorage.setKeyPrivate("MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQCocbFh28Ja+qhKXsSxkmu820LFE9rdVuJ1E7So/FcNxfqbHbGcd0iTOqZLy8gtw82BUDn+YJSrL3wpsWoKjL4n/LelMwjd3sYmOgx6hWrM6KBHFCSO2TDv4ZFQOEo8bHTotxQoVGFRrSpZ0VZlMe1BJvJdbgaZW9L02v9LOZfRtbR8QbPW2fzGoXLS/X0BVPCIm3W2BoI50RBHHGbW4rEx3iC93y8HM6efCbTYD3VwVFMAbYIcndrXomdQmkPnyIbtBMqvhzJCoDCwDHXJi2rhEQajpUlNVDtgiz9w12qaaaf+b/GChAdyGEBWilgaXI0DktJ21xxEhbvOvtDypMr5AgMBAAECggEAGP19HpKW/B5x906mCd46Q7OX7VsrvmjUgiAhTmzZkX6M1pSKNDdyOf2ajGY4VanWBFhdskKr69XIqvraS6Rj1dTlfgnbR+d0KFm2XjsLBEmC9eikH9lTLFOf1nHzeZFxqtD2maEjKxXw0ZCAK9VDaMYZcQkQy5HW82LGO2fZAVCgjnIC7HnbZIyTfRMRpnWGusEAFIqWGPos4L9N4G6nDtHQnY7G0JiSLb1X3g4bOFZFqyFQWqKWwY0b4TJmmiNHVY43o4EIS2kSRIGomjsDoHORSygit9jEsFtBMZB07JGHAjEeHNe7q3XsgR7AuK83gskDoHCUK1j+hK8p3u07MQKBgQDyx5q5xOhLnXTgAyFXlGYOVhb7izZp+tOl1CR73EL3ECu9oETHf0zQfGjImIeTt6ppn9iTCaqJk066zowVzCI83eCo6EaPHqVsFWEwiMXdg2O3LqQX/elWpAID6WQejTuRBUt+mtXKIA5Nd2VERLRvhwHKGW5FLHTr1ywVV6k2dQKBgQCxndYLQBQv2MqgniKwj+TSIQq8G6EvdCRBYJyyWgBKRbE+WZ+pFMDhIdnvqRXyj3jBMCmhD8QgAETkSrQqWuVPK+DUPu2MFzn+QAuiClvsN9zrplxf1ahv9m+MBcmk9wZy7C9eFF2UTrSrQTb8T52hhzCT2H5jTLGVLqLbrBFZ9QKBgQChaEka5qmV1AonAI3DPzsWnu/KrsQvc34QytiyrD6tKUgbWxk/FQhJt9ymOJoygDJA5O/E2fFSY+g81CNYPo4or060nTCD6FkMYa5q6RO7cKXz3INmG/5tBr78QMe6dCU9Bisp8eDe767ym8VsvMzFNRngdkXUoXjebwC088HhHQKBgQCuZDWT6D+p1ubkmz+eMjpOIskidtJlAFjUpCJWb03XnuPvPxonbkwPACi2SkNVXI8Ix9wn2o4LiZgaukp5R7Pcb02Zt4uPQALd405ItHkazqKn8TjDk8mE3OcbCVe6FB0N216Ktd5HzptPhoGRbC5uOYl1sFwv7U5zFk4q96A1vQKBgQDq6dK5t/BPQ6HQjb2Ys0CxWLefJn77zmyswGAfQ1VvZjcV9NSA5mVBVN3qU27oIcIkBIp+XE61lVPqRGzGxY8eZCdJPw5o0u9rGp/3mIS8WIZQ3Bkl+tkC5fS2bfwho9mJJJhvmFIddWqWMFCQRtk3TkwD0r2KqJ6pcQuyhipv0Q==");
        aliPayConfigStorage.setNotifyUrl("https://kgpg44675804.vicp.fun/pay/2");
        aliPayConfigStorage.setReturnUrl("https://kgpg44675804.vicp.fun/pay/2");
        aliPayConfigStorage.setSignType("RSA2");
//        aliPayConfigStorage.setSeller("收款账号");
        aliPayConfigStorage.setInputCharset("utf-8");
        //是否为测试账号，沙箱环境
//        aliPayConfigStorage.setTest(true);
        //支付服务
        PayService service = new AliPayService(aliPayConfigStorage);
        //支付订单基础信息
        PayOrder payOrder = new PayOrder("订单title", "摘要",  BigDecimal.valueOf(0.01) , UUID.randomUUID().toString().replace("-", ""));
        /*-----------扫码付-------------------*/
        payOrder.setTransactionType(AliTransactionType.SWEEPPAY);
        String image = service.getQrPay(payOrder);
        //获取扫码付的二维码
//        BufferedImage image = service.genQrPay(payOrder);
        /*-----------/扫码付-------------------*/

        /*-----------APP-------------------*/
        payOrder.setTransactionType(AliTransactionType.APP);
        //获取APP支付所需的信息组，直接给app端就可使用
        Map appOrderInfo = service.orderInfo(payOrder);
        /*-----------/APP-------------------*/

        /*-----------即时到帐 WAP 网页支付-------------------*/
//        payOrder.setTransactionType(AliTransactionType.WAP); //WAP支付

        payOrder.setTransactionType(AliTransactionType.PAGE); // 即时到帐 PC网页支付
        //获取支付所需的信息
        Map directOrderInfo = service.orderInfo(payOrder);
        //获取表单提交对应的字符串，将其序列化到页面即可,
        String directHtml = service.buildRequest(directOrderInfo, MethodType.POST);
        /*-----------/即时到帐 WAP 网页支付-------------------*/



        /*-----------条码付 声波付-------------------*/

//        payOrder.setTransactionType(AliTransactionType.WAVE_CODE); //声波付
        payOrder.setTransactionType(AliTransactionType.BAR_CODE);//条码付

        payOrder.setAuthCode("条码信息或者声波信息");
        // 支付结果
        Map params = service.microPay(payOrder);

        /*-----------/条码付 声波付-------------------*/

        /*-----------回调处理-------------------*/
//        HttpServletRequest request
//        params = service.getParameter2Map(request.getParameterMap(), request.getInputStream());
        if (service.verify(params)){
            System.out.println("支付成功");
            return;
        }
        System.out.println("支付失败");


        /*-----------回调处理-------------------*/
    }
}
