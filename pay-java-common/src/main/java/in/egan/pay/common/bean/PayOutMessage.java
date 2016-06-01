package in.egan.pay.common.bean;

import in.egan.pay.common.bean.outbuilder.TextBuilder;
import in.egan.pay.common.bean.outbuilder.XmlBuilder;

import java.io.Serializable;

/**
 *  支付宝支付通知
 * @author  egan
 * @email egzosn@gmail.com
 * @date 2016-6-1 11:40:30
 */
public  class PayOutMessage implements Serializable {
    protected String content;
    protected String msgType;


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    /**
     * 获得文本消息builder
     * @return
     */
    public static TextBuilder TEXT() {
        return new TextBuilder();
    }
    /**
     * 获得XML消息builder
     * @return
     */
    public static XmlBuilder XML() {
        return new XmlBuilder();
    }

}
