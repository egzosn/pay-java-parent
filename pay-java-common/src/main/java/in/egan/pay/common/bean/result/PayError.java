package in.egan.pay.common.bean.result;

import in.egan.pay.common.util.json.WxGsonBuilder;

import java.io.Serializable;

/**
 * 支付错误码说明
 * @author Daniel Qian
 *
 */
public class PayError implements Serializable {

    private int errorCode;

    private String errorMsg;

    private String json;

    public PayError(int errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public PayError() {
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public static PayError fromJson(String json) {
        PayError error = WxGsonBuilder.create().fromJson(json, PayError.class);
        return error;
    }

    @Override
    public String toString() {
        return "支付错误: errcode=" + errorCode + ", errmsg=" + errorMsg + "\njson:" + json;
    }

}
