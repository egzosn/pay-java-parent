
### 快速入门
##### 1.支付整合配置
```java


/**
 * 支付类型
 * @author egan
 * @email egzosn@gmail.com
 * @date 2016/11/20 0:30
 */
public enum PayType implements BasePayType {

    aliPay{
        /**
         *  @see com.egzosn.pay.ali.api.AliPayService  17年更新的版本,旧版本请自行切换{@link com.egzosn.pay.ali.before.api.AliPayService }
         * @param apyAccount
         * @return
         */
        @Override
        public PayService getPayService(ApyAccount apyAccount) {
            AliPayConfigStorage aliPayConfigStorage = new AliPayConfigStorage();
            aliPayConfigStorage.setPid(apyAccount.getPartner());
            aliPayConfigStorage.setAppid(apyAccount.getAppid());
            aliPayConfigStorage.setKeyPublic(apyAccount.getPublicKey());
            aliPayConfigStorage.setKeyPrivate(apyAccount.getPrivateKey());
            aliPayConfigStorage.setNotifyUrl(apyAccount.getNotifyUrl());
            aliPayConfigStorage.setReturnUrl(apyAccount.getReturnUrl());
            aliPayConfigStorage.setSignType(apyAccount.getSignType());
            aliPayConfigStorage.setSeller(apyAccount.getSeller());
            aliPayConfigStorage.setPayType(apyAccount.getPayType().toString());
            aliPayConfigStorage.setMsgType(apyAccount.getMsgType());
            aliPayConfigStorage.setInputCharset(apyAccount.getInputCharset());
            return new AliPayService(aliPayConfigStorage);
        }

        @Override
        public TransactionType getTransactionType(String transactionType) {
              // com.egzosn.pay.ali.bean.AliTransactionType 17年更新的版本,旧版本请自行切换{@link com.egzosn.pay.ali.before.bean.AliTransactionType}
            return AliTransactionType.valueOf(transactionType);
        }


    },wxPay {
        @Override
        public PayService getPayService(ApyAccount apyAccount) {
            WxPayConfigStorage wxPayConfigStorage = new WxPayConfigStorage();
            wxPayConfigStorage.setMchId(apyAccount.getPartner());
            wxPayConfigStorage.setAppSecret(apyAccount.getPublicKey());
            wxPayConfigStorage.setKeyPublic(apyAccount.getPublicKey());
            wxPayConfigStorage.setAppid(apyAccount.getAppid());
            wxPayConfigStorage.setKeyPrivate(apyAccount.getPrivateKey());
            wxPayConfigStorage.setNotifyUrl(apyAccount.getNotifyUrl());
            wxPayConfigStorage.setSignType(apyAccount.getSignType());
            wxPayConfigStorage.setPayType(apyAccount.getPayType().toString());
            wxPayConfigStorage.setMsgType(apyAccount.getMsgType());
            wxPayConfigStorage.setInputCharset(apyAccount.getInputCharset());
            return  new WxPayService(wxPayConfigStorage);
        }

        /**
         * 根据支付类型获取交易类型
         * @param transactionType 类型值
         * @see com.egzosn.pay.wx.bean.WxTransactionType
         * @return
         */
        @Override
        public TransactionType getTransactionType(String transactionType) {

            return WxTransactionType.valueOf(transactionType);
        }
    },youdianPay {
        @Override
        public PayService getPayService(ApyAccount apyAccount) {
            // TODO 2017/1/23 14:12 author: egan  集群的话,友店可能会有bug。暂未测试集群环境
            WxYouDianPayConfigStorage wxPayConfigStorage = new WxYouDianPayConfigStorage();
            wxPayConfigStorage.setKeyPrivate(apyAccount.getPrivateKey());
            wxPayConfigStorage.setKeyPublic(apyAccount.getPublicKey());
//            wxPayConfigStorage.setNotifyUrl(apyAccount.getNotifyUrl());
//            wxPayConfigStorage.setReturnUrl(apyAccount.getReturnUrl());
            wxPayConfigStorage.setSignType(apyAccount.getSignType());
            wxPayConfigStorage.setPayType(apyAccount.getPayType().toString());
            wxPayConfigStorage.setMsgType(apyAccount.getMsgType());
            wxPayConfigStorage.setSeller(apyAccount.getSeller());
            wxPayConfigStorage.setInputCharset(apyAccount.getInputCharset());
            return  new WxYouDianPayService(wxPayConfigStorage);
        }

        /**
         * 根据支付类型获取交易类型
         * @param transactionType 类型值
         * @see com.egzosn.pay.wx.youdian.bean.YoudianTransactionType
         * @return
         */
        @Override
        public TransactionType getTransactionType(String transactionType) {

            return YoudianTransactionType.valueOf(transactionType);
        }
    };

    public abstract PayService getPayService(ApyAccount apyAccount);

}

/**
 * 支付响应对象
 * @author: egan
 * @email egzosn@gmail.com
 * @date 2016/11/18 0:34
 */
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
     * @see ApyAccount 对应表结构详情--》 /pay-java-demo/resources/apy_account.sql
     */
    public void init(ApyAccount apyAccount) {
        //根据不同的账户类型 初始化支付配置
        this.service = apyAccount.getPayType().getPayService(apyAccount);
        this.storage = service.getPayConfigStorage();
        //这里设置代理配置
//        service.setRequestTemplateConfigStorage(getHttpConfigStorage());
        buildRouter(apyAccount.getPayId());
    }

    /**
     * 获取http配置，如果配置为null则为默认配置，无代理,无证书的请求方式。
     *  此处非必需
     * @param apyAccount 账户信息
     * @return 请求配置
     */
    public HttpConfigStorage getHttpConfigStorage(ApyAccount apyAccount){
        HttpConfigStorage httpConfigStorage = new HttpConfigStorage();
     /*
        //http代理地址
        httpConfigStorage.setHttpProxyHost("192.168.1.69");
        //代理端口
        httpConfigStorage.setHttpProxyPort(3308);
        //代理用户名
        httpConfigStorage.setAuthUsername("user");
        //代理密码
        httpConfigStorage.setAuthPassword("password");

        */
        //设置ssl证书路径
        httpConfigStorage.setKeystore(apyAccount.getKeystorePath());
        //设置ssl证书对应的密码
        httpConfigStorage.setStorePassword(apyAccount.getStorePassword());
        return httpConfigStorage;
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
                .msgType(MsgType.text.name()) //消息类型
                .payType(PayType.aliPay.name()) //支付账户事件类型
                .transactionType(AliTransactionType.UNAWARE.name())//交易类型，有关回调的可在这处理
                .interceptor(new AliPayMessageInterceptor(payId)) //拦截器
                .handler(autowire(new AliPayMessageHandler(payId))) //处理器
                .end()
                .rule()
                .async(false)
                .msgType(MsgType.xml.name())
                .payType(PayType.wxPay.name())
                .handler(autowire(new WxPayMessageHandler(payId)))
                .end()
                .rule()
                .async(false)
                .msgType(MsgType.json.name())
                .payType(PayType.youdianPay.name())
                .handler(autowire(new YouDianPayMessageHandler(payId)))
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

##### 2.支付处理器与拦截器简单实现

```java
    /**
     * 微信支付回调处理器
     * Created by ZaoSheng on 2016/6/1.
     */
    public class WxPayMessageHandler extends BasePayMessageHandler {
        public WxPayMessageHandler(Integer payId) {
            super(payId);
        }
        @Override
        public PayOutMessage handle(PayMessage payMessage, Map<String, Object> context, PayService payService) throws PayErrorException {
            //交易状态
            if ("SUCCESS".equals(payMessage.getPayMessage().get("result_code"))){
                /////这里进行成功的处理

                return  payService.getPayOutMessage("SUCCESS", "OK");
            }

            return  payService.getPayOutMessage("FAIL", "失败");
        }
    }

    /**
     * 支付宝回调信息拦截器
     * @author: egan
     * @email egzosn@gmail.com
     * @date 2017/1/18 19:28
     */
    public class AliPayMessageInterceptor implements PayMessageInterceptor {
        /**
         * 拦截支付消息
         *
         * @param payMessage     支付回调消息
         * @param context        上下文，如果handler或interceptor之间有信息要传递，可以用这个
         * @param payService
         * @return true代表OK，false代表不OK并直接中断对应的支付处理器
         * @see PayMessageHandler 支付处理器
         */
        @Override
        public boolean intercept(PayMessage payMessage, Map<String, Object> context, PayService payService) throws PayErrorException {

            //这里进行拦截器处理，自行实现
            return true;
        }
    }

