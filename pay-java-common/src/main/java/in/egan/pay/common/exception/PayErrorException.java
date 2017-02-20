package in.egan.pay.common.exception;

import in.egan.pay.common.bean.result.PayError;

/**
 * @author  egan
 * @email egzosn@gmail.com
 * @date 2016-5-18 14:09:01
 * @source chanjarster/weixin-java-tools
 */
public class PayErrorException extends Exception {

    private PayError error;

    public PayErrorException(PayError error) {
        super(error.toString());
        this.error = error;
    }

    public PayError getError() {
        return error;
    }
}
