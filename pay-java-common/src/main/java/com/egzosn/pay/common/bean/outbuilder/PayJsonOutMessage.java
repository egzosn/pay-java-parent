package com.egzosn.pay.common.bean.outbuilder;

import com.egzosn.pay.common.bean.PayOutMessage;

/**
 * @author egan
 * <pre>
 *      email egzosn@gmail.com
 *      date 2016-6-1 11:40:30
 *   </pre>
 */
public class PayJsonOutMessage extends PayOutMessage {

    public PayJsonOutMessage() {

    }

    @Override
    public String toMessage() {
        return getContent();
    }


}
