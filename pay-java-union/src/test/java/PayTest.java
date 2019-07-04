import com.egzosn.pay.common.bean.MethodType;
import com.egzosn.pay.common.bean.PayOrder;
import com.egzosn.pay.common.bean.RefundOrder;
import com.egzosn.pay.union.api.UnionPayConfigStorage;
import com.egzosn.pay.union.api.UnionPayService;
import com.egzosn.pay.union.bean.UnionTransactionType;

import java.awt.image.BufferedImage;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 *
 * Descrption:银联支付测试
 * Author:Actinia
 * Date:2017/12/19  21:12
 * 移步@see com.egzosn.pay.demo.controller.UnionPayController
 */
@Deprecated
public class PayTest {

    public static void main(String[] args) {


        UnionPayConfigStorage unionPayConfigStorage = new UnionPayConfigStorage();
        //是否为证书签名
        unionPayConfigStorage.setCertSign(true);
        //商户id
        unionPayConfigStorage.setMerId("商户id");
        //公钥，验签证书链格式： 中级证书路径;根证书路径
//        unionPayConfigStorage.setKeyPublic("D:/certs/acp_test_middle.cer;D:/certs/acp_test_root.cer");
        //中级证书路径
        unionPayConfigStorage.setAcpMiddleCert("D:/certs/acp_test_middle.cer");
        //根证书路径
        unionPayConfigStorage.setAcpRootCert("D:/certs/acp_test_root.cer");

        //私钥, 私钥证书格式： 私钥证书路径;私钥证书对应的密码
//        unionPayConfigStorage.setKeyPrivate("D:/certs/acp_test_sign.pfx;000000");
        // 私钥证书路径
        unionPayConfigStorage.setKeyPrivateCert("D:/certs/acp_test_sign.pfx");
        //私钥证书对应的密码
        unionPayConfigStorage.setKeyPrivateCertPwd("000000");

        unionPayConfigStorage.setNotifyUrl("异步回调地址");
        unionPayConfigStorage.setReturnUrl("同步回调地址");
        unionPayConfigStorage.setSignType("RSA2");
        unionPayConfigStorage.setInputCharset("UTF-8");
        //是否为测试账号，沙箱环境
        unionPayConfigStorage.setTest(true);
        //支付服务
        UnionPayService service = new UnionPayService(unionPayConfigStorage);
        //支付订单基础信息
        PayOrder payOrder = new PayOrder("订单title", "摘要",  new BigDecimal(0.01) , new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));


        /*----------- 网页支付-------------------*/
//        手机网页支付（WAP支付）
//        payOrder.setTransactionType(UnionTransactionType.WAP);
//        网关支付
        payOrder.setTransactionType(UnionTransactionType.WEB);
//        企业网银支付（B2B支付）
//        payOrder.setTransactionType(UnionTransactionType.B2B);
        //获取支付所需的信息
        Map directOrderInfo = service.orderInfo(payOrder);
        //获取表单提交对应的字符串，将其序列化到页面即可,
        String directHtml = service.buildRequest(directOrderInfo, MethodType.POST);
        /*-----------/网页支付-------------------*/
        Map<String, Object> params = null;


        /*-----------主扫申请二维码交易------------------------------*/
        payOrder.setTransactionType(UnionTransactionType.APPLY_QR_CODE);
        BufferedImage image = service.genQrPay(payOrder);
        /*-----------主扫申请二维码交易-----------------------------*/


        /*-----------消费(被扫场景)待定------------------------------*/
        payOrder.setTransactionType(UnionTransactionType.CONSUME);
        payOrder.setAuthCode("C2B码(条码号),1-20位数字");
        params =   service.microPay(payOrder);
        /*-----------消费(被扫场景)------------------------------*/

//       /*-----------消费撤销------------------------------*/
        params =   service.unionRefundOrConsumeUndo(new RefundOrder( "订单号", "原交易查询流水号", new BigDecimal("退款金额" )),UnionTransactionType.CONSUME_UNDO);
//       /*-----------消费撤销------------------------------*/

        /*-----------交易状态查询交易：只有同步应答------------------------------*/
        payOrder.setTransactionType(UnionTransactionType.QUERY);
        params =   service.query(null,"商户单号");
        /*-----------交易状态查询交易：只有同步应答------------------------------*/

        /*-----------退货交易：后台资金类交易，有同步应答和后台通知应答------------------------------*/
        payOrder.setTransactionType(UnionTransactionType.REFUND);
        params =   service.refund("原交易查询流水号", "订单号", null,new BigDecimal("退款金额" ));
        /*-----------退货交易：后台资金类交易，有同步应答和后台通知应答------------------------------*/


        /*-----------文件传输类接口：后台获取对账文件交易，只有同步应答 ------------------------------*/
        Map<String, Object> fileConten =   service.downloadbill(new Date(),"文件类型，一般商户填写00即可");      /*-----------退货交易：后台资金类交易，有同步应答和后台通知应答------------------------------*/


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
