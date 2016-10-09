
#pay-java-parent

##整合支付模块（微信支付，支付宝）


###特性

    1.支付请求调用支持HTTP和异步
    2.控制层统一异常处理
    3.LogBack日志记录
    4.简单快速完成支付模块的开发
    5.支持多种支付类型多支付账户扩展（目前已支持微信支付与支付宝支付）
###使用  
这里不多说直接上代码

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
     * @see ApyAccount 对应表结构详情--》 /db/apy_account.sql
     */
    public void init(ApyAccount apyAccount) {


        this.service = getPayService(apyAccount);
        this.storage = service.getPayConfigStorage();

        buildRouter(apyAccount.getPayId());
    }


    /**
     * 根据不同的账户类型 初始化支付配置
     * 一个账户类型可支持多个账户
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

支付响应PayResponse的获取

```java


public class ApyAccountService {


    @Inject
    private ApyAccountDao dao;

    @Inject
    private AutowireCapableBeanFactory spring;

    private final static Map<Integer, PayResponse> payResponses = new HashMap<Integer, PayResponse>();


    /**
     *  获取支付响应
     * @param id 账户id
     * @return
     */
    public PayResponse getPayResponse(Integer id) {

        PayResponse payResponse = payResponses.get(id);
        if (mpResponse == null) {
            ApyAccount apyAccount = dao.get(id);
            if (apyAccount == null) {
                throwError(-1, "无法查询");
            }
            payResponse = new PayResponse();
            spring.autowireBean(payResponse);
            payResponse.init(apyAccount);
            payResponses.put(id, payResponse);
            // 查询
        }
        return payResponse;
    }



}

```


2.  根据账户id与业务id，组拼订单信息（支付宝、微信支付订单）获取支付信息所需的数据

```java
  //获取对应的支付账户操作工具（可根据账户id）
  PayResponse payResponse =  service.getPayResponse(payId);;
  //这里之所以用Object，因为微信需返回Map， 支付吧String。
  Object orderInfo = payResponse.getService().orderInfo("订单title", "摘要", new BigDecimal(0.01), "tradeNo");
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
    @RequestMapping(value = "payBack{payId}.json")
    public String payBack(HttpServletRequest request, @PathVariable Integer payId){
        //根据账户id，获取对应的支付账户操作工具
        PayResponse payResponse = service.getPayResponse(payId);
        PayConfigStorage storage = payResponse.getStorage();
        Map<String, String> params = request2Params(request, storage.getPayType());
        if (null == params){
            return "fail";
        }


        if (payResponse.getService().verify(params)){

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
    public Map<String, String> request2Params(HttpServletRequest request, Short payType){

         if (1 == storage.getPayType()){
            //根据请求文件流里获取
            Map<String, String> data = inputStream2Map(request);
            if (null == data || data.size() == 0){
                return null;
            }
            return data;
         }

        Map<String, String[]> requestParams = request.getParameterMap();

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

##交流
很希望更多志同道合友友一起扩展新的的支付接口。

微信：zzs1134842433

E-Mail：egzosn@gmail.com

QQ：1134842433

