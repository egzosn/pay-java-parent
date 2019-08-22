全能第三方支付对接Java开发工具包.优雅的轻量级支付模块集成支付对接支付整合（微信,支付宝,银联,友店,富友,跨境支付paypal,payoneer(P卡派安盈)易极付）app,扫码,网页支付刷卡付条码付刷脸付转账服务商模式、支持多种支付类型多支付账户，支付与业务完全剥离，简单几行代码即可实现支付，简单快速完成支付模块的开发，可轻松嵌入到任何系统里 目前仅是一个开发工具包（即SDK），只提供简单Web实现，建议使用maven或gradle引用本项目即可使用本SDK提供的各种支付相关的功能 


### 特性
    1. 不依赖任何 mvc 框架，依赖极少:httpclient，fastjson,log4j,com.google.zxing，项目精简，不用担心项目迁移问题
    2. 也不依赖 servlet，仅仅作为工具使用，可轻松嵌入到任何系统里（项目例子利用spring mvc的 @PathVariable进行，推荐使用类似的框架）
    3. 支付请求调用支持HTTP和异步、支持http代理，连接池
    4. 简单快速完成支付模块的开发
    5. 支持多种支付类型多支付账户扩展

### 本项目包含 3 个部分：

     1. pay-java-common  公共lib,支付核心与规范定义
     2. pay-java-demo  具体的支付demo
     3. pay-java-*  具体的支付实现库
     
### Maven配置
具体支付模块 "{module-name}" 为具体的支付渠道的模块名 pay-java-ali，pay-java-wx等

```xml
<dependency>
    <groupId>com.egzosn</groupId>
    <artifactId>{module-name}</artifactId>
    <version>2.12.8</version>
</dependency>

```
#### 本项目在以下代码托管网站
* 码云：https://gitee.com/egzosn/pay-java-parent
* GitHub：https://github.com/egzosn/pay-java-parent

#### 基于spring-boot实现自动化配置的支付对接，让你真正做到一行代码实现支付聚合，让你可以不用理解支付怎么对接，只需要专注你的业务  全能第三方支付对接spring-boot-starter-pay开发工具包
* 码云：https://gitee.com/egzosn/pay-spring-boot-starter-parent
* GitHub：https://github.com/egzosn/pay-spring-boot-starter-parent

##### 开源中国项目地址
如果你觉得项目对你有帮助，也点击下进入后点击收藏呗
* 基础支付聚合组件[pay-java-parent](https://www.oschina.net/p/pay-java-parent)
* spring-boot-starter自动化配置支付聚合组件 [pay-spring-boot-starter](https://www.oschina.net/p/spring-boot-starter-pay)

###### 支付教程 

 * [基础模块支付宝微信讲解](https://gitee.com/egzosn/pay-java-parent/wikis/Home)
 * [银联](pay-java-union?dir=1&filepath=pay-java-union)
 * [payoneer](pay-java-payoneer?dir=1&filepath=pay-java-payoneer)
 * [paypal](pay-java-paypal?dir=1&filepath=pay-java-paypal)
 * [友店微信](pay-java-wx-youdian?dir=1&filepath=pay-java-youdian)
 * [富友](pay-java-fuiou?dir=1&filepath=pay-java-fuiou)


支付整合》服务端+网页端详细使用与简单教程请看 [pay-java-demo](pay-java-demo?dir=1&filepath=pay-java-demo)

android 例子 [pay-java-android](https://gitee.com/egzosn/pay-java-android)



## 交流
很希望更多志同道合友友一起扩展新的的支付接口。

这里感谢[ouyangxiangshao](https://github.com/ouyangxiangshao),[ZhuangXiong](https://github.com/ZhuangXiong) 与[Actinian](http://git.oschina.net/Actinia517) 所提交的安卓例子或者分支

也感谢各大友友同学帮忙进行接口测试

非常欢迎和感谢对本项目发起Pull Request的同学，不过本项目基于git flow开发流程，因此在发起Pull Request的时候请选择develop分支。

E-Mail：egzosn@gmail.com

QQ群：542193977

