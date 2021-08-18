package com.egzosn.pay.common.bean;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

/**`
 *
 * 默认的通知请求
 * @author Egan
 * <pre>
 * email egzosn@gmail.com
 * date 2021/8/18
 * </pre>
 */
public class DefaultNoticeRequest implements NoticeRequest {

    private Map<String, String[]> parameterMap;
    private InputStream inputStream;

    private  Map<String, List<String>> headers;

    public DefaultNoticeRequest(Map<String, String[]> parameterMap, InputStream inputStream) {
        this.parameterMap = parameterMap;
        this.inputStream = inputStream;
    }

    public DefaultNoticeRequest(Map<String, String[]> parameterMap, InputStream inputStream, Map<String, List<String>> headers) {
        this.parameterMap = parameterMap;
        this.inputStream = inputStream;
        this.headers = headers;
    }

    public DefaultNoticeRequest(InputStream inputStream, Map<String, List<String>> headers) {
        this.inputStream = inputStream;
        this.headers = headers;
    }

    /**
     * 根据请求头名称获取请求头信息
     *
     * @param name 名称
     * @return 请求头值
     */
    @Override
    public String getHeader(String name) {
        List<String> value = this.headers.get(name);
        return (null == value || value.isEmpty()) ? null : value.get(0);
    }

    /**
     * 根据请求头名称获取请求头信息
     *
     * @param name 名称
     * @return 请求头值
     */
    @Override
    public Enumeration<String> getHeaders(String name) {
        return Collections.enumeration(this.headers.get(name));
    }

    /**
     * 获取所有的请求头名称
     *
     * @return 请求头名称
     */
    @Override
    public Enumeration<String> getHeaderNames() {
        return Collections.enumeration(this.headers.keySet());
    }

    /**
     * 输入流
     *
     * @return 输入流
     * @throws IOException IOException
     */
    @Override
    public InputStream getInputStream() throws IOException {
        return inputStream;
    }

    /**
     * 获取所有的请求参数
     *
     * @return 请求参数
     */
    @Override
    public Map<String, String[]> getParameterMap() {
        return parameterMap;
    }
}
