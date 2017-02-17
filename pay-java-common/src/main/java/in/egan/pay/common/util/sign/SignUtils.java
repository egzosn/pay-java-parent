/*
 * Copyright 2002-2017 the original huodull or egan.
 *
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


package in.egan.pay.common.util.sign;


import in.egan.pay.common.util.str.StringUtils;

import java.util.*;

/**
 * 签名 工具
 *
 * @author: egan
 * @email egzosn@gmail.com
 * @date 2016/11/9 17:45
 */
public enum SignUtils {

    MD5 {
        /**
         *
         * @param content           需要签名的内容
         * @param key               密钥
         * @param characterEncoding 字符编码
         * @return
         */
        @Override
        public String createSign(String content, String key, String characterEncoding) {

            return in.egan.pay.common.util.sign.encrypt.MD5.sign(content, key, characterEncoding);
        }

        /**
         * 签名字符串
         * @param text 需要签名的字符串
         * @param sign 签名结果
         * @param key 密钥
         * @param characterEncoding 编码格式
         * @return 签名结果
         */
        public boolean verify(String text, String sign, String key, String characterEncoding) {
            return in.egan.pay.common.util.sign.encrypt.MD5.verify(text, sign, key, characterEncoding);
        }
    },

    RSA {
        @Override
        public String createSign(String content, String key, String characterEncoding) {
            return in.egan.pay.common.util.sign.encrypt.RSA.sign(content, key, characterEncoding);
        }

        @Override
        public boolean verify(String text, String sign, String publicKey, String characterEncoding) {
            return in.egan.pay.common.util.sign.encrypt.RSA.verify(text, sign, publicKey, characterEncoding);
        }
    };

    /**
     *
     * 把数组所有元素排序，并按照“参数=参数值”的模式用“@param separator”字符拼接成字符串
     * @param parameters 参数
     * @return 去掉空值与签名参数后的新签名，拼接后字符串
     */
    public static String parameterText(Map parameters) {
        return parameterText(parameters, "&");

    }
    /**
     *
     * 把数组所有元素排序，并按照“参数=参数值”的模式用“@param separator”字符拼接成字符串
     * @param parameters 参数
     * @param separator 分隔符
     * @return 去掉空值与签名参数后的新签名，拼接后字符串
     */
    public static String parameterText(Map parameters, String separator) {
        if(parameters == null){
            return "";
        }
        StringBuffer sb = new StringBuffer();

        // TODO 2016/11/11 10:14 author: egan 已经排序好处理
        if (parameters instanceof SortedMap) {
            for (String k : ((Set<String>) parameters.keySet())) {
                Object v = parameters.get(k);
                if (null == v || "".equals(v.toString().trim()) || "sign".equals(k) || "key".equals(k) || "appId".equals(k) || "sign_type".equalsIgnoreCase(k)) {
                    continue;
                }
                sb.append(k ).append("=").append( v.toString().trim()).append(separator);
            }
            if (sb.length() > 0 && !"".equals(separator)) {
                sb.deleteCharAt(sb.length() - 1);
            }
            return sb.toString();

        }


        // TODO 2016/11/11 10:14 author: egan 未排序须处理
        List<String> keys = new ArrayList<String>(parameters.keySet());
        //排序
        Collections.sort(keys);
        for (String k : keys) {
            String valueStr = "";
            Object o = parameters.get(k);
            if (o instanceof String[]) {
                String[] values = (String[]) o;
                if (null == values){continue;}
                for (int i = 0; i < values.length; i++) {
                    String value = values[i].trim();
                    if ("".equals(value)){ continue;}
                    valueStr = (i == values.length - 1) ? valueStr + value
                            : valueStr + value + ",";
                }
            } else if (o != null) {
                valueStr = o.toString();
            }
            if (null == valueStr || "".equals(valueStr.toString().trim()) || "sign".equals(k) || "key".equals(k) || "appId".equals(k) || "sign_type".equalsIgnoreCase(k)) {
                continue;
            }
                sb.append(k ).append("=").append( valueStr).append(separator);
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    /**
     * 将参数集合(事前做好排序)按分割符号拼凑字符串并加密为MD5
     * example: mchnt_cd+"|"  +order_id+"|"+order_amt+"|"+order_pay_type+"|"+page_notify_url+"|"+back_notify_url+"|"+order_valid_time+"|"+iss_ins_cd+"|"+goods_name+"|"+"+goods_display_url+"|"+rem+"|"+ver+"|"+mchnt_key
     * @param parameters
     * @param separator
     * @return
     */
    public static String  parameters2MD5Str(Map parameters, String separator){
        StringBuffer sb = new StringBuffer();
        Set<String >  keys = (Set<String>) parameters.keySet();

        if (parameters instanceof LinkedHashMap) {
                for(String key : keys){
                    String val = parameters.get(key).toString();
                    if(StringUtils.isNotBlank(val)){
                        sb.append(val).append(separator);
                    }
                }
        }

        return StringUtils.isBlank(sb.toString())?"":sb.deleteCharAt(sb.length() - 1).toString();
    }


    /**
     * 获取随机字符串
     * @return
     */
    public static String randomStr(){
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 签名
     *
     * @param parameters 需要进行排序签名的参数
     * @param key 密钥
     * @param characterEncoding 编码格式
     * @return
     */
    public  String sign(Map parameters, String key, String characterEncoding) {

        return createSign(parameterText(parameters, "&"), key, characterEncoding);
    }
    /**
     * 签名
     * @param parameters 需要进行排序签名的参数
     * @param key 密钥
     * @param separator 分隔符  默认 &
     * @param characterEncoding 编码格式
     * @return
     */
    public  String sign(Map parameters, String key, String separator, String characterEncoding) {

        return createSign(parameterText(parameters, separator), key, characterEncoding);

    }

    /**
     * 签名
     *
     * @param content           需要签名的内容
     * @param key               密钥
     * @param characterEncoding 字符编码
     * @return
     */
    public abstract String createSign(String content, String key, String characterEncoding);

    /**
     * 签名字符串
     *
     * @param params              需要签名的字符串
     * @param sign              签名结果
     * @param key               密钥
     * @param characterEncoding 编码格式
     * @return 签名结果
     */
    public  boolean verify(Map params, String sign, String key, String characterEncoding){
        //判断是否一样
//        return StringUtils.equals(sign(params, key, characterEncoding), sign);
        return this.verify(parameterText(params), sign, key, characterEncoding);
    }


    /**
     * 签名字符串
     *
     * @param text              需要签名的字符串
     * @param sign              签名结果
     * @param key               密钥
     * @param characterEncoding 编码格式
     * @return 签名结果
     */
    public abstract boolean verify(String text, String sign, String key, String characterEncoding);



    //签名错误代码
    public static final int SIGN_ERROR = 91;
}
