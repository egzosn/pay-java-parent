

## 微信支付简单例子

#### 支付配置

```java

       UnionPayConfigStorage unionPayConfigStorage = new UnionPayConfigStorage();
       unionPayConfigStorage.setMerId("合作者id");
       unionPayConfigStorage.setKeyPublic("支付密钥");
       unionPayConfigStorage.setKeyPrivate("支付密钥");
       unionPayConfigStorage.setNotifyUrl("异步回调地址");
       unionPayConfigStorage.setReturnUrl("同步回调地址");
       unionPayConfigStorage.setSignType("MD5");
       unionPayConfigStorage.setInputCharset("utf-8");
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
  
```

#### 创建支付订单信息

```java
      PayOrder payOrder = new PayOrder("订单title", "摘要",  new BigDecimal(0.01) , new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
``` 

#### 主扫申请二维码交易

```java
       payOrder.setTransactionType(UnionTransactionType.APPLY_QR_CODE);
       BufferedImage image = service.genQrPay(payOrder);
``` 

#### 消费(被扫场景)待定

```java
       payOrder.setTransactionType(UnionTransactionType.CONSUME);
       params =   service.microPay(payOrder);
``` 
#### 消费撤销

```java
       params =   service.unionRefundOrConsumeUndo("原交易查询流水号", "订单号", new BigDecimal("退款金额" ),UnionTransactionType.CONSUME_UNDO);
  
``` 
#### 交易状态查询交易：只有同步应答
  
  ```java
       payOrder.setTransactionType(UnionTransactionType.QUERY);
       params =   service.query(null,"商户单号");
    
``` 


#### 退货交易：后台资金类交易，有同步应答和后台通知应答
  
  ```java
       payOrder.setTransactionType(UnionTransactionType.REFUND);
       params =   service.refund("原交易查询流水号", "订单号", null,new BigDecimal("退款金额" ));
    
``` 


#### 文件传输类接口：后台获取对账文件交易，只有同步应答
 
 ```java
       String fileConten =   service.downloadbill(new Date(),"格式为MMDD");
``` 

       