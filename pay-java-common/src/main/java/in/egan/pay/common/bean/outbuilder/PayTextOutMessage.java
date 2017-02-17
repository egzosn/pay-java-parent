package in.egan.pay.common.bean.outbuilder;

import in.egan.pay.common.api.PayConsts;
import in.egan.pay.common.bean.PayOutMessage;

/**
 * @author egan
 * @email egzosn@gmail.com
 * @date 2016-6-1 11:40:30
 */
public class PayTextOutMessage extends PayOutMessage{

    public PayTextOutMessage() {
        this.msgType = PayConsts.OUT_MSG_TEXT;
    }

    @Override
    public String toMessage() {
        return getContent();
    }
}
