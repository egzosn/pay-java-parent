package in.egan.pay.common.util.json;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import in.egan.pay.common.bean.result.PayError;

public class WxGsonBuilder {

    public static final GsonBuilder INSTANCE = new GsonBuilder();

    static {
        INSTANCE.disableHtmlEscaping();
        INSTANCE.registerTypeAdapter(PayError.class, new PayErrorAdapter());
    }

    public static Gson create() {
        return INSTANCE.create();
    }

}
