package in.egan.pay.common.http;

import java.util.HashMap;
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


}
