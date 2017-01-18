/*
 * Copyright 2002-2017 the original huodull or egan.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package in.egan.pay.demo.controller;

import in.egan.pay.common.api.PayConfigStorage;
import in.egan.pay.common.bean.PayMessage;
import in.egan.pay.common.bean.PayOrder;
import in.egan.pay.common.bean.PayOutMessage;
import in.egan.pay.demo.entity.PayType;
import in.egan.pay.demo.service.ApyAccountService;
import in.egan.pay.demo.service.PayResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

/**
 *
 * @author: egan
 * @email egzosn@gmail.com
 * @date 2016/11/18 0:25
 */
@Controller
@RequestMapping
public class PayController {

    @Autowired
    private ApyAccountService service;

    /**
     * 跳到支付页面
     *  针对实时支付
     *
     * @return
     */
    @RequestMapping(value = "toPay.html", produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String toAliPay( String payId, String transactionType) {
        //。。。待实现
        return "";
    }


    /**
     *  获取支付预订单信息
     * @param payId 支付账户id
     * @param transactionType 交易类型
     * @return
     */
    @RequestMapping("getOrderInfo")
    public Object getOrderInfo( Integer payId, String transactionType){
        //获取对应的支付账户操作工具（可根据账户id）
        PayResponse payResponse =  service.getPayResponse(payId);;

        //这里之所以用Object，因为微信需返回Map， 支付吧String。
        Object orderInfo = payResponse.getService().orderInfo(new PayOrder("订单title", "摘要", new BigDecimal(0.01), "tradeNo", PayType.valueOf(payResponse.getStorage().getPayType()).getTransactionType(transactionType)));

        return orderInfo;
    }


    /**
     * 微信或者支付宝回调地址
     * @param request
     * @return
     */
    @ResponseBody
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




}
