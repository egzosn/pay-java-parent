package com.egzosn.pay.common.http;


import java.util.Map;

public class HttpResult {

    public HttpResult(int code){
        this.statusCode = code;
    }

    public HttpResult(int code, String _content){
        this.statusCode = code;
        this.content = _content;
    }

    public HttpResult(Exception e){
        if(e==null){
            throw new IllegalArgumentException("exception must be specified");
        }
        this.statusCode = -1;
        this.exception = e;
        this.exceptionMsg = e.getMessage();
    }
    /**
     * HTTP状态码
     */
    private int statusCode;

    /**
     * HTTP结果
     */
    private String content;

    private String exceptionMsg;

    private Exception exception;

    private Map<String,String> headers;

    private String contentType;


    public String getHeaderField(String key){
        if(headers==null){
            return null;
        }
        return headers.get(key);
    }

    public String getContentType(){
        return contentType;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }


    public void setHeaders(Map<String,String> headers) {
        this.headers = headers;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getExceptionMsg() {
        return exceptionMsg;
    }

    public Exception getException() {
        return exception;
    }

    public boolean isSuccess(){
        return statusCode==200;
    }

    public boolean isError(){
        return exception!=null;
    }


    public void setContentType(String contentType) {
        this.contentType = contentType;
    }
}
