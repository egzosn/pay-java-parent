package in.egan.pay.common.api;

import in.egan.pay.common.exception.PayErrorException;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Map;

/**
 * 支付通知
 * @author  egan
 * @email egzosn@gmail.com
 * @date 2016-5-18 14:09:01
 */
public interface PayService {

        public String getHttpsVerifyUrl();
        public void setPayConfigStorage(PayConfigStorage payConfigStorage) ;
        public PayConfigStorage getPayConfigStorage() ;
        public boolean verify(Map<String, String> params);
        public boolean checkSignature(Map<String, String> params);
        public boolean getSignVeryfy(Map<String, String> Params, String sign);
        public String verifyUrl(String notify_id) throws PayErrorException;
        public <T, E> T execute(RequestExecutor<T, E> executor, String uri, E data) throws PayErrorException;
    /**
     * 入口
     * @param subject 商品名称
     * @param body 商品详情
     * @param price 价格
     * @param tradeNo 商户单号
     * @return
     */
    public Object orderInfo(String subject, String body, BigDecimal price,String tradeNo);

    /**
     * 创建签名
     * @param content 需要签名的内容
     * @param characterEncoding 字符编码
     * @return
     */
    public String createSign(String content, String characterEncoding);

    /**
     * 将请求参数或者请求流转化为 Map
     * @param parameterMap 请求参数
     * @param is 请求流
     * @return
     */
    public Map<String, String> getParameter2Map(Map<String, String[]> parameterMap, InputStream is);
}
