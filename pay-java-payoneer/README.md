

## payoneer简单例子

#### 支付配置

```java

        PayoneerConfigStorage configStorage = new PayoneerConfigStorage();
        configStorage.setProgramId("商户id");
        configStorage.setMsgType(MsgType.json);
        configStorage.setInputCharset("utf-8");
        configStorage.setUserName("PayoneerPay 用户名");
        configStorage.setApiPassword("PayoneerPay API password");
        configStorage.setTest(true);
        //是否为测试账号，沙箱环境
        configStorage.setTest(true);


        
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
     PayoneerPayService service = new PayoneerPayService(configStorage);
    //设置网络请求配置根据需求进行设置
     //service.setRequestTemplateConfigStorage(httpConfigStorage)

```

#### 用户授权

```java
    //授权的地址
    String url = service.getAuthorizationPage("用户标识,一般为用户id或者账单id");
    
```


#### 创建支付订单信息

```java

        //支付订单基础信息
         PayOrder order = new PayOrder("Order_payment:", "Order payment", price, UUID.randomUUID().toString().replace("-", ""), PayoneerTransactionType.CHARGE);
  
``` 

#### 发起扣款

```java


    //币种
        order.setCurType(CurType.USD);
        //设置授权码，条码等
        order.setAuthCode( userId);
        //支付结果
        Map<String, Object> params = service.microPay(order);

        if (10700 == (Integer) params.get(PayoneerPayService.CODE)){
            System.out.println("未授权");
        }else  if (0 == (Integer) params.get(PayoneerPayService.CODE)){
            System.out.println("收款成功");
        }
```

#### 授权回调处理

```java

        /*-----------回调处理-------------------*/
           //HttpServletRequest request;
         Map<String, Object> params = service.getParameter2Map(request.getParameterMap(), request.getInputStream());
         //这里自行处理，


        /*-----------回调处理-------------------*/

```




#### 支付订单查询

```java
        
      Map result = service..query(null, "我方系统单号");

```


#### 取消交易接口(交易关闭接口)
  ```java

          Map result = service.query(null, "我方系统单号");

```

#### 取消交易接口(退款)
  ```java
          //过时方法
         //Map result = service.refund(null, "我方系统单号", null, null);
         //支付宝单号与我方系统单号二选一
         RefundOrder order = new RefundOrder(null, "我方系统单号", null, null);
         Map result = service.refund(order);

```


#### 转账
  ```java
        TransferOrder order = new TransferOrder();
        order.setOutNo("商户转账订单号");
        order.setCurType(CurType.USD);
        order.setPayeeAccount("收款方账户,用户授权所使用的userId");
        order.setAmount(new BigDecimal(10));
        order.setRemark("转账备注, 非必填");
        Map result = service.transfer(order);

```
#### 转账查询
  ```java
       Map result = service.transferQuery("商户转账订单号", null);
```
