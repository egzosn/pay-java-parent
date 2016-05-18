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
    //授权令牌
    public String getToken();
    //服务端回调Url
    public String getNotify_url();
    //签名方式
    public String getSign_type();


}
