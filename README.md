
##整合支付模块

声明： 本项目最初想法自 https://github.com/chanjarster/weixin-java-tools, 15年1月左右关注chanjarster/weixin-java-tools，并将其回调处理修改并进行使用。


##### 详细文档请看 [wiki](https://github.com/egzosn/pay-java-parent/wiki)。

### 特性



    1. 不依赖任何 mvc 框架，依赖极少:httpclient，fastjson,log4j,com.google.zxing，项目精简，不用担心项目迁移问题
    2. 也不依赖 servlet，仅仅作为工具使用，可轻松嵌入到任何系统里（项目例子利用spring mvc的 @PathVariable进行，推荐使用类似的框架）
    3. 支付请求调用支持HTTP和异步、支持http代理，连接池
    4. 控制层统一异常处理
    5. LogBack日志记录
    6. 简单快速完成支付模块的开发
    7. 支持多种支付类型多支付账户扩展

### 本项目包含 3 个部分：

     1. pay-java-common  公共lib,支付核心与规范定义
     2. pay-java-demo  具体的支付demo
     3. pay-java-*  具体的支付实现库
### Maven配置
支付核心模块
```xml

<dependency>
    <groupId>com.egzosn</groupId>
    <artifactId>pay-java-common</artifactId>
    <version>2.12.6</version>
</dependency>

```

具体支付模块 "{module-name}" 为具体的支付渠道的模块名 pay-java-ali，pay-java-wx等

```xml

<dependency>
    <groupId>com.egzosn</groupId>
    <artifactId>{module-name}</artifactId>
    <version>2.12.6</version>
</dependency>

```
#### 本项目在以下代码托管网站
* 码云：https://gitee.com/egzosn/pay-java-parent
* GitHub：https://github.com/egzosn/pay-java-parent


### 使用
这里不多说直接上代码 


###### 单一支付教程 

 * [基础模块支付宝微信讲解](https://github.com/egzosn/pay-java-parent/wiki)
 * [银联](pay-java-union?dir=1&filepath=pay-java-union)
 * [payoneer](pay-java-payoneer?dir=1&filepath=pay-java-payoneer)
 * [paypal](pay-java-paypal?dir=1&filepath=pay-java-paypal)
 * [友店微信](pay-java-wx-youdian?dir=1&filepath=pay-java-youdian)
 * [富友](pay-java-fuiou?dir=1&filepath=pay-java-fuiou)


支付整合》服务端+网页端详细使用与简单教程请看 [pay-java-demo](pay-java-demo?dir=1&filepath=pay-java-demo)

android 例子 [pay-java-android](https://github.com/egzosn/pay-java-android)



## 交流
很希望更多志同道合友友一起扩展新的的支付接口。

这里感谢[ouyangxiangshao](https://github.com/ouyangxiangshao),[ZhuangXiong](https://github.com/ZhuangXiong) 与[Actinian](http://git.oschina.net/Actinia517) 所提交的安卓例子或者分支

也感谢各大友友同学帮忙进行接口测试

非常欢迎和感谢对本项目发起Pull Request的同学，不过本项目基于git flow开发流程，因此在发起Pull Request的时候请选择develop分支。

E-Mail：egzosn@gmail.com

QQ群：542193977

