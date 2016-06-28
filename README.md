
#pay-java-parent

##整合支付模块（微信支付，支付宝）

#### 一.  快速入门
1. 支付整合配置
```java
public class PayResponse {
    @Resource
    private AutowireCapableBeanFactory spring;

    private PayConfigStorage storage;

    private PayService service;

    private PayMessageRouter router;

    public PayResponse() {

    }

    /**
     * 初始化支付配置
     * @param apyAccount 账户信息
     */
    public void init(ApyAccount apyAccount) {


        this.service = getPayService(apyAccount);
        this.storage = service.getPayConfigStorage();

        buildRouter(apyAccount.getPayId());
    }


    /**
     * 根据不同的账户类型 初始化支付配置
     * @param apyAccount 账户信息
     */
    public PayService getPayService(ApyAccount apyAccount){

        switch (apyAccount.getPayType()){
            case 0:
                AliPayConfigStorage aliPayConfigStorage = new AliPayConfigStorage();
                aliPayConfigStorage.setPartner(apyAccount.getPartner());
                aliPayConfigStorage.setAli_public_key(apyAccount.getPublicKey());
                aliPayConfigStorage.setKeyPrivate(apyAccount.getPrivateKey());
                aliPayConfigStorage.setInputCharset(apyAccount.getInputCharset());
                aliPayConfigStorage.setNotifyUrl(apyAccount.getNotifyUrl());
                aliPayConfigStorage.setSignType(apyAccount.getSignType());
                aliPayConfigStorage.setSeller(apyAccount.getSeller());
                aliPayConfigStorage.setPayType(apyAccount.getPayType());
                return new AliPayService(aliPayConfigStorage);
            case 1:
                WxPayConfigStorage wxPayConfigStorage = new WxPayConfigStorage();
                wxPayConfigStorage.setMchId(apyAccount.getPartner());
                wxPayConfigStorage.setAppSecret(apyAccount.getPublicKey());
                wxPayConfigStorage.setAppid(apyAccount.getAppid());
                wxPayConfigStorage.setKeyPrivate(apyAccount.getPrivateKey());
                wxPayConfigStorage.setInputCharset(apyAccount.getInputCharset());
                wxPayConfigStorage.setNotifyUrl(apyAccount.getNotifyUrl());
                wxPayConfigStorage.setSignType(apyAccount.getSignType());
                wxPayConfigStorage.setPayType(apyAccount.getPayType());
                return  new WxPayService(wxPayConfigStorage);
            default:

        }
        return null;
    }


    /**
     * 配置路由
     * @param payId 指定账户id，用户多微信支付多支付宝支付
     */
    private void buildRouter(Integer payId) {
        router = new PayMessageRouter(this.service);
        router
                .rule()
                .async(false)
                .msgType(PayConsts.MSG_TEXT)
                .event(PayConsts.MSG_ALIPAY)
                .handler(autowire(new AliPayMessageHandler(payId)))
                .end()
                .rule()
                .async(false)
                .msgType(PayConsts.MSG_XML)
                .event(PayConsts.MSG_WXPAY)
                .handler(autowire(new WxPayMessageHandler(payId)))
                .end()
        ;
    }

    
    private PayMessageHandler autowire(PayMessageHandler handler) {
        spring.autowireBean(handler);
        return handler;
    }

    public PayConfigStorage getStorage() {
        return storage;
    }
    
    public PayService getService() {
        return service;
    }

    public PayMessageRouter getRouter() {
        return router;
    }
}

```

2.  根据账户id与业务id，组拼订单信息（支付宝、微信支付订单）获取支付信息所需的数据

```java
  //获取对应的支付账户操作工具（可根据账户id）
  PayResponse payResponse = null;
  //这里之所以用Object，因为微信需返回Map， 支付吧String。 摘要部分：@_%s_@中的'%s'用户替代账户id，支付回调得知账户信息
  Object orderInfo = payResponse.getService().orderInfo("订单title", String.format("@_%s_@摘要", body.getPayId()), new BigDecimal(0.01), "tradeNo");
  System.out.println(orderInfo);
  
```

3. 支付回调
```java
     
     /**
     * 微信或者支付宝回调地址
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "payBack.json")
    public String payBack(HttpServletRequest request){
               Map<String, String> params = request2Params(request);
        if (null == params){
            return "fail";
        }
        Integer payId = null;
        if ( "0".equals(params.remove("payType"))){
            String subject  = params.get("body");
            payId = Integer.parseInt(subject.substring(subject.indexOf("@_") + 2, subject.indexOf("_@")));
        }else {
            String attach  = params.get("attach");
            payId = Integer.parseInt(attach.substring(attach.indexOf("@_") + 2, attach.indexOf("_@")));
        }

        PayResponse payResponse = service.getPayResponse(payId);
        if (payResponse.getService().verify(params)){
            PayConfigStorage storage = payResponse.getStorage();
            String msgType = null;
            if (0 == storage.getPayType()){
                msgType = PayConsts.MSG_TEXT;
            }else {
                msgType = PayConsts.MSG_XML;
            }
            PayMessage message = new PayMessage(params, storage.getPayType(), msgType);
            PayOutMessage outMessage = payResponse.getRouter().route(message);
            return outMessage.toMessage();
        }

        return "fail";
    }


    /**
     * 根据请求获取参数Map
     * @param request
     * @return
     */
    public Map<String, String> request2Params(HttpServletRequest request){

        Map<String, String[]> requestParams = request.getParameterMap();
        //微信在请求参数里面获取不到对应的参数信息
        if (0 == requestParams.size()){
            //根据请求文件流里获取
            Map<String, String> data = inputStream2Map(request);
            if (null == data || data.size() == 0){
                return null;
            }
            //设置支付类型
            data.put("payType", "1");
            return data;
        }
        Map<String,String> params = new HashMap<String,String>();
        for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
            //valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
            params.put(name, valueStr);
        }
        //  设置支付类型
        params.put("payType", "0");
        return params;
    }

    /**
     * 从请求中获取xml文件流，转化为map
     * @param request
     * @return
     */
        public Map<String, String> inputStream2Map(HttpServletRequest request) {
            Map<String, String> map = null;//将微信发出的Xml转Map
            try {
                map = WxpayCore.toMap(request.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return map;
        }
        
```

