package in.egan.pay.common.util.http;

/**
 * @author  egan
 * @email egzosn@gmail.com
 * @date 2016-5-24
 */

import in.egan.pay.common.api.RequestExecutor;
import in.egan.pay.common.bean.result.PayError;
import in.egan.pay.common.exception.PayErrorException;
import org.apache.http.HttpHost;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.IOException;



import org.apache.http.impl.client.CloseableHttpClient;

/**
 * 简单的GET请求执行器，请求的参数是String, 返回的结果也是String
 * @author Daniel Qian
 *
 */
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

        CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(httpGet);
            String responseContent = Utf8ResponseHandler.INSTANCE.handleResponse(response);

            if ("true".equals(responseContent)){ return responseContent; }

            throw new PayErrorException(new PayError(100101, "校验失败"));
       /* PayError error = PayError.fromJson(responseContent);
            if (error.getErrorCode() != 0) {
                throw new PayErrorException(error);
            }*/
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (response != null) {
                response.close();
            }
        }
    return null;

    }

}
