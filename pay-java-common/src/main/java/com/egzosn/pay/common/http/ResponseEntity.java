package com.egzosn.pay.common.http;

import org.apache.http.Header;

/**
 * 响应实体
 * @author Egan
 * email egzosn@gmail.com
 * date 2021/8/1
 */
public class ResponseEntity<T> {
    private final int statusCode;
    private final Header[] headers;
    private final T body;

    public ResponseEntity(int statusCode, Header[] headers, T body) {
        this.statusCode = statusCode;
        this.headers = headers;
        this.body = body;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public Header[] getHeaders() {
        return headers;
    }

    public T getBody() {
        return body;
    }
}
