package in.egan.pay.common.api;

/**
 * 支付客户端配置存储
 * @author  egan
 * @email egzosn@gmail.com
 * @date 2016-5-18 14:09:01
 */
public interface PayConfigStorage {

    //客户端号
    public String getAppid();
    //客户端号
    public String getPartner();
    //获取收款账号
    public String getSeller();
    //授权令牌
    public String getToken();
    //服务端回调Url
    public String getNotifyUrl();
    //签名方式
    public String getSignType();
    // 字符编码格式 目前支持 gbk 或 utf-8
    public String getInputCharset();
    //获取公钥
    public String getSecretKey();

    public String getHttpProxyHost();

    public int getHttpProxyPort();

    public String getHttpProxyUsername();

    public String getHttpProxyPassword();
    public String getKeyPublic();
    public String getKeyPrivate();
    public String getLogPath();
    public Boolean isShowLog();
    public Short getPayType();


}
