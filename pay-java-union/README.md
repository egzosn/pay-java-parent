

## 银联支付简单例子

#### 支付配置

```java

       UnionPayConfigStorage unionPayConfigStorage = new UnionPayConfigStorage();
       unionPayConfigStorage.setMerId("商户id");
       unionPayConfigStorage.setKeyPublic("公钥，验签证书链格式： 中级证书路径;根证书路径");
       unionPayConfigStorage.setKeyPrivate("私钥, 私钥证书格式： 私钥证书路径;私钥证书对应的密码");
       unionPayConfigStorage.setNotifyUrl("异步回调地址");
       unionPayConfigStorage.setReturnUrl("同步回调地址");
       unionPayConfigStorage.setSignType("RSA2");
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

      UnionPayService service = new UnionPayService(unionPayConfigStorage);
      
      unionPayConfigStorage.setCertSign(true);//是否为证书签名
  
```

#### 创建支付订单信息

```java
      PayOrder payOrder = new PayOrder("订单title", "摘要",  new BigDecimal(0.01) , new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()));
``` 
#### 网页支付
```java
//      手机网页支付（WAP支付）
        payOrder.setTransactionType(UnionTransactionType.WAP);
//      网关支付
//      payOrder.setTransactionType(UnionTransactionType.WEB);
//      企业网银支付（B2B支付）
//      payOrder.setTransactionType(UnionTransactionType.B2B);
        //获取支付所需的信息
        Map directOrderInfo = service.orderInfo(payOrder);
        //获取表单提交对应的字符串，将其序列化到页面即可,
        String directHtml = service.buildRequest(directOrderInfo, MethodType.POST);
```

#### 主扫申请二维码交易

```java
       payOrder.setTransactionType(UnionTransactionType.APPLY_QR_CODE);
       BufferedImage image = service.genQrPay(payOrder);
``` 

#### 消费(被扫场景)待定

```java
       payOrder.setTransactionType(UnionTransactionType.CONSUME);
       payOrder.setAuthCode("C2B码(条码号),1-20位数字");
       Map<String, Object> params =   service.microPay(payOrder);
``` 
#### 消费撤销

```java
       Map<String, Object> params =   service.unionRefundOrConsumeUndo("原交易查询流水号", "订单号", new BigDecimal("退款金额" ),UnionTransactionType.CONSUME_UNDO);
  
``` 
#### 交易状态查询交易：只有同步应答
  
  ```java
       payOrder.setTransactionType(UnionTransactionType.QUERY);
       Map<String, Object> params =   service.query(null,"商户单号");
    
``` 


#### 退货交易：后台资金类交易，有同步应答和后台通知应答
  
  ```java
       payOrder.setTransactionType(UnionTransactionType.REFUND);
       Map<String, Object> params =   service.refund("原交易查询流水号", "订单号", null,new BigDecimal("退款金额" ));
    
``` 


#### 文件传输类接口：后台获取对账文件交易，只有同步应答
 
 ```java
       String fileConten =   service.downloadbill(null,"文件类型，一般商户填写00即可"); 
``` 

       