package com.egzosn.pay.wx.v3.api;

import com.egzosn.pay.common.api.PayService;
import com.egzosn.pay.common.bean.NoticeParams;
import com.egzosn.pay.common.bean.TransactionType;
import com.egzosn.pay.common.http.HttpStringEntity;

/**
 * 微信支付接口
 * @author Egan
 * email egan@egzosn.com
 * date 2023/9/11
 */
public interface WxPayServiceInf extends PayService<WxPayConfigStorage> {
    /**
     * 辅助api
     *
     * @return 辅助api
     */
    WxPayAssistService getAssistService();

    /**
     * 设置api服务器地址
     *
     * @param apiServerUrl api服务器地址
     * @return 自身
     */
    WxPayService setApiServerUrl(String apiServerUrl);

    String getApiServerUrl();

    /**
     * 根据交易类型获取url
     *
     * @param transactionType 交易类型
     * @return 请求url
     */
    @Override
    String getReqUrl(TransactionType transactionType);

    /**
     * 验签，使用微信平台证书.
     *
     * @param noticeParams 通知参数
     * @return the boolean
     */
    @Override
    boolean verify(NoticeParams noticeParams);

    /**
     * 签名
     *
     * @param content           需要签名的内容 不包含key
     * @param characterEncoding 字符编码
     * @return 签名结果
     */
    @Override
    String createSign(String content, String characterEncoding);

    /**
     * http 实体 钩子
     * @param entity 实体
     * @return 返回处理后的实体
     */
    default HttpStringEntity hookHttpEntity(HttpStringEntity entity){
        return entity;
    }
}
