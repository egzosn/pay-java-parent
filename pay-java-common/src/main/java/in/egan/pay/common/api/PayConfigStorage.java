package in.egan.pay.common.api;

import in.egan.pay.common.bean.MsgType;

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

    /**
     * 支付类型 自定义
     * 这里暂定 aliPay 支付宝， wxPay微信支付
     * @return
     */
    public String getPayType();

    /**
     *  @see #getMsgType
     *  @see MsgType
     * @return "text" 或者 "xml"
     * @see MsgType#xml
     * @see MsgType#text
     */
    public MsgType getMsgType();


}
