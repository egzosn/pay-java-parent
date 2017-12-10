

## 微信支付简单例子

#### 支付配置

```java

        UnionPayConfigStorage configStorage = new UnionPayConfigStorage();
        configStorage.setMchId("合作者id（商户号）");
        configStorage.setAppid("应用id");
        configStorage.setKeyPublic("密钥");
        configStorage.setKeyPrivate("密钥");
        configStorage.setNotifyUrl("异步回调地址");
        configStorage.setReturnUrl("同步回调地址");
        configStorage.setSignType("签名方式");
        configStorage.setInputCharset("utf-8");
        //是否为测试账号，沙箱环境 此处暂未实现
        configStorage.setTest(true);
        
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
        PayService service =  new WxPayService(configStorage);
        
        //设置网络请求配置根据需求进行设置
        //service.setRequestTemplateConfigStorage(httpConfigStorage)

```

#### 创建支付订单信息

```java

        //支付订单基础信息
           PayOrder payOrder = new PayOrder("订单title", "摘要",  new BigDecimal(0.01) , UUID.randomUUID().toString().replace("-", ""));
  
``` 
