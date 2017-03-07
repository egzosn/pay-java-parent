package in.egan.pay.fuiou.utils;

import com.alibaba.fastjson.JSONObject;
import in.egan.pay.common.before.api.RequestExecutor;
import in.egan.pay.common.before.bean.result.PayError;
import in.egan.pay.common.exception.PayErrorException;
import in.egan.pay.common.util.XML;
import in.egan.pay.common.before.util.http.Utf8ResponseHandler;
import org.apache.http.Consts;
import org.apache.http.HttpHost;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author  egan
 * @email egzosn@gmail.com
 * @date 2016-5-18 14:09:01
 */
public class SimplePostRequestExecutor implements RequestExecutor<String, Object> {

    @Override
    public String execute(CloseableHttpClient httpclient, HttpHost httpProxy, String uri, Object postEntity) throws PayErrorException, ClientProtocolException, IOException {
        HttpPost httpPost = new HttpPost(uri);
//        httpPost.setHeader("Content-Type","application/x-www-from-urlencoded");
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
//            entity.setContentType("application/x-www-from-urlencoded");
            httpPost.setEntity(entity);
        } else if (postEntity instanceof String) {

            StringEntity entity = new StringEntity((String) postEntity, Consts.UTF_8);
            httpPost.setEntity(entity);
        }else if(postEntity instanceof List){
            //表单方式
            httpPost.setEntity(new UrlEncodedFormEntity((List<BasicNameValuePair>)postEntity, Consts.UTF_8));
        }

        try (CloseableHttpResponse response = httpclient.execute(httpPost)) {
            String responseContent = Utf8ResponseHandler.INSTANCE.handleResponse(response);
//            System.out.println("直接返回的查询结果-->"+responseContent);
//            responseContent = responseContent.replace("<ap>","").replace("</ap>","").replace("<plain>","").replace("</plain>","");
            JSONObject jsonObject = XML.toJSONObject(responseContent);//包含md5
            JSONObject plain = XML.toJSONObject("<a>"+jsonObject.getString("plain")+"</a>");//"plain" -> "<order_pay_code>5002</order_pay_code><order_pay_error>验证签名失败</order_pay_error><order_id></order_id><order_st></order_st><fy_ssn></fy_ssn><resv1></resv1>"

            if("0000".equals(plain.getString("order_pay_code"))){
                return "0000";
            }else{
                throw new PayErrorException(new PayError(404,plain.getString("order_pay_error"),responseContent));
            }
        }finally {
            httpPost.releaseConnection();
        }
    }
}