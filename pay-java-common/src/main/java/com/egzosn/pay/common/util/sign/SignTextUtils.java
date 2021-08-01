package com.egzosn.pay.common.util.sign;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

import org.apache.http.message.BasicNameValuePair;

import com.egzosn.pay.common.util.str.StringUtils;

/**
 * 签名文本构建工具
 *
 * @author Egan
 * email egzosn@gmail.com
 * date 2021/8/1
 */
public final class SignTextUtils {

    private SignTextUtils() {
    }


    /**
     * 把数组所有元素排序，并按照“参数=参数值”的模式用“@param separator”字符拼接成字符串
     *
     * @param parameters 参数
     * @return 去掉空值与签名参数后的新签名，拼接后字符串
     */
    public static String parameterText(Map parameters) {
        return parameterText(parameters, "&");

    }

    /**
     * 把数组所有元素排序，并按照“参数=参数值”的模式用“@param separator”字符拼接成字符串
     *
     * @param parameters 参数
     * @param separator  分隔符
     * @return 去掉空值与签名参数后的新签名，拼接后字符串
     */
    public static String parameterText(Map parameters, String separator) {
        return parameterText(parameters, separator, "signature", "sign", "key", "sign_type");
    }

    /**
     * 把数组所有元素排序，并按照“参数=参数值”的模式用“@param separator”字符拼接成字符串
     *
     * @param parameters 参数
     * @param separator  分隔符
     * @param ignoreKey  需要忽略添加的key
     * @return 去掉空值与签名参数后的新签名，拼接后字符串
     */
    public static String parameterText(Map<String, Object> parameters, String separator, String... ignoreKey) {
        return parameterText(parameters, separator, true, ignoreKey);
    }

    /**
     * 把数组所有元素排序，并按照“参数=参数值”的模式用“@param separator”字符拼接成字符串
     *
     * @param parameters      参数
     * @param separator       分隔符
     * @param ignoreNullValue 需要忽略NULL值
     * @param ignoreKey       需要忽略添加的key
     * @return 去掉空值与签名参数后的新签名，拼接后字符串
     */
    public static String parameterText(Map<String, Object> parameters, String separator, boolean ignoreNullValue, String... ignoreKey) {
        if (parameters == null) {
            return "";
        }

        if (null != ignoreKey) {
            Arrays.sort(ignoreKey);
        }
        StringBuffer sb = new StringBuffer();
        // TODO 2016/11/11 10:14 author: egan 已经排序好处理
        if (parameters instanceof SortedMap) {
            for (Map.Entry<String, Object> entry : parameters.entrySet()) {
                Object v = entry.getValue();
                if (null == v) {
                    continue;
                }
                String valStr = v.toString().trim();
                if ("".equals(valStr) || (null != ignoreKey && Arrays.binarySearch(ignoreKey, entry.getKey()) >= 0)) {
                    continue;
                }
                sb.append(entry.getKey()).append("=").append(valStr).append(separator);
            }
            if (sb.length() > 0 && !"".equals(separator)) {
                sb.deleteCharAt(sb.length() - 1);
            }
            return sb.toString();

        }

        return sortMapParameterText(parameters, separator, ignoreNullValue, ignoreKey);

    }


    private static String sortMapParameterText(Map<String, Object> parameters, String separator, boolean ignoreNullValue, String... ignoreKey) {
        StringBuffer sb = new StringBuffer();
        // TODO 2016/11/11 10:14 author: egan 未排序须处理
        List<String> keys = new ArrayList<String>(parameters.keySet());
        //排序
        Collections.sort(keys);
        for (String k : keys) {
            String valueStr = "";
            Object o = parameters.get(k);
            if (ignoreNullValue && null == o) {
                continue;
            }
            if (o instanceof String[]) {
                String[] values = (String[]) o;

                for (int i = 0; i < values.length; i++) {
                    String value = values[i].trim();
                    if ("".equals(value)) {
                        continue;
                    }
                    valueStr += (i == values.length - 1) ? value : value + ",";
                }
            }
            else {
                valueStr = o.toString();
            }
            if (StringUtils.isBlank(valueStr) || (null != ignoreKey && Arrays.binarySearch(ignoreKey, k) >= 0)) {
                continue;
            }
            sb.append(k).append("=").append(valueStr).append(separator);
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    /**
     * 将参数集合(事前做好排序)按分割符号拼凑字符串并加密为MD5
     * example: mchnt_cd+"|"  +order_id+"|"+order_amt+"|"+order_pay_type+"|"+page_notify_url+"|"+back_notify_url+"|"+order_valid_time+"|"+iss_ins_cd+"|"+goods_name+"|"+"+goods_display_url+"|"+rem+"|"+ver+"|"+mchnt_key
     *
     * @param parameters 参数集合
     * @param separator  分隔符
     * @return 参数排序好的值
     */
    public static String parameters2Md5Str(Object parameters, String separator) {
        StringBuffer sb = new StringBuffer();

        if (parameters instanceof LinkedHashMap) {
            Set<String> keys = (Set<String>) ((LinkedHashMap) parameters).keySet();
            for (String key : keys) {
                String val = ((LinkedHashMap) parameters).get(key).toString();
                sb.append(val).append(separator);

            }
        }
        else if (parameters instanceof List) {
            for (BasicNameValuePair bnv : ((List<BasicNameValuePair>) parameters)) {
                sb.append(bnv.getValue()).append(separator);
            }
        }

        return StringUtils.isBlank(sb.toString()) ? "" : sb.deleteCharAt(sb.length() - 1).toString();
    }


    /**
     * 获取随机字符串
     *
     * @return 随机字符串
     */
    public static String randomStr() {
        return StringUtils.randomStr();
    }


}
