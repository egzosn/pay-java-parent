package com.egzosn.pay.common.http;

import com.alibaba.fastjson.JSONObject;
import com.egzosn.pay.common.bean.result.PayException;
import com.egzosn.pay.common.exception.PayErrorException;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * URL表达式处理器
 *
 * @author: egan
 * <pre>
 * email egzosn@gmail.com
 * date 2017/3/5 10:07
 * </pre>
 */
public class UriVariables {

    /**
     * 依次匹配
     * @param uri 匹配的uri，带代表式
     * @param uriVariables 匹配表达式的值
     * @return 匹配完的url
     * <code>
     *     System.out.println(getUri(&quot;http://egan.in/{a}/ba/{a1}?{bb}={a1}&quot;, &quot;no1&quot;, &quot;no2&quot;, &quot;no3&quot;, &quot;no4&quot;));
     *    结果 http://egan.in/no1/ba/no2?no3=no4
     * </code>
     *
     */
    public static String getUri(String uri, Object... uriVariables) {

        if (null == uriVariables){
            return uri;
        }
        for (Object variable : uriVariables){
            if (null == variable){
                continue;
            }
            uri = uri.replaceFirst("\\{\\w+\\}", variable.toString());
        }
        return uri;
    }



    /**
     * 匹配Map.key
     * @param uri 匹配的uri，带代表式
     * @param uriVariables 匹配表达式的值
     * @return 匹配完的url
     * <code>
     *      Map&lt;String, Object&gt;  uriVariable = new HashMap&lt;String, Object&gt;();
     *      uriVariable.put(&quot;a&quot;, &quot;no1&quot;);
     *      uriVariable.put(&quot;a1&quot;, &quot;no2&quot;);
     *      uriVariable.put(&quot;bb&quot;, &quot;no3&quot;);
     *      System.out.println(getUri(&quot;http://egan.in/{a}/ba/{a1}?{bb}={a1}&quot;, uriVariable));
     *      结果 http://egan.in/no1/ba/no2?no3=no2
     * </code>
     */
    public static String getUri(String uri, Map<String, Object> uriVariables) {

        if (null == uriVariables){
            return uri;
        }
        for (Map.Entry<String, Object> entry : uriVariables.entrySet()) {
            Object uriVariable = entry.getValue();
            if (null == uriVariable){
                continue;
            }

            uri = uri.replace("{" + entry.getKey() + "}", uriVariable.toString());
        }
        return uri;
    }



    /**
     * Map转化为对应得参数字符串
     * @param pe 参数
     * @return 参数字符串
     */
    public static String getMapToParameters(Map pe){
        StringBuilder builder = new StringBuilder();
        for (Map.Entry entry : (Set<Map.Entry>)pe.entrySet()) {
            Object o = entry.getValue();

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
                    builder.append(entry.getKey()).append("=").append(URLEncoder.encode(valueStr, "utf-8")).append("&");

                    continue;
                }
                builder.append(entry.getKey()).append("=").append(URLEncoder.encode( entry.getValue().toString(), "utf-8")).append("&");
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
     * 解析应答字符串，生成应答要素
     *
     * @param str 需要解析的字符串
     * @return 解析的结果map
     */
    public static JSONObject getParametersToMap (String str) {

        JSONObject map = new JSONObject();
        int len = str.length();
        StringBuilder temp = new StringBuilder();
        char curChar;
        String key = null;
        boolean isKey = true;
        boolean isOpen = false;//值里有嵌套
        char openName = 0;
        if (len > 0) {
            for (int i = 0; i < len; i++) {// 遍历整个带解析的字符串
                curChar = str.charAt(i);// 取当前字符
                if (isKey) {// 如果当前生成的是key

                    if (curChar == '=') {// 如果读取到=分隔符
                        key = temp.toString();
                        temp.setLength(0);
                        isKey = false;
                    } else {
                        temp.append(curChar);
                    }
                } else {// 如果当前生成的是value
                    if (isOpen) {
                        if (curChar == openName) {
                            isOpen = false;
                        }

                    } else {//如果没开启嵌套
                        if (curChar == '{') {//如果碰到，就开启嵌套
                            isOpen = true;
                            openName = '}';
                        }
                        if (curChar == '[') {
                            isOpen = true;
                            openName = ']';
                        }
                    }
                    if (curChar == '&' && !isOpen) {// 如果读取到&分割符,同时这个分割符不是值域，这时将map里添加
                        putKeyValueToMap(temp, isKey, key, map);
                        temp.setLength(0);
                        isKey = true;
                    } else {
                        temp.append(curChar);
                    }
                }

            }
            putKeyValueToMap(temp, isKey, key, map);
        }
        return map;
    }

    private static void putKeyValueToMap (StringBuilder temp, boolean isKey, String key, Map<String, Object> map) {
        if (isKey) {
            key = temp.toString();
            if (key.length() == 0) {
                throw new PayErrorException(new PayException("QString format illegal", "内容格式有误"));
            }
            map.put(key, "");
        } else {
            if (key.length() == 0) {
                throw new PayErrorException(new PayException("QString format illegal", "内容格式有误"));
            }
            map.put(key, temp.toString());
        }
    }


}
