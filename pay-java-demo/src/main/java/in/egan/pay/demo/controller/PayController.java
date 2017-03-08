
package in.egan.pay.demo.controller;


import in.egan.pay.ali.before.bean.AliTransactionType;
import in.egan.pay.common.api.Callback;
import in.egan.pay.common.bean.*;
import in.egan.pay.common.util.str.StringUtils;
import in.egan.pay.demo.entity.ApyAccount;
import in.egan.pay.demo.entity.PayType;
import in.egan.pay.demo.service.ApyAccountService;
import in.egan.pay.demo.service.PayResponse;
import in.egan.pay.common.api.PayConfigStorage;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static in.egan.pay.demo.dao.ApyAccountRepository.apyAccounts;

/**
 * 发起支付入口
 * @author: egan
 * @email egzosn@gmail.com
 * @date 2016/11/18 0:25
 */
@RestController
@RequestMapping
public class PayController{

    @Resource
    private ApyAccountService service;


    /**
     * 这里模拟账户信息增加
     * @param account
     * @return
     */
    @RequestMapping("add")
    public Map<String, Object> add(ApyAccount account){
        apyAccounts.put(account.getPayId(), account);
        Map<String, Object> data = new HashMap<>();
        data.put("code", 0);
        data.put("account", account);

        return data;
    }


    /**
     * 跳到支付页面
     * 针对实时支付,即时付款
     *
     * @param payId 账户id
     * @param transactionType 交易类型， 这个针对于每一个 支付类型的对应的几种交易方式
     * @param bankType 针对刷卡支付，卡的类型，类型值
     * @return
     */
    @RequestMapping(value = "toPay.html", produces = "text/html;charset=UTF-8")
    public String toPay( Integer payId, String transactionType, String bankType,  BigDecimal price) {
        //获取对应的支付账户操作工具（可根据账户id）
        PayResponse payResponse =  service.getPayResponse(payId);

        PayOrder order = new PayOrder("订单title", "摘要",  null == price ? new BigDecimal(0.01) : price, UUID.randomUUID().toString().replace("-", ""), PayType.valueOf(payResponse.getStorage().getPayType()).getTransactionType(transactionType));

        //此处只有刷卡支付(银行卡支付)时需要
        if (StringUtils.isNotEmpty(bankType)){
            order.setBankType(bankType);
        }
        Map orderInfo = payResponse.getService().orderInfo(order);
        return  payResponse.getService().buildRequest(orderInfo, MethodType.POST);
    }


    /**
     * 获取二维码图像
     * 二维码支付
     * @return
     */
    @RequestMapping(value = "toQrPay.jpg", produces = "image/jpeg;charset=UTF-8")
    public byte[] toWxQrPay(Integer payId, String transactionType,  BigDecimal price) throws IOException {
        //获取对应的支付账户操作工具（可根据账户id）
        PayResponse payResponse =  service.getPayResponse(payId);
        //获取订单信息
        Map<String, Object> orderInfo = payResponse.getService().orderInfo(new PayOrder("订单title", "摘要",  null == price ? new BigDecimal(0.01) : price, UUID.randomUUID().toString().replace("-", ""), PayType.valueOf(payResponse.getStorage().getPayType()).getTransactionType(transactionType)));

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(payResponse.getService().genQrPay(orderInfo), "JPEG", baos);
        return baos.toByteArray();
    }


    /**
     *
     *  获取支付预订单信息
     * @param payId 支付账户id
     * @param transactionType 交易类型
     * @return
     */
    @RequestMapping("getOrderInfo")
    public Map<String, Object> getOrderInfo(Integer payId, String transactionType, BigDecimal price){
        //获取对应的支付账户操作工具（可根据账户id）
        PayResponse payResponse =  service.getPayResponse(payId);
        Map<String, Object> data = new HashMap<>();
        data.put("code", 0);
        PayOrder order = new PayOrder("订单title", "摘要",   null == price ? new BigDecimal(0.01) : price, UUID.randomUUID().toString().replace("-", ""), PayType.valueOf(payResponse.getStorage().getPayType()).getTransactionType(transactionType));
        data.put("orderInfo",  payResponse.getService().orderInfo(order));
        return data;
    }




    /**
     * 微信或者支付宝回调地址
     * @param request
     * @return
     */
    @RequestMapping(value = "payBack{payId}.json")
    public String payBack(HttpServletRequest request, @PathVariable Integer payId) throws IOException {
        //根据账户id，获取对应的支付账户操作工具
        PayResponse payResponse = service.getPayResponse(payId);
        PayConfigStorage storage = payResponse.getStorage();
        //获取支付方返回的对应参数
        Map<String, String> params = payResponse.getService().getParameter2Map(request.getParameterMap(), request.getInputStream());
        if (null == params){
            return payResponse.getService().getPayOutMessage("fail","失败").toMessage();
        }

        //校验
        if (payResponse.getService().verify(params)){
            PayMessage message = new PayMessage(params, storage.getPayType(), storage.getMsgType().name());
            PayOutMessage outMessage = payResponse.getRouter().route(message);
            return outMessage.toMessage();
        }

        return payResponse.getService().getPayOutMessage("fail","失败").toMessage();
    }

    /**
     * 查询
     * @param payId
     * @return
     */
    @RequestMapping("query")
    public Map<String, Object> query(Integer payId) {
        PayResponse payResponse = service.getPayResponse(payId);


        return payResponse.getService().query("2017012921001004530273937216", "8a2950f959cf08740159ea0666fc04bd");
    }

    /**
     * 通用接口，根据 TransactionType 类型进行实现
     * @param payId
     * @return
     */
    @RequestMapping("query1")
    public Map<String, Object> query1(Integer payId,  String transactionType) {
        PayResponse payResponse = service.getPayResponse(payId);
        TransactionType type = PayType.valueOf(payResponse.getStorage().getPayType()).getTransactionType(transactionType);
        return payResponse.getService().secondaryInterface("2017012921001004530273937216", "8a2950f959cf08740159ea0666fc04bd", type, new Callback<Map<String, Object>>() {
            @Override
            public Map<String, Object> perform(Map<String, Object> map) {
                return map;
            }
        });
    }





}
