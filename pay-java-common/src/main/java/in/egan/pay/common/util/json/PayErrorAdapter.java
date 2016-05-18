package in.egan.pay.common.util.json;

/*
 * KINGSTAR MEDIA SOLUTIONS Co.,LTD. Copyright c 2005-2013. All rights reserved.
 *
 * This source code is the property of KINGSTAR MEDIA SOLUTIONS LTD. It is intended
 * only for the use of KINGSTAR MEDIA application development. Reengineering, reproduction
 * arose from modification of the original source, or other redistribution of this source
 * is not permitted without written permission of the KINGSTAR MEDIA SOLUTIONS LTD.
 */

import com.google.gson.*;
import java.lang.reflect.Type;
import in.egan.pay.common.bean.result.PayError;

/**
 *
 * @author Daniel Qian
 *
 */
public class PayErrorAdapter implements JsonDeserializer<PayError> {

    public PayError deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        PayError wxError = new PayError();
        JsonObject payErrorJsonObject = json.getAsJsonObject();

        if (payErrorJsonObject.get("errcode") != null && !payErrorJsonObject.get("errcode").isJsonNull()) {
            wxError.setErrorCode(GsonHelper.getAsPrimitiveInt(payErrorJsonObject.get("errcode")));
        }
        if (payErrorJsonObject.get("errmsg") != null && !payErrorJsonObject.get("errmsg").isJsonNull()) {
            wxError.setErrorMsg(GsonHelper.getAsString(payErrorJsonObject.get("errmsg")));
        }
        wxError.setJson(json.toString());
        return wxError;
    }

}
