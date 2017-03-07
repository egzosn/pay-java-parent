package in.egan.pay.common.before.bean.result;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import in.egan.pay.common.util.XML;

import java.io.Serializable;

/**
 * 支付错误码说明
 * @author Daniel Qian
 * @dete 2017/1/12 9:57
 * @author: egan
 * @source chanjarster/weixin-java-tools
 * @see in.egan.pay.common.bean.result.PayError
 */
@Deprecated
public class PayError implements in.egan.pay.common.bean.result.PayError, Serializable {

    private int errorCode;

    private String errorMsg;

    private JSONObject json;
    private String responseContent;

    public PayError() {
    }
    public PayError(int errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }


    public PayError(int errorCode, String errorMsg, String responseContent) {
        this(errorCode, errorMsg);
        this.responseContent = responseContent;
    }


    public String getErrorCode() {
        return errorCode + "";
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    @Override
    public String getString() {
        return "支付错误: errcode=" + errorCode + ", errmsg=" + errorMsg + "\njson:" + json;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public JSONObject getJson() {
        return json;
    }

    public void setJson(JSONObject json) {
        this.json = json;
    }

    public String getResponseContent() {
        return responseContent;
    }

    public void setResponseContent(String responseContent) {
        this.responseContent = responseContent;
    }

    public static PayError fromJson(String json) {

        JSONObject jsonObject = JSON.parseObject(json);
        PayError error =   jsonObject.toJavaObject(PayError.class);
        error.setJson(jsonObject);
        error.setResponseContent(json);
        return error;
    }

    public static PayError fromXml(String xml) {
        JSONObject jsonObject = XML.toJSONObject(xml);
        if (null == jsonObject.get("return_code")){
            PayError error = new PayError(403, null == jsonObject.get("return_msg") ? "未知错误！" : jsonObject.get("return_msg").toString());
            return error;
        }

        if ("FAIL".equals( jsonObject.get("return_code"))){
            PayError error = new PayError(-1,  jsonObject.get("return_msg").toString());
            return error;
        }
        return null;
    }


    @Override
    public String toString() {
        return "支付错误: errcode=" + errorCode + ", errmsg=" + errorMsg + "\njson:" + json;
    }

}
