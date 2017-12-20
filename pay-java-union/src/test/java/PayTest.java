import com.egzosn.pay.common.bean.MethodType;
import com.egzosn.pay.common.bean.PayOrder;
import com.egzosn.pay.union.api.UnionPayConfigStorage;
import com.egzosn.pay.union.api.UnionPayService;
import com.egzosn.pay.union.bean.UnionTransactionType;

import java.awt.image.BufferedImage;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Descrption:银联支付测试
 * Author:Actinia
 * Date:2017/12/19  21:12
 */
public class PayTest {

    public static void main(String[] args) {


        UnionPayConfigStorage unionPayConfigStorage = new UnionPayConfigStorage();
        unionPayConfigStorage.setMerId("合作者id");
        unionPayConfigStorage.setKeyPublic("支付密钥");
        unionPayConfigStorage.setKeyPrivate("支付密钥");
        unionPayConfigStorage.setNotifyUrl("异步回调地址");
        unionPayConfigStorage.setReturnUrl("同步回调地址");
        unionPayConfigStorage.setSignType("MD5");
        unionPayConfigStorage.setInputCharset("utf-8");
        //是否为测试账号，沙箱环境
        unionPayConfigStorage.setTest(true);
        //支付服务
        UnionPayService service = new UnionPayService(unionPayConfigStorage);
        //支付订单基础信息
        PayOrder payOrder = new PayOrder("订单title", "摘要",  new BigDecimal(0.01) , new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));


        /*----------- 网关支付-------------------*/
        payOrder.setTransactionType(UnionTransactionType.WEB);
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
        params =   service.microPay(payOrder);
        /*-----------消费(被扫场景)------------------------------*/

//       /*-----------消费撤销------------------------------*/
        params =   service.unionRefundOrConsumeUndo("原交易查询流水号", "订单号", new BigDecimal("退款金额" ),UnionTransactionType.CONSUME_UNDO);
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
        String fileConten =   service.downloadbill(new Date(),"格式为MMDD");
        /*-----------退货交易：后台资金类交易，有同步应答和后台通知应答------------------------------*/


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
