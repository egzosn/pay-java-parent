package com.egzosn.pay.baidu.api;

import org.junit.jupiter.api.Test;

/**
 * Created by hocgin on 2019/11/24.
 * email: hocgin@gmail.com
 *
 * @author hocgin
 */
public class BaiduPayServiceTest {
    
    @Test
    public void orderInfo() {
        BaiduPayConfigStorage configStorage = new BaiduPayConfigStorage();
        configStorage.setAppid("APP ID");
        configStorage.setAppKey("APP KEY");
        configStorage.setDealId("DEAL ID");
        configStorage.setKeyPublic("KEY PUBLIC");
    
        BaiduPayService payService = new BaiduPayService(configStorage);
        // payService.refund()
    }
}
