package com.egzosn.pay.paypal.v2.api;

import java.util.Map;

import com.egzosn.pay.common.bean.outbuilder.TextBuilder;

/**
 * @author Egan
 * <pre>
 * email egzosn@gmail.com
 * date 2021/1/17
 * </pre>
 */
public class PayPalOutMessageBuilder extends TextBuilder {


    public PayPalOutMessageBuilder(Map<String, Object> message) {
        StringBuilder out = new StringBuilder();
        for (Map.Entry<String, Object> entry : message.entrySet()) {
            out.append(entry.getKey()).append('=').append(entry.getValue()).append("<br>");
        }
        super.content(out.toString());
    }


}
