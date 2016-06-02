package in.egan.pay.wx.utils;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author  egan
 * @email egzosn@gmail.com
 * @date 2016-6-2 19:45:06
 */
public class XML {
    /**
     * 解析xml并转化为Map<String,String>值
     * @param content
     * @return
     */
    public static Map<String, Object> toMap(String content){

        if (null == content || "".equals(content)) {
            return null;
        }

        Map m = new HashMap();

        SAXBuilder builder = new SAXBuilder();
        Document doc = null;
        InputStream in = null;
        try {
             in = new ByteArrayInputStream(content.getBytes("UTF-8"));
            doc = builder.build(in);
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
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            // 关闭流
            try {
                if (null != in ) in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return m;

    }


    /**
     * 获取子结点的xml
     *
     * @param children
     * @return String
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

}
