package in.egan.pay.common.bean.result;

import in.egan.pay.common.util.json.WxGsonBuilder;

import java.io.Serializable;
import java.util.Map;

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

    public static PayError fromMap(Map<String, Object> map) {
        if (null == map.get("return_code") || "FAIL".equals( map.get("return_code") )){
            PayError error = new PayError(-1, null == map.get("return_msg") ? "未知错误！" : map.get("return_msg").toString());
            return error;
        }
        return null;

    }
}
