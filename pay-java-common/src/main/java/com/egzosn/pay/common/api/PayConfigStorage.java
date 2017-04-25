package com.egzosn.pay.common.api;

import com.egzosn.pay.common.bean.MsgType;

import java.util.concurrent.locks.Lock;

/**
 * 支付客户端配置存储
 * @author  egan
 * <pre>
 *     email egzosn@gmail.com
 *     date 2016-5-18 14:09:01
 * </pre>
 */
 public interface PayConfigStorage {

    /*
     *  应用id
     *  @return 应用id
     */
     String getAppid();
    /**
     * 合作商唯一标识
     *  @see #getPid()  代替者
     *  @return 合作商唯一标识
     */
    @Deprecated
     String getPartner();
    /**
     * 合作商唯一标识
     * @see #getPartner()  代替者
     *  @return 合作商唯一标识
     */
     String getPid();

    /**
     * 获取收款账号
     *  @return 收款账号
     */
     String getSeller();

    /**
     * 授权令牌
     *  @return 授权令牌
     */
     String getToken();


    /**
     * 服务端异步回调Url
     *  @return 异步回调Url
     */
     String getNotifyUrl();
    /**
     * 服务端同步回调Url
     *  @return 同步回调Url
     */
     String getReturnUrl();
    /**
    * 签名方式
     *  @return 签名方式
     */
     String getSignType();

    /**
     *  字符编码格式
     * @return 字符编码
     */
     String getInputCharset();
    /**
     *  获取密钥 与 #getKeyPrivate 类似
     *  @return 获取密钥
     */
     String getSecretKey();

    /**
     * 公钥
     * @return 公钥
     */
     String getKeyPublic();

    /**
     * 私钥
     * @return 私钥
     */
     String getKeyPrivate();

    /**
     * 支付类型 自定义
     * 这里暂定 aliPay 支付宝， wxPay微信支付
     * @return 支付类型
     */
     String getPayType();

    /**
     * 消息类型
     *  @see #getMsgType
     *  @see MsgType
     * @return "text" 或者 "xml"，json
     */
     MsgType getMsgType();


    /**
     * 获取访问令牌
     * @return  访问令牌
     */
    String getAccessToken();

    /**
     * 访问令牌是否过期
     * @return true过期
     */
    boolean isAccessTokenExpired();
    /**
     * 获取access token锁
     * @return access token锁
     */
    Lock getAccessTokenLock();

    /**
     * 强制将access token过期掉
     */
    void expireAccessToken();
    /**
     * 强制将access token过期掉
     *  @return 过期时间
     */
    long getExpiresTime();

    /**
     * 应该是线程安全的
     * @param accessToken 新的accessToken值
     * @param expiresInSeconds 过期时间，以秒为单位 多少秒
     */
    void updateAccessToken(String accessToken, int expiresInSeconds);

    /**
     * 应该是线程安全的
     * @param accessToken 新的accessToken值
     * @param expiresTime 过期时间，时间戳
     */
    void updateAccessToken(String accessToken, long expiresTime);

    /**
     * 是否为测试环境， true测试环境
     * @return true测试环境
     */
    boolean isTest();
}
