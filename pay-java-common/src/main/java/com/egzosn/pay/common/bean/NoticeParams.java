/*
 * Copyright 2017-2021 the original  Egan.
 * email egzosn@gmail.com
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.egzosn.pay.common.bean;

import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

/**
 * 通知参数
 *
 * @author Egan
 * email egzosn@gmail.com
 * date 2021/8/8
 */
public class NoticeParams {


    /**
     * 为了获取request里面传过来的动态参数
     */
    private Map<String, Object> body;

    /**
     * 存放请求头信息
     */
    private Map<String, List<String>> headers;

    /**
     * 附加属性
     */
    private Map<String, Object> attr;

    public NoticeParams() {
    }

    public NoticeParams(Map<String, Object> body) {
        this.body = body;
    }

    public NoticeParams(Map<String, Object> body, Map<String, List<String>> headers) {
        this.body = body;
        this.headers = headers;
    }

    public String getHeader(String name) {
        List<String> value = this.headers.get(name);
        return (null == value || value.isEmpty()) ? null : value.get(0);
    }

    public Enumeration<String> getHeaders(String name) {
        List<String> value = this.headers.get(name);
        return (Collections.enumeration(value != null ? value : Collections.<String>emptySet()));
    }

    public Enumeration<String> getHeaderNames() {
        if (null == headers){
            return  Collections.enumeration(Collections.emptySet());
        }
        return Collections.enumeration(this.headers.keySet());
    }


    public Map<String, Object> getBody() {
        return body;
    }

    public void setBody(Map<String, Object> body) {
        this.body = body;
    }

    public Map<String, List<String>> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, List<String>> headers) {
        this.headers = headers;
    }

    public Map<String, Object> getAttr() {
        return attr;
    }

    public void setAttr(Map<String, Object> attr) {
        this.attr = attr;
    }
}
