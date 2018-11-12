package com.egzosn.pay.common.http;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 *  请求头
 *
 * @author egan
 *         <pre>
*               email egzosn@gmail.com
*               date 2018/01/30
*           </pre>
 */
public class HttpHeader{
    /**
     * 请求头
     */
    private List<Header> headers;

    public HttpHeader() {
    }

    public HttpHeader(List<Header> headers) {
        this.headers = headers;
    }

    /**
     * 请求头
     *
     * @param header 请求头
     */
    public HttpHeader(Header header) {
        addHeader(header);
    }

    /**
     * 获取请求头集
     *
     * @return 请求头集
     */
    public List<Header> getHeaders() {
        return headers;
    }

    /**
     * 添加请求头
     *
     * @param header 请求头
     */
    public void addHeader(Header header) {
        if (null == this.headers) {
            this.headers = new ArrayList<>();
        }
        this.headers.add(header);
    }

    /**
     * 设置请求头集
     *
     * @param headers 请求头集
     */
    public void setHeaders(List<Header> headers) {
        this.headers = headers;
    }

    /**
     * 设置请求头集
     *
     * @param headers 请求头集
     */
    public void setHeaders(Map<String, String> headers) {
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            addHeader(new BasicHeader(entry.getKey(), entry.getValue()));
        }
    }


}
