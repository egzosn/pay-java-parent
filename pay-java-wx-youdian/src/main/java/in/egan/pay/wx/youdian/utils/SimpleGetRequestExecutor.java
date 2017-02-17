package in.egan.pay.wx.youdian.utils;



import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import in.egan.pay.common.api.RequestExecutor;
import in.egan.pay.common.bean.result.PayError;
import in.egan.pay.common.exception.PayErrorException;
import in.egan.pay.common.util.http.Utf8ResponseHandler;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import java.io.IOException;


/**
 * 简单的GET请求执行器，请求的参数是String, 返回的结果JSONObject
 * @author  egan
 * @email egzosn@gmail.com
 * @date 2017/01/12 22:58
 */
public class SimpleGetRequestExecutor implements RequestExecutor<JSONObject, String> {

    @Override
    public JSONObject execute(CloseableHttpClient httpclient, HttpHost httpProxy, String uri, String queryParam) throws IOException, PayErrorException {
        if (queryParam != null) {
            if (uri.indexOf('?') == -1) {
                uri += '?';
            }
            uri += uri.endsWith("?") ? queryParam : '&' + queryParam;
        }
        HttpGet httpGet = new HttpGet(uri);
        if (httpProxy != null) {
            RequestConfig config = RequestConfig.custom().setProxy(httpProxy).build();
            httpGet.setConfig(config);
        }

        try (CloseableHttpResponse response = httpclient.execute(httpGet)) {
            String text = Utf8ResponseHandler.INSTANCE.handleResponse(response);
            JSONObject jsonObject = JSON.parseObject(text);
            PayError payError = new PayError(jsonObject.getIntValue("errorcode"), jsonObject.getString("msg"), text);
            if (0 != payError.getErrorCode()){
                throw new PayErrorException(payError);
            }
            return jsonObject;
        }finally {
            httpGet.releaseConnection();
        }


    }

}