```


##### 3.支付响应PayResponse的获取


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
        if (payResponse  == null) {
            ApyAccount apyAccount = dao.get(id);
            if (apyAccount == null) {
               throw new IllegalArgumentException ("无法查询");
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


##### 4.根据账户id与业务id，组拼订单信息（支付宝、微信支付订单）获取支付信息所需的数据

```java

    /**
     * 跳到支付页面
     * 针对实时支付,即时付款
     *
     * @param payId 账户id
     * @param transactionType 交易类型， 这个针对于每一个 支付类型的对应的几种交易方式
     * @param bankType 针对刷卡支付，卡的类型，类型值
     * @return
     */
    
    @RequestMapping(value = "toPay.html", produces = "text/html;charset=UTF-8")
    public String toPay( Integer payId, String transactionType, String bankType, BigDecimal price) {
        //获取对应的支付账户操作工具（可根据账户id）
        PayResponse payResponse =  service.getPayResponse(payId);

        PayOrder order = new PayOrder("订单title", "摘要", null == price ? new BigDecimal(0.01) : price, UUID.randomUUID().toString().replace("-", ""), PayType.valueOf(payResponse.getStorage().getPayType()).getTransactionType(transactionType));

        //此处只有刷卡支付(银行卡支付)时需要
        if (StringUtils.isNotEmpty(bankType)){
            order.setBankType(bankType);
        }
        Map orderInfo = payResponse.getService().orderInfo(order);
        return  payResponse.getService().buildRequest(orderInfo, MethodType.POST);
    }


    /**
     * 获取二维码图像
     * 二维码支付
     * @return
     */
    @RequestMapping(value = "toQrPay.jpg", produces = "image/jpeg;charset=UTF-8")
    public byte[] toWxQrPay(Integer payId, String transactionType, BigDecimal price) throws IOException {
        //获取对应的支付账户操作工具（可根据账户id）
        PayResponse payResponse =  service.getPayResponse(payId);
        //获取订单信息
        Map<String, Object> orderInfo = payResponse.getService().orderInfo(new PayOrder("订单title", "摘要", null == price ? new BigDecimal(0.01) : price, UUID.randomUUID().toString().replace("-", ""), PayType.valueOf(payResponse.getStorage().getPayType()).getTransactionType(transactionType)));

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(payResponse.getService().genQrPay(orderInfo), "JPEG", baos);
        return baos.toByteArray();
    }


    /**
     *
     *  获取支付预订单信息
     * @param payId 支付账户id
     * @param transactionType 交易类型
     * @return
     */
    @RequestMapping("getOrderInfo")
    public Map<String, Object> getOrderInfo(Integer payId, String transactionType, BigDecimal price){
        //获取对应的支付账户操作工具（可根据账户id）
        PayResponse payResponse =  service.getPayResponse(payId);
        Map<String, Object> data = new HashMap<>();
        data.put("code", 0);
        PayOrder order = new PayOrder("订单title", "摘要", null == price ? new BigDecimal(0.01) : price, UUID.randomUUID().toString().replace("-", ""), PayType.valueOf(payResponse.getStorage().getPayType()).getTransactionType(transactionType));
        data.put("orderInfo",  payResponse.getService().orderInfo(order));
        return data;
    }


 /**
     * 查询
     * @param order 订单的请求体
     * @return
     */
    @RequestMapping("query")
    public Map<String, Object> query(QueryOrder order) {
        PayResponse payResponse = service.getPayResponse(order.getPayId());
        return payResponse.getService().query(order.getTradeNo(), order.getOutTradeNo());
    }
    /**
     * 交易关闭接口
     * @param order 订单的请求体
     * @return
     */
    @RequestMapping("close")
    public Map<String, Object> close(QueryOrder order) {
        PayResponse payResponse = service.getPayResponse(order.getPayId());
        return payResponse.getService().close(order.getTradeNo(), order.getOutTradeNo());
    }

    /**
     * 申请退款接口
     * @param order 订单的请求体
     * @return
     */
    @RequestMapping("refund")
    public Map<String, Object> refund(QueryOrder order) {
        PayResponse payResponse = service.getPayResponse(order.getPayId());


        return payResponse.getService().refund(order.getTradeNo(), order.getOutTradeNo(), order.getRefundAmount(), order.getTotalAmount());
    }

    /**
     * 查询退款
     * @param order 订单的请求体
     * @return
     */
    @RequestMapping("refundquery")
    public Map<String, Object> refundquery(QueryOrder order) {
        PayResponse payResponse = service.getPayResponse(order.getPayId());
        return payResponse.getService().refundquery(order.getTradeNo(), order.getOutTradeNo());
    }

    /**
     * 下载对账单
     * @param order 订单的请求体
     * @return
     */
    @RequestMapping("downloadbill")
    public Object downloadbill(QueryOrder order) {
        PayResponse payResponse = service.getPayResponse(order.getPayId());

        return payResponse.getService().downloadbill(order.getBillDate(), order.getBillType());
    }


    /**
     * 通用查询接口，根据 TransactionType 类型进行实现,此接口不包括退款
     * @param order 订单的请求体
     *
     * @return
     */
    @RequestMapping("secondaryInterface")
    public Map<String, Object> secondaryInterface(QueryOrder order) {
        PayResponse payResponse = service.getPayResponse(order.getPayId());
        TransactionType type = PayType.valueOf(payResponse.getStorage().getPayType()).getTransactionType(order.getTransactionType());
        return payResponse.getService().secondaryInterface(order.getTradeNoOrBillDate(), order.getOutTradeNoBillType(), type, new Callback<Map<String, Object>>() {
            @Override
            public Map<String, Object> perform(Map<String, Object> map) {
                return map;
            }
        });
    }




```

##### 5.支付回调
```java


   /**
       * 支付回调地址
       * @param request
       * @return
       */
      @RequestMapping(value = "payBack{payId}.json")
      public String payBack(HttpServletRequest request, @PathVariable Integer payId) throws IOException {
          //根据账户id，获取对应的支付账户操作工具
          PayResponse payResponse = service.getPayResponse(payId);
          PayConfigStorage storage = payResponse.getStorage();
          //获取支付方返回的对应参数
          Map<String, String> params = payResponse.getService().getParameter2Map(request.getParameterMap(), request.getInputStream());
          if (null == params){
              return payResponse.getService().getPayOutMessage("fail","失败").toMessage();
          }

          //校验
          if (payResponse.getService().verify(params)){
              PayMessage message = new PayMessage(params, storage.getPayType(), storage.getMsgType().name());
              PayOutMessage outMessage = payResponse.getRouter().route(message);
              return outMessage.toMessage();
          }

          return payResponse.getService().getPayOutMessage("fail","失败").toMessage();
      }


```