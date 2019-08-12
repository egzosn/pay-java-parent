

## 友店支付简单例子

#### 支付配置

```java


    
    WxYouDianPayConfigStorage wxPayConfigStorage = new WxYouDianPayConfigStorage();
    wxPayConfigStorage.setKeyPrivate("友店所提供的加密串");
    wxPayConfigStorage.setKeyPublic("线下支付异步通知加签密钥");
//            wxPayConfigStorage.setNotifyUrl(account.getNotifyUrl());
//            wxPayConfigStorage.setReturnUrl(account.getReturnUrl());
    wxPayConfigStorage.setSeller("友店账号");
    wxPayConfigStorage.setSignType("签名方式");
    wxPayConfigStorage.setInputCharset("utf-8");
    //是否为测试账号，沙箱环境 此处暂未实现
    wxPayConfigStorage.setTest(true);
        
```

#### 网络请求配置

```java

        HttpConfigStorage httpConfigStorage = new HttpConfigStorage();
      /* /网络请求连接池**/
        //最大连接数
        httpConfigStorage.setMaxTotal(20);
        //默认的每个路由的最大连接数
        httpConfigStorage.setDefaultMaxPerRoute(10);
```



#### 创建支付服务

```java

    //支付服务
     PayService service =   new WxYouDianPayService(wxPayConfigStorage);
    //设置网络请求配置根据需求进行设置
     //service.setRequestTemplateConfigStorage(httpConfigStorage)
```

#### 创建支付订单信息

```java

         //支付订单基础信息
             PayOrder payOrder = new PayOrder("订单title", "摘要",  new BigDecimal(0.01) , UUID.randomUUID().toString().replace("-", ""));

``` 


#### 扫码付

```java

        /*-----------扫码付-------------------*/
        payOrder.setTransactionType(YoudianTransactionType.NATIVE);
        //获取扫码付的二维码
//        String image = service.getQrPay(payOrder);
        BufferedImage image = service.genQrPay(payOrder);
        /*-----------/扫码付-------------------*/
        
``` 


#### 回调处理

```java

        /*-----------回调处理-------------------*/
           //HttpServletRequest request;
         Map<String, Object> params = service.getParameter2Map(request.getParameterMap(), request.getInputStream());
        if (service.verify(params)){
            System.out.println("支付成功");
            return;
        }
        System.out.println("支付失败");


        /*-----------回调处理-------------------*/

```

