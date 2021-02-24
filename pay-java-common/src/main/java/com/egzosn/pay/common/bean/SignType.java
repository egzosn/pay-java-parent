package com.egzosn.pay.common.bean;


import java.util.*;

/**
 * 签名类型
 *
 * @author egan
 * <pre>
 * email egzosn@gmail.com
 * date 2019/12/08 13:30
 * </pre>
 */
public interface SignType {


    /**
     * 获取签名类型名称
     * @return 类型名称
     */
    String getName();

    /**
     * 签名
     *
     * @param parameters 需要进行排序签名的参数
     * @param key 密钥
     * @param characterEncoding 编码格式
     * @return 签名值
     */
    String sign(Map parameters, String key, String characterEncoding);
    /**
     * 签名
     * @param parameters 需要进行排序签名的参数
     * @param key 密钥
     * @param separator 分隔符  默认 &amp;
     * @param characterEncoding 编码格式
     * @return 签名值
     */
    String sign(Map parameters, String key, String separator, String characterEncoding);

    /**
     * 签名
     *
     * @param content           需要签名的内容
     * @param key               密钥
     * @param characterEncoding 字符编码
     * @return 签名值
     */
    String createSign(String content, String key, String characterEncoding);

    /**
     * 签名字符串
     *
     * @param params              需要签名的字符串
     * @param sign              签名结果
     * @param key               密钥
     * @param characterEncoding 编码格式
     * @return 签名结果
     */
    boolean verify(Map params, String sign, String key, String characterEncoding);


    /**
     * 签名字符串
     *
     * @param text              需要签名的字符串
     * @param sign              签名结果
     * @param key               密钥
     * @param characterEncoding 编码格式
     * @return 签名结果
     */
    boolean verify(String text, String sign, String key, String characterEncoding);



}
