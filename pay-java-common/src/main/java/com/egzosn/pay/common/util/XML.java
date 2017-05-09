package com.egzosn.pay.common.util;


import com.alibaba.fastjson.JSONObject;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * XML工具
 * @author  egan
 * <pre>
 * email egzosn@gmail.com
 * date 2016-6-2 19:45:06
 * </pre>
 */
public class XML {
    /**
     * 解析xml并转化为Json值
     * @param content json字符串
     * @return Json值
     */
    public static JSONObject toJSONObject(String content){

        if (null == content || "".equals(content)) {
            return null;
        }

        try (InputStream in = new ByteArrayInputStream(content.getBytes("UTF-8"))){
            return (JSONObject) inputStream2Map(in, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

    /**
     * 解析xml并转化为json值
     * @param in 输入流
     * @return Json值
     */
    public static JSONObject toJSONObject(InputStream in) {

        if (null == in) {
            return null;
        }

        try {
            return (JSONObject)inputStream2Map(in, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;


    }

    /**
     *
     * @param in xml输入流
     * @param m 参数集
     * @return 整理完成的参数集
     * @throws IOException xml io转化异常
     */
    public static Map inputStream2Map(InputStream in, Map m) throws IOException {
        if (null == m){
            m = new JSONObject();
        }
        SAXBuilder builder = new SAXBuilder();
        try {
            Document doc = builder.build(in);
            Element root = doc.getRootElement();
            List list = root.getChildren();
            Iterator it = list.iterator();
            while (it.hasNext()) {
                Element e = (Element) it.next();
                String k = e.getName();
                String v = "";
                List children = e.getChildren();
                if (children.isEmpty()) {
                    v = e.getTextNormalize();
                } else {
                    v = getChildrenText(children);
                }
                m.put(k, v);
            }
        } catch (JDOMException e) {
            e.printStackTrace();
        }
        return m;
    }



    /**
     * 获取子结点的xml
     *
     * @param children 集合
     * @return String 子结点的xml
     */
    public static String getChildrenText(List children) {
        StringBuffer sb = new StringBuffer();
        if (!children.isEmpty()) {
            Iterator it = children.iterator();
            while (it.hasNext()) {
                Element e = (Element) it.next();
                String name = e.getName();
                String value = e.getTextNormalize();
                List list = e.getChildren();
                sb.append("<" + name + ">");
                if (!list.isEmpty()) {
                    sb.append(getChildrenText(list));
                }
                sb.append(value);
                sb.append("</" + name + ">");
            }
        }

        return sb.toString();
    }

    /**
     * 将请求参数转换为xml格式的string
     * @param parameters 请求参数
     * @return xml
     */
    public static String getMap2Xml(Map<String, Object> parameters) {
        StringBuffer sb = new StringBuffer();
        sb.append("<xml>");
        for (String key : parameters.keySet()){
            if ("attach".equalsIgnoreCase(key) || "body".equalsIgnoreCase(key) || "attach".equalsIgnoreCase(key) || "sign".equalsIgnoreCase(key)) {
                sb.append("<" + key + ">" + "<![CDATA[" + parameters.get(key) + "]]></" + key + ">");
            } else {
                sb.append("<" + key + ">" +  parameters.get(key) + "</" + key + ">");
            }

        }

        sb.append("</xml>");
        return sb.toString();
    }

}
