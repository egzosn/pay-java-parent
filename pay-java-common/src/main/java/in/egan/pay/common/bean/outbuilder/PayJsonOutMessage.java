package in.egan.pay.common.bean.outbuilder;

import in.egan.pay.common.api.PayConsts;
import in.egan.pay.common.bean.PayOutMessage;

/**
 * @author egan
 * @email egzosn@gmail.com
 * @date 2016-6-1 11:40:30
 */
public class PayJsonOutMessage extends PayOutMessage{

    public PayJsonOutMessage() {
        this.msgType = PayConsts.OUT_MSG_JSON;
    }

    @Override
    public String toMessage() {
        return getContent();
    }
}
