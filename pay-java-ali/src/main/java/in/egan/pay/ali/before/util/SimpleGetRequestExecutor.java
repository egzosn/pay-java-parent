package in.egan.pay.ali.before.util;

/**
 * @author  egan
 * @email egzosn@gmail.com
 * @date 2016-5-24
 */

import in.egan.pay.common.before.api.RequestExecutor;
import in.egan.pay.common.before.bean.result.PayError;
import in.egan.pay.common.exception.PayErrorException;
import in.egan.pay.common.before.util.http.Utf8ResponseHandler;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.IOException;

/**
 * 简单的GET请求执行器，请求的参数是String, 返回的结果也是String
 * @author Daniel Qian
 * @source chanjarster/weixin-java-tools
 *
 */
@Deprecated
public class SimpleGetRequestExecutor implements RequestExecutor<String, String> {

    @Override
    public String execute(CloseableHttpClient httpclient, HttpHost httpProxy, String uri, String queryParam) throws IOException, PayErrorException {
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
            String responseContent = Utf8ResponseHandler.INSTANCE.handleResponse(response);
            if ("true".equals(responseContent)){ return responseContent; }

            throw new PayErrorException(new PayError(100101, responseContent));
        }


    }

}
