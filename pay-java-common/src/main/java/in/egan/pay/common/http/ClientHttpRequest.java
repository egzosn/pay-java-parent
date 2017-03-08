package in.egan.pay.common.http;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import in.egan.pay.common.bean.MethodType;
import in.egan.pay.common.bean.result.PayException;
import in.egan.pay.common.exception.PayErrorException;
import in.egan.pay.common.util.XML;
import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

/**
 * 一个HTTP请求的客户端
 * @author: egan
 * @email egzosn@gmail.com
 * @date 2017/3/4 17:56
 */
public class ClientHttpRequest<T> extends HttpEntityEnclosingRequestBase implements  org.apache.http.client.ResponseHandler<T>{
    //http请求
    private  MethodType method;
    //响应类型
    private Class<T> responseType;


    public ClientHttpRequest<T> setResponseType(Class<T> responseType) {
        this.responseType = responseType;
        return this;
    }

    public ClientHttpRequest() {
    }

    public ClientHttpRequest(URI uri, MethodType method, Object request) {
        this.setURI(uri);
        this.method = method;
        setParameters(request);
    }
    public ClientHttpRequest(URI uri, MethodType method) {
        this.setURI(uri);
        this.method = method;
    }
    public ClientHttpRequest(URI uri) {
        this.setURI(uri);
    }

    public ClientHttpRequest(String uri) {
        this.setURI(URI.create(uri));
    }
    public ClientHttpRequest(String uri, MethodType method) {
        this.setURI(URI.create(uri));
        this.method = method;
    }

    public ClientHttpRequest(String uri, MethodType method, Object request) {
        this.setURI(URI.create(uri));
        this.method = method;
        setParameters(request);
    }

    public void setMethod(MethodType method) {
        this.method = method;
    }

    @Override
    public String getMethod() {
        return method.name();
    }

    /**
     * 设置代理
     * @param httpProxy http代理配置信息
     * @return
     */
    public ClientHttpRequest setProxy(HttpHost httpProxy){
        if (httpProxy != null) {
            RequestConfig config = RequestConfig.custom().setProxy(httpProxy).build();
            setConfig(config);
        }
        return this;
    }

    /**
     * Map转化为对应得参数字符串
     * @param pe
     * @return
     */
    public static String getMapToParameters(Map pe){
        StringBuilder builder = new StringBuilder();
        for (Object key : pe.keySet()) {
            Object o = pe.get(key);

            if (null == o) {
                continue;
            }

            if (o instanceof List) {
                o = ((List) o).toArray();
            }
            try {
                if (o instanceof Object[]) {
                    Object[] os = (Object[]) o;
                    String valueStr = "";
                    for (int i = 0, len = os.length; i < len; i++) {
                        if (null == os[i]) {
                            continue;
                        }
                        String value = os[i].toString().trim();
                        valueStr += (i == len - 1) ?  value :  value + ",";
                    }
                    builder.append(key).append("=").append(URLEncoder.encode(valueStr, "utf-8")).append("&");

                    continue;
                }
                builder.append(key).append("=").append(URLEncoder.encode((String) pe.get(key), "utf-8")).append("&");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        if (builder.length() > 1) {
            builder.deleteCharAt(builder.length() - 1);
        }
        return builder.toString();
    }

    /**
     * 设置请求参数
     *
     * @param request 请求参数
     * @return
     */
    public ClientHttpRequest setParameters(Object request) {
        if (null == request){
            return this;
        }
        if (request instanceof Map) {
            StringEntity entity = new StringEntity(getMapToParameters((Map) request), Consts.UTF_8);
            setEntity(entity);
        } else if (request instanceof String) {
            StringEntity entity = new StringEntity((String) request, Consts.UTF_8);
            setEntity(entity);
        } else {
            StringEntity entity = new StringEntity(JSON.toJSONString(request), ContentType.APPLICATION_JSON);
            setEntity(entity);
        }

        return this;

    }

    @Override
    public T handleResponse(HttpResponse response) throws ClientProtocolException, IOException {
        final StatusLine statusLine = response.getStatusLine();
        final HttpEntity entity = response.getEntity();

        if (statusLine.getStatusCode() >= 300 && statusLine.getStatusCode() != 304) {
            EntityUtils.consume(entity);
            throw new HttpResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase());
        }
        if (null == responseType){
            responseType = (Class<T>) String.class;
        }


        String[] value = entity.getContentType().getValue().split(";");

        if (ContentType.APPLICATION_OCTET_STREAM.getMimeType().equals(value[0])){

            if (responseType.isAssignableFrom(InputStream.class)){
                return (T)entity.getContent();
            }
            if (responseType.isAssignableFrom(OutputStream.class)){
                try {
                    T t = responseType.newInstance();
                    entity.writeTo((OutputStream)t);
                    return t;
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                throw  new HttpResponseException(statusLine.getStatusCode(), responseType + " 无法进行类型转换");
            }
        }
        String charset = "UTF-8";
        if (null != value && 2 == charset.length()) {
            charset = value[1].substring(value[1].indexOf("=") + 1);
        }
        String result = EntityUtils.toString(entity, charset);

        if (responseType.isAssignableFrom(String.class)){
            return (T)result;
        }

        String frist = result.substring(0, 1);
        if ( ContentType.APPLICATION_JSON.getMimeType().equals( value[0]) || "{[".indexOf(frist) >= 0 ){
            try {
                return JSON.parseObject(result, responseType);
            }catch (JSONException e){
                throw new PayErrorException(new PayException("failure", "类型转化异常,contentType:" + entity.getContentType().getValue(), result));
            }
        }

        if (ContentType.APPLICATION_XML.getMimeType().equals( value[0]) || "<".indexOf(frist) >= 0){
            return XML.toJSONObject(result).toJavaObject(responseType);
        }

        throw new PayErrorException(new PayException("failure", "类型转化异常,contentType:" + entity.getContentType().getValue(), result));

    }
}
