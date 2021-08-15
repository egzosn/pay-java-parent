package com.egzosn.pay.web.support;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.egzosn.pay.common.bean.NoticeRequest;

/**
 * web 相关请求支持
 *
 * @author Egan
 * <pre>
 * email egzosn@gmail.com
 * date 2021/8/16
 * </pre>
 */
public class HttpRequestNoticeParams implements NoticeRequest {


    private final HttpServletRequest httpServletRequest;

    public HttpRequestNoticeParams(HttpServletRequest httpServletRequest) {
        this.httpServletRequest = httpServletRequest;
    }

    /**
     * 根据请求头名称获取请求头信息
     *
     * @param name 名称
     * @return 请求头值
     */
    @Override
    public String getHeader(String name) {
        return httpServletRequest.getHeader(name);
    }

    /**
     * 根据请求头名称获取请求头信息
     *
     * @param name 名称
     * @return 请求头值
     */
    @Override
    public Enumeration<String> getHeaders(String name) {
        return httpServletRequest.getHeaders(name);
    }

    /**
     * 获取所有的请求头名称
     *
     * @return 请求头名称
     */
    @Override
    public Enumeration<String> getHeaderNames() {
        return httpServletRequest.getHeaderNames();
    }

    /**
     * 输入流
     *
     * @return 输入流
     * @throws IOException IOException
     */
    @Override
    public InputStream getInputStream() throws IOException {
        return httpServletRequest.getInputStream();
    }

    /**
     * 获取所有的请求参数
     *
     * @return 请求参数
     */
    @Override
    public Map<String, String[]> getParameterMap() {
        return httpServletRequest.getParameterMap();
    }
}
