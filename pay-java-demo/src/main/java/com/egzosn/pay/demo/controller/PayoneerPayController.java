package com.egzosn.pay.demo.controller;

import com.egzosn.pay.common.bean.*;
import com.egzosn.pay.common.http.HttpConfigStorage;
import com.egzosn.pay.demo.request.QueryOrder;
import com.egzosn.pay.payoneer.api.PayoneerConfigStorage;
import com.egzosn.pay.payoneer.api.PayoneerPayService;
import com.egzosn.pay.payoneer.bean.PayoneerTransactionType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author egan
 * email egzosn@gmail.com
 * date 2018/2/5
 */
@RestController
@RequestMapping("payoneer")
public class PayoneerPayController {


    private PayoneerPayService service = null;

    @PostConstruct
    public void init() {
        PayoneerConfigStorage configStorage = new PayoneerConfigStorage();
        configStorage.setProgramId("商户id");
        configStorage.setMsgType(MsgType.json);
        configStorage.setInputCharset("utf-8");
        configStorage.setUserName("PayoneerPay 用户名");
        configStorage.setApiPassword("PayoneerPay API password");
        // 是否为测试账号，沙箱环境
        configStorage.setTest(true);
        service = new PayoneerPayService(configStorage);

        //请求连接池配置
        HttpConfigStorage httpConfigStorage = new HttpConfigStorage();
        //最大连接数
        httpConfigStorage.setMaxTotal(20);
        //默认的每个路由的最大连接数
        httpConfigStorage.setDefaultMaxPerRoute(10);
        service.setRequestTemplateConfigStorage(httpConfigStorage);

        //以下不建议进行使用，会引起两次请求的问题
        //Basic Auth
       /* HttpConfigStorage httpConfigStorage = new  HttpConfigStorage();
        httpConfigStorage.setAuthUsername("PayoneerPay 用户名");
        httpConfigStorage.setAuthPassword("PayoneerPay API password");
        service = new PayoneerPayService(configStorage, httpConfigStorage);*/


    }


    /**
     * 获取授权页面
     * @param payeeId 用户id
     * @return 获取授权页面
     */
    @RequestMapping("getAuthorizationPage.json")
    public Map<String ,Object> getAuthorizationPage( String payeeId ){

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("code", 0);
        data.put("url", service.getAuthorizationPage(payeeId));
        return data;
    }

     /**
     * 获取授权用户信息，包含用户状态，注册时间，联系人信息，地址信息等等
     * @param payeeId 用户id
     * @return 获取授权用户信息
     */
    @RequestMapping("getAuthorizationUser.json")
    public Map<String ,Object> getAuthorizationUser( String payeeId ){

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("code", 0);
        data.put("url", service.getAuthorizationUser(payeeId));
        return data;
    }


    /**
     * 主动收款
     * @param price           金额
     * @param userId          付款用户
     * @return 支付结果
     */
    @ResponseBody
    @RequestMapping(value = "microPay.json")
    public Map<String, Object> microPay(BigDecimal price, String userId){

        PayOrder order = new PayOrder("Order_payment:", "Order payment", price, UUID.randomUUID().toString().replace("-", ""), PayoneerTransactionType.CHARGE);
        //币种
        order.setCurType(DefaultCurType.USD);
        //设置授权码，条码等
        order.setAuthCode( userId);
        //支付结果
        Map<String, Object> params = service.microPay(order);
        if (10700 == (Integer) params.get(PayoneerPayService.CODE)){
            System.out.println("未授权");
        }else  if (0 == (Integer) params.get(PayoneerPayService.CODE)){
            System.out.println("收款成功");
        }
        return params;
    }



    /**
     * 用户授权回调地址
     *
     * @param request 请求
     *
     * @return 是否成功
     * @throws IOException IOException
     */
    @RequestMapping(value = "payBack.json")
    public String payBack(HttpServletRequest request) throws IOException {

        //获取支付方返回的对应参数
        Map<String, Object> params = service.getParameter2Map(request.getParameterMap(), request.getInputStream());
        if (null == params) {
            return service.getPayOutMessage("fail", "失败").toMessage();
        }

        //校验
        if (service.verify(params)) {
            //这里处理业务逻辑
            //......业务逻辑处理块........
            return service.successPayOutMessage(null).toMessage();
        }

        return service.getPayOutMessage("fail", "失败").toMessage();
    }

    /**
     * 查询
     *
     * @param order 订单的请求体
     * @return 返回查询回来的结果集，支付方原值返回
     */
    @RequestMapping("query")
    public Map<String, Object> query(QueryOrder order) {
        return service.query(order.getTradeNo(), order.getOutTradeNo());
    }


    /**
     * 交易关闭接口
     *
     * @param order 订单的请求体
     * @return 返回支付方交易关闭后的结果
     */
    @RequestMapping("close")
    public Map<String, Object> close(QueryOrder order) {
        return service.close(order.getTradeNo(), order.getOutTradeNo());
    }
    /**
     * 申请退款接口
     *
     * @param order 订单的请求体
     * @return 返回支付方申请退款后的结果
     */
    @RequestMapping("refund")
    public Map<String, Object> refund(RefundOrder order) {
        return service.refund(order);
    }


    /**
     * 通用查询接口，根据 PayoneerTransactionType 类型进行实现
     *
     * @param order 订单的请求体
     * @return 返回支付方对应接口的结果
     */
    @RequestMapping("secondaryInterface")
    public Map<String, Object> secondaryInterface(QueryOrder order) {
        TransactionType type = PayoneerTransactionType.valueOf(order.getTransactionType());
        return service.secondaryInterface(order.getTradeNoOrBillDate(), order.getOutTradeNoBillType(), type);
    }


    /**
     * 转账
     *
     * @param order 转账订单
     *
     * @return 对应的转账结果
     */
    @RequestMapping("transfer")
    public Map<String, Object> transfer(TransferOrder order) {
        order.setOutNo("商户转账订单号");
        order.setCurType(DefaultCurType.USD);
        order.setPayeeAccount("收款方账户,用户授权所使用的userId");
        order.setAmount(new BigDecimal(10));
        order.setRemark("转账备注, 非必填");
        return service.transfer(order);
    }

    /**
     * 转账查询
     *
     * @param outNo   商户转账订单号
     * @param tradeNo 支付平台转账订单号
     *
     * @return 对应的转账订单
     */
    @RequestMapping("transferQuery")
    public Map<String, Object> transferQuery(String outNo, String tradeNo) {
        return service.transferQuery(outNo, tradeNo);
    }

}
