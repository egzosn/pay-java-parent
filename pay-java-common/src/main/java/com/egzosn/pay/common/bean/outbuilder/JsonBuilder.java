
package com.egzosn.pay.common.bean.outbuilder;

import com.alibaba.fastjson.JSONObject;
import com.egzosn.pay.common.bean.PayOutMessage;

/**
 * @author egan
 *  <pre>
 *      email egzosn@gmail.com
 *      date 2017/1/13 14:30
 *   </pre>
 */
public class JsonBuilder  extends BaseBuilder<JsonBuilder, PayOutMessage>{
    JSONObject json = null;

    public JsonBuilder(JSONObject json) {
        this.json = json;
    }

    public JsonBuilder content(String key, Object content) {
        this.json.put(key, content);
        return this;
    }

    public JSONObject getJson() {
        return json;
    }

    @Override
    public PayOutMessage build() {
        PayJsonOutMessage message = new PayJsonOutMessage();
        setCommon(message);
        message.setContent(json.toJSONString());
        return message;
    }
}
