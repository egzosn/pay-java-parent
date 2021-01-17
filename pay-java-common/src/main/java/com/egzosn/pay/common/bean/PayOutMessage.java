package com.egzosn.pay.common.bean;

import java.io.Serializable;

import com.alibaba.fastjson.JSONObject;
import com.egzosn.pay.common.bean.outbuilder.JsonBuilder;
import com.egzosn.pay.common.bean.outbuilder.TextBuilder;
import com.egzosn.pay.common.bean.outbuilder.XmlBuilder;

/**
 * 支付回调通知返回消息
 *
 * @author egan
 * <pre>
 *     email egzosn@gmail.com
 *     date 2016-6-1 11:40:30
 * </pre>
 */
public abstract class PayOutMessage implements Serializable {
    protected String content;


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    /**
     * 获得文本消息builder
     *
     * @return 文本消息builder
     */
    public static TextBuilder TEXT() {
        return new TextBuilder();
    }

    /**
     * 获得XML消息builder
     *
     * @return XML消息builder
     */
    public static XmlBuilder XML() {
        return new XmlBuilder();
    }

    /**
     * 获得Json消息builder
     *
     * @return Json消息builder
     */
    public static JsonBuilder JSON() {
        return new JsonBuilder(new JSONObject());
    }

    public abstract String toMessage();
}
