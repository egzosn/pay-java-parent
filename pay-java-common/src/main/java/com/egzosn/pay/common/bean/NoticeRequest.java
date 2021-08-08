package com.egzosn.pay.common.bean;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Map;

/**
 * 通知请求
 * @author Egan
 * email egzosn@gmail.com
 * date 2021/8/8
 */
public interface NoticeRequest {

    /**
     * 根据请求头名称获取请求头信息
      * @param name 名称
     * @return 请求头值
     */
    String getHeader(String name);
    /**
     * 根据请求头名称获取请求头信息
     * @param name 名称
     * @return 请求头值
     */
    Enumeration<String> getHeaders(String name);

    /**
     * 获取所有的请求头名称
     * @return 请求头名称
     */
    Enumeration<String> getHeaderNames();

    /**
     * 输入流
     * @return 输入流
     * @throws IOException IOException
     */
    InputStream getInputStream() throws IOException;

    /**
     *  获取所有的请求参数
     * @return 请求参数
     */
    Map<String, String[]> getParameterMap();
}
