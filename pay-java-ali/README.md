

## 支付宝支付简单例子

#### 支付配置

```java


        AliPayConfigStorage aliPayConfigStorage = new AliPayConfigStorage();
        aliPayConfigStorage.setPid("合作者id");
        aliPayConfigStorage.setAppId("应用id");
        aliPayConfigStorage.setAliPublicKey("支付宝公钥");
        aliPayConfigStorage.setKeyPrivate("应用私钥");
        aliPayConfigStorage.setNotifyUrl("异步回调地址");
        aliPayConfigStorage.setReturnUrl("同步回调地址");
        aliPayConfigStorage.setSignType("签名方式");
        aliPayConfigStorage.setSeller("收款账号");
        aliPayConfigStorage.setInputCharset("utf-8");
        //是否为测试账号，沙箱环境
        aliPayConfigStorage.setTest(true);
        
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
        httpConfigStorage.setHttpProxyUsername("user");
        //代理密码
        httpConfigStorage.setHttpProxyPassword("password");
        /* /网路代理配置 根据需求进行设置**/
    
         /* 网络请求ssl证书 根据需求进行设置**/
        //设置ssl证书路径
        httpConfigStorage.setKeystorePath("证书绝对路径");
        //设置ssl证书对应的密码
        httpConfigStorage.setStorePassword("证书对应的密码");
        /* /网络请求ssl证书**/
        
```


#### 创建支付服务


```java
    //支付服务
     PayService service = new AliPayService(aliPayConfigStorage);

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
        payOrder.setTransactionType(AliTransactionType.SWEEPPAY);
        //获取扫码付的二维码
        BufferedImage image = service.genQrPay(payOrder);
        /*-----------/扫码付-------------------*/

``` 

#### APP支付

```java

        /*-----------APP-------------------*/
        payOrder.setTransactionType(AliTransactionType.APP);
        //获取APP支付所需的信息组，直接给app端就可使用
        Map appOrderInfo = service.orderInfo(payOrder);
        /*-----------/APP-------------------*/

``` 

#### 即时到帐 WAP 网页支付

```java

        /*-----------即时到帐 WAP 网页支付-------------------*/
//        payOrder.setTransactionType(AliTransactionType.WAP); //WAP支付

        payOrder.setTransactionType(AliTransactionType.DIRECT); // 即时到帐 PC网页支付
        //获取支付所需的信息
        Map directOrderInfo = service.orderInfo(payOrder);
        //获取表单提交对应的字符串，将其序列化到页面即可,
        String directHtml = service.buildRequest(directOrderInfo, MethodType.POST);
        /*-----------/即时到帐 WAP 网页支付-------------------*/

``` 

#### 条码付 声波付

```java

        /*-----------条码付 声波付-------------------*/

//        payOrder.setTransactionType(AliTransactionType.WAVE_CODE); //声波付
        payOrder.setTransactionType(AliTransactionType.BAR_CODE);//条码付

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
        
      Map result = service..query("支付宝单号", "我方系统单号");

```


#### 交易关闭接口
  ```java

          Map result = service..query("支付宝单号", "我方系统单号");

```


#### 申请退款接口
  ```java

          Map result = service.refund("支付宝单号", "我方系统单号", "退款金额", "订单总金额");

```


#### 查询退款
  ```java

          Map result = service.refundquery("支付宝单号", "我方系统单号");

```

#### 下载对账单
  ```java

          Map result = service.downloadbill("账单时间：日账单格式为yyyy-MM-dd，月账单格式为yyyy-MM", "账单类型");

```

