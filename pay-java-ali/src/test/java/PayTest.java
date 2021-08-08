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
        aliPayConfigStorage.setKeyPublic("支付宝公钥");
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
        aliPayConfigStorage.setPid("合作者id");
        aliPayConfigStorage.setAppId("应用id");
        //普通公钥方式与证书公钥方式为两者取其一的方式
        keyPublic(aliPayConfigStorage);
//        certKeyPublic(aliPayConfigStorage);
        aliPayConfigStorage.setKeyPrivate("应用私钥");
        aliPayConfigStorage.setNotifyUrl("异步回调地址");
        aliPayConfigStorage.setReturnUrl("同步回调地址");
        aliPayConfigStorage.setSignType("签名方式");
        aliPayConfigStorage.setSeller("收款账号");
        aliPayConfigStorage.setInputCharset("utf-8");
        //是否为测试账号，沙箱环境
        aliPayConfigStorage.setTest(true);
        //支付服务
        PayService service = new AliPayService(aliPayConfigStorage);
        //支付订单基础信息
        PayOrder payOrder = new PayOrder("订单title", "摘要",  BigDecimal.valueOf(0.01) , UUID.randomUUID().toString().replace("-", ""));
        /*-----------扫码付-------------------*/
        payOrder.setTransactionType(AliTransactionType.SWEEPPAY);
        //获取扫码付的二维码
        BufferedImage image = service.genQrPay(payOrder);
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
