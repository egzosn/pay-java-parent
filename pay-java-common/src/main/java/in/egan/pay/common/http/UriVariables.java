package in.egan.pay.common.http;

import com.alibaba.fastjson.JSON;
import org.apache.http.Consts;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * URL表达式处理器
 *
 * @author: egan
 * @email egzosn@gmail.com
 * @date 2017/3/5 10:07
 */
public class UriVariables {

    /**
     * 依次匹配
     * @param uri
     * @param uriVariables
     * @return
     * <code>
     *     System.out.println(getUri("http://egan.in/{a}/ba/{a1}?{bb}={a1}", "no1", "no2", "no3", "no4"));
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
     * @param uri
     * @param uriVariables
     * @return
     * <code>
     *      Map<String, Object> uriVariable = new HashMap<>();
     *      uriVariable.put("a", "no1");
     *      uriVariable.put("a1", "no2");
     *      uriVariable.put("bb", "no3");
     *      System.out.println(getUri("http://egan.in/{a}/ba/{a1}?{bb}={a1}", uriVariable));
     *      结果 http://egan.in/no1/ba/no2?no3=no2
     * </code>
     */
    public static String getUri(String uri, Map<String, Object> uriVariables) {

        if (null == uriVariables){
            return uri;
        }
        for (String key : uriVariables.keySet()){
            Object uriVariable = uriVariables.get(key);
            if (null == uriVariable){
                continue;
            }

            uri = uri.replace("{" + key + "}", uriVariable.toString());
        }
        return uri;
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




}
