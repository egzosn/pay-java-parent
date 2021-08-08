import com.egzosn.pay.common.api.PayService;
import com.egzosn.pay.common.bean.MethodType;
import com.egzosn.pay.common.bean.PayOrder;
import com.egzosn.pay.fuiou.api.FuiouPayConfigStorage;
import com.egzosn.pay.fuiou.api.FuiouPayService;
import com.egzosn.pay.fuiou.bean.FuiouTransactionType;

import java.awt.image.BufferedImage;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/**
 *
 * 
 * 富友支付测试
 * @author egan
 * email egzosn@gmail.com
 * date 2017/8/18
 */
public class PayTest {

    public static void main(String[] args) {


        FuiouPayConfigStorage fuiouPayConfigStorage = new FuiouPayConfigStorage();
        fuiouPayConfigStorage.setMchntCd("合作者id");
        fuiouPayConfigStorage.setKeyPublic("支付密钥");
        fuiouPayConfigStorage.setKeyPrivate("支付密钥");
        fuiouPayConfigStorage.setNotifyUrl("异步回调地址");
        fuiouPayConfigStorage.setReturnUrl("同步回调地址");
        fuiouPayConfigStorage.setSignType("MD5");
        fuiouPayConfigStorage.setInputCharset("utf-8");
        //是否为测试账号，沙箱环境
        fuiouPayConfigStorage.setTest(true);
        //支付服务
        PayService service = new FuiouPayService(fuiouPayConfigStorage);
        //支付订单基础信息
        PayOrder payOrder = new PayOrder("订单title", "摘要",  BigDecimal.valueOf(0.01) , UUID.randomUUID().toString().replace("-", "").substring(2));


        /*----------- 网页支付-------------------*/
//        payOrder.setTransactionType(FuiouTransactionType.B2B);
        payOrder.setTransactionType(FuiouTransactionType.B2C);
        //获取支付所需的信息
        Map directOrderInfo = service.orderInfo(payOrder);
        //获取表单提交对应的字符串，将其序列化到页面即可,
        String directHtml = service.buildRequest(directOrderInfo, MethodType.POST);
        /*-----------/网页支付-------------------*/

        /*-----------回调处理-------------------*/

        Map<String, Object> params = null;
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
