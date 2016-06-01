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
    public String getNotify_url();
    //签名方式
    public String getSign_type();
    // 字符编码格式 目前支持 gbk 或 utf-8
    public String getInput_charset();
    //获取公钥
    public String getSecretKey();

    public String getHttp_proxy_host();

    public int getHttp_proxy_port();

    public String getHttp_proxy_username();

    public String getHttp_proxy_password();
    public String getRsa_public();
    public String getRsa_private();
    public String getLogPath();
    public Boolean isShowLog();


}
