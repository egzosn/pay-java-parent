

## 富友支付简单例子

#### 支付配置

```java


    
        FuiouPayConfigStorage fuiouPayConfigStorage = new FuiouPayConfigStorage();
        fuiouPayConfigStorage.setMchntCd("合作者id");
        fuiouPayConfigStorage.setKeyPublic("支付密钥");
        fuiouPayConfigStorage.setKeyPrivate("支付密钥");
        fuiouPayConfigStorage.setNotifyUrl("异步回调地址");
        fuiouPayConfigStorage.setReturnUrl("同步回调地址");
        fuiouPayConfigStorage.setSignType("MD5");
        fuiouPayConfigStorage.setInputCharset("utf-8");
        //是否为测试账号，沙箱环境
        fuiouPayConfigStorage.setTest(true);
        
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
    PayService service = new FuiouPayService(fuiouPayConfigStorage);
    //设置网络请求配置根据需求进行设置
     //service.setRequestTemplateConfigStorage(httpConfigStorage)

```

#### 创建支付订单信息

```java

         //支付订单基础信息
           PayOrder payOrder = new PayOrder("订单title", "摘要",  new BigDecimal(0.01) , UUID.randomUUID().toString().replace("-", "").substring(2));

``` 


#### 网页支付

```java

       /*----------- 网页支付-------------------*/
//        payOrder.setTransactionType(FuiouTransactionType.B2B);
        payOrder.setTransactionType(FuiouTransactionType.B2C);
        //获取支付所需的信息
        Map directOrderInfo = service.orderInfo(payOrder);
        //获取表单提交对应的字符串，将其序列化到页面即可,
        String directHtml = service.buildRequest(directOrderInfo, MethodType.POST);
        /*-----------/网页支付-------------------*/

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

