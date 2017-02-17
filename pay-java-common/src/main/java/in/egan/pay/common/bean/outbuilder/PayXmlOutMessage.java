package in.egan.pay.common.bean.outbuilder;

import in.egan.pay.common.api.PayConsts;
import in.egan.pay.common.bean.PayOutMessage;

/**
 * @author egan
 * @email egzosn@gmail.com
 * @date 2016-6-1 13:53:3
 */
public class PayXmlOutMessage extends PayOutMessage{

    private String code;

    public PayXmlOutMessage() {
        this.msgType = PayConsts.OUT_MSG_XML;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toMessage() {
       return "<xml><return_code><![CDATA[" + code + "]]></return_code><return_msg><![CDATA[" + content
                + "]]></return_msg></xml>";
    }
}
