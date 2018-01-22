package com.egzosn.pay.common.api;

import com.egzosn.pay.common.bean.AuthPageType;

/**
 * 高级支付接口
 * @author Actinia
 * @email hayesfu@qq.com
 * @date 2018/1/18
 * 
 */

public interface AdvancedPayService extends PayService{
    /**
     * 获取授权页面
     * @param payeeId 收款id
     * @param authPageType 授权类型
     * @return 返回请求结果
     */
    String getAuthorizationPage(String payeeId,AuthPageType authPageType);

}
