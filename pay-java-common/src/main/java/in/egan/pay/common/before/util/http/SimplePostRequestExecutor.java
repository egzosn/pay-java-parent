package in.egan.pay.common.before.util.http;

import in.egan.pay.common.before.api.RequestExecutor;
import in.egan.pay.common.exception.PayErrorException;
import org.apache.http.Consts;
import org.apache.http.HttpHost;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.IOException;
import java.util.Map;

/**
 * @author  egan
 * @email egzosn@gmail.com
 * @date 2016-5-18 14:09:01
 * @author Daniel Qian
 * @source chanjarster/weixin-java-tools
 */
public class SimplePostRequestExecutor implements RequestExecutor<String, Object> {

    @Override
    public String execute(CloseableHttpClient httpclient, HttpHost httpProxy, String uri, Object postEntity) throws PayErrorException, ClientProtocolException, IOException {
        HttpPost httpPost = new HttpPost(uri);
        if (httpProxy != null) {
            RequestConfig config = RequestConfig.custom().setProxy(httpProxy).build();
            httpPost.setConfig(config);
        }


        if (postEntity instanceof Map) {
            StringBuilder builder = new StringBuilder();
            Map pe = (Map) postEntity;
            for (Object key : pe.keySet()) {
                builder.append(key).append("=").append(pe.get(key)).append("&");
            }
            if (builder.length() > 1) {
                builder.deleteCharAt(builder.length() - 1);
            }
            StringEntity entity = new StringEntity(builder.toString(), Consts.UTF_8);
            httpPost.setEntity(entity);
        } else if (postEntity instanceof String) {

            StringEntity entity = new StringEntity((String) postEntity, Consts.UTF_8);
            httpPost.setEntity(entity);
        }


        try (CloseableHttpResponse response = httpclient.execute(httpPost)) {
            String responseContent = Utf8ResponseHandler.INSTANCE.handleResponse(response);

            return responseContent;
        }finally {
            httpPost.releaseConnection();
        }
    }

}