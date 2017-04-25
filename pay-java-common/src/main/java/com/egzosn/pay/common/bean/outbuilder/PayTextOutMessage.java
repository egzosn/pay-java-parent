package com.egzosn.pay.common.bean.outbuilder;

import com.egzosn.pay.common.bean.MsgType;
import com.egzosn.pay.common.bean.PayOutMessage;

/**
 * @author egan
 *  <pre>
 *      email egzosn@gmail.com
 *      date 2016-6-1 11:40:30
 *  </pre>
 */
public class PayTextOutMessage extends PayOutMessage{

    public PayTextOutMessage() {
        this.msgType = MsgType.text.name();
    }

    @Override
    public String toMessage() {
        return getContent();
    }
}
