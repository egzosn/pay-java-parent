package in.egan.pay.common.api;

import in.egan.pay.common.bean.PayOrder;
import in.egan.pay.common.exception.PayErrorException;

import java.io.InputStream;
import java.util.Map;

/**
 * 支付通知
 *
 * @author egan
 * @email egzosn@gmail.com
 * @date 2016-5-18 14:09:01
 */
public interface PayService {

    public String getHttpsVerifyUrl();

    public void setPayConfigStorage(PayConfigStorage payConfigStorage);

    public PayConfigStorage getPayConfigStorage();

    public boolean verify(Map<String, String> params);

    public boolean checkSignature(Map<String, String> params);

    public boolean getSignVeryfy(Map<String, String> Params, String sign);

    public String verifyUrl(String notify_id) throws PayErrorException;

    public <T, E> T execute(RequestExecutor<T, E> executor, String uri, E data) throws PayErrorException;

    /**
     * 返回创建的订单信息
     *
     * @param order 支付订单
     * @return
     * @see in.egan.pay.common.bean.PayOrder
     */
    public Object orderInfo(PayOrder order);

    /**
     * 创建签名
     *
     * @param content           需要签名的内容
     * @param characterEncoding 字符编码
     * @return
     */
    public String createSign(String content, String characterEncoding);

    /**
     * 将请求参数或者请求流转化为 Map
     *
     * @param parameterMap 请求参数
     * @param is           请求流
     * @return
     */
    public Map<String, String> getParameter2Map(Map<String, String[]> parameterMap, InputStream is);
}
