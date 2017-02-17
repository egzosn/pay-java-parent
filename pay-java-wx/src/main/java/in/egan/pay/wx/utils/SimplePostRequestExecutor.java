package in.egan.pay.wx.utils;

import in.egan.pay.common.api.RequestExecutor;
import in.egan.pay.common.bean.result.PayError;
import in.egan.pay.common.exception.PayErrorException;
import in.egan.pay.common.util.XML;
import in.egan.pay.common.util.http.Utf8ResponseHandler;
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
 */
public class SimplePostRequestExecutor implements RequestExecutor<String, String> {

    @Override
    public String execute(CloseableHttpClient httpclient, HttpHost httpProxy, String uri, String postEntity) throws PayErrorException, ClientProtocolException, IOException {
        HttpPost httpPost = new HttpPost(uri);
        if (httpProxy != null) {
            RequestConfig config = RequestConfig.custom().setProxy(httpProxy).build();
            httpPost.setConfig(config);
        }

        if (postEntity != null) {
            StringEntity entity = new StringEntity(postEntity, Consts.UTF_8);
            httpPost.setEntity(entity);
        }


        try (CloseableHttpResponse response = httpclient.execute(httpPost)) {
            String responseContent = Utf8ResponseHandler.INSTANCE.handleResponse(response);
          /*  Map<String, Object> map = XML.toMap(responseContent);

            PayError error = PayError.fromMap(map);
            if (null != error) {
                throw new PayErrorException(error);
            }*/
            return responseContent;
        }

      /*  CloseableHttpResponse response = null;
        try {
            response = httpclient.execute(httpPost);
//            String responseContent = Utf8ResponseHandler.INSTANCE.handleResponse(response);
            String responseContent = Utf8ResponseHandler.INSTANCE.handleResponse(response);
            Map<String, Object> map = XML.toMap(responseContent);

            PayError error = PayError.fromMap(map);
            if (null != error) {
                throw new PayErrorException(error);
            }
            return responseContent;
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (response != null) {
                response.close();
            }
        }
        return null;*/
    }

}