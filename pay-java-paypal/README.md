

## paypal简单例子

#### 支付配置

```java

        PayPalConfigStorage storage = new PayPalConfigStorage();
        storage.setClientID("商户id");
        storage.setClientSecret("商户密钥");
        storage.setTest(true);
        //发起付款后的页面转跳地址
        storage.setReturnUrl("http://127.0.0.1:8088/pay/success");
        //取消按钮转跳地址,这里用异步通知地址的兼容的做法
        storage.setNotifyUrl("http://127.0.0.1:8088/pay/cancel");
           
        
```



#### 创建支付服务


```java
    //支付服务
     PayService service = new PayPalPayService(configStorage);

```


#### 创建支付订单信息

```java

        //支付订单基础信息
         PayOrder order = new PayOrder("Order_payment:", "Order payment", price, UUID.randomUUID().toString().replace("-", ""), PayPalTransactionType.sale);
  
``` 

#### 网页支付

```java


        //币种
        order.setCurType(CurType.USD);
        Map orderInfo = service.orderInfo(order);
        service.buildRequest(orderInfo, MethodType.POST);
```

#### 授权回调处理

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

#### 申请退款接口
  ```java
        RefundOrder order = new RefundOrder();
        order.setCurType(CurType.USD);
        order.setDescription(" description ");
        order.setTradeNo("paypal 平台的单号");
        order.setRefundAmount(new BigDecimal(0.01));
          Map result =  service.refund(order);

```
