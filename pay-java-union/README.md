

## 银联简单例子

#### 支付配置

```java

    UnionPayConfigStorage unionPayConfigStorage = new UnionPayConfigStorage();
    unionPayConfigStorage.setMerId("700000000000001");
    //是否为证书签名
    unionPayConfigStorage.setCertSign(true);
    
     //中级证书路径
     unionPayConfigStorage.setAcpMiddleCert("证书文件流，证书字符串信息或证书绝对地址");
     //根证书路径
     unionPayConfigStorage.setAcpRootCert("证书文件流，证书字符串信息或证书绝对地址");
     // 私钥证书路径
     unionPayConfigStorage.setKeyPrivateCert("证书文件流，证书字符串信息或证书绝对地址");
     //私钥证书对应的密码
     unionPayConfigStorage.setKeyPrivateCertPwd("私钥证书对应的密码");
     //设置证书对应的存储方式，这里默认为文件地址
     httpConfigStorage.setCertStoreType(CertStoreType.PATH);
     
     
     
     
    unionPayConfigStorage.setNotifyUrl("http://www.pay.egzosn.com/payBack.json");
      // 无需同步回调可不填  app填这个就可以
    unionPayConfigStorage.setReturnUrl("http://www.pay.egzosn.com/payBack.json");
    unionPayConfigStorage.setSignType(SignUtils.RSA2.name());
    //单一支付可不填
    unionPayConfigStorage.setPayType("unionPay");
    unionPayConfigStorage.setInputCharset("UTF-8");
    //是否为测试账号，沙箱环境
    unionPayConfigStorage.setTest(true);

```


#### 网络请求配置

```java

        HttpConfigStorage httpConfigStorage = new HttpConfigStorage();
        /* 网路代理配置 根据需求进行设置**/
        //http代理地址
        httpConfigStorage.setHttpProxyHost("192.168.1.69");
        //代理端口
        httpConfigStorage.setHttpProxyPort(3308);
        //代理用户名
        httpConfigStorage.setAuthUsername("user");
        //代理密码
        httpConfigStorage.setAuthPassword("password");
        /* /网路代理配置 根据需求进行设置**/

      /* /网络请求连接池**/
        //最大连接数
        httpConfigStorage.setMaxTotal(20);
        //默认的每个路由的最大连接数
        httpConfigStorage.setDefaultMaxPerRoute(10);
```


#### 创建支付服务


```java
    //支付服务
     PayService service = new UnionPayService(unionPayConfigStorage);

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
        payOrder.setTransactionType(UnionTransactionType.APPLY_QR_CODE);
        //获取扫码付的二维码
//        String image = service.getQrPay(payOrder);
        BufferedImage image = service.genQrPay(payOrder);
        /*-----------/扫码付-------------------*/

``` 


#### APP支付， 苹果付

```java

        /*-----------APP-------------------*/
        //App支付
        order.setTransactionType(UnionTransactionType.APP);
        
        //APPLE支付 苹果付
//        order.setTransactionType(UnionTransactionType.APPLE);

        //获取APP支付所需的信息组，直接给app端就可使用
        Map appOrderInfo = service.orderInfo(payOrder);
        /*-----------/APP-------------------*/

``` 
       
       
       
#### 即时到帐 WAP 网页支付 企业网银支付（B2B支付）

```java

        /*-----------即时到帐 WAP 网页支付-------------------*/
//        payOrder.setTransactionType(UnionTransactionType.WAP); //WAP支付

//        payOrder.setTransactionType(UnionTransactionType.B2B); //企业网银支付（B2B支付）

        payOrder.setTransactionType(UnionTransactionType.WEB); // PC网页支付
        //获取支付所需的信息
        Map directOrderInfo = service.orderInfo(payOrder);
        
        //获取表单提交对应的字符串，将其序列化到页面即可,
        String directHtml = service.buildRequest(directOrderInfo, MethodType.POST);
        /*-----------/即时到帐 WAP 网页支付-------------------*/

``` 


#### 条码付 声波付

```java

        /*-----------条码付 -------------------*/

        payOrder.setTransactionType(UnionTransactionType.CONSUME);//条码付

        payOrder.setAuthCode("条码信息或者声波信息");
        // 支付结果
        Map params = service.microPay(payOrder);
        /*-----------/条码付 声波付-------------------*/

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



#### 支付订单查询

```java
        
      Map result = service..query(null, "我方系统单号");

```


#### 申请退款接口
  ```java
    
         RefundOrder order = new RefundOrder(null, "原交易查询流水号", "退款金额", "订单总金额");
         order.setRefundNo("退款单号")
         Map result = service.refund(order);

```
