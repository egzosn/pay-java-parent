package in.egan.pay.common.api;

import in.egan.pay.common.exception.PayErrorException;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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
        public boolean verify(Map<String, String> params);
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
    public  String orderInfo(String subject, String body, String price,String tradeNo);

    /**
     * 创建签名
     * @param content 需要签名的内容
     * @param characterEncoding 字符编码
     * @return
     */
    public String createSign(String content, String characterEncoding);
}
