
/*Table structure for table `apy_account` */

DROP TABLE IF EXISTS `apy_account`;

CREATE TABLE `apy_account` (
  `pay_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '支付账号id',
  `partner` varchar(32) DEFAULT NULL COMMENT '支付合作id,商户id，差不多是支付平台的账号或id',
  `appid` varchar(32) DEFAULT NULL COMMENT '应用id',
  `public_key` varchar(1204) DEFAULT NULL COMMENT '支付公钥，sign_type只有单一key时public_key与private_key相等，比如sign_type=MD5的情况',
  `private_key` varchar(2048) DEFAULT NULL COMMENT '支付私钥',
  `notify_url` varchar(1024) DEFAULT NULL COMMENT '异步回调地址',
  `return_url` varchar(1024) DEFAULT NULL COMMENT '同步回调地址',
  `seller` varchar(256) DEFAULT NULL COMMENT '收款账号, 针对支付宝',
  `sign_type` varchar(16) DEFAULT NULL COMMENT '签名类型',
  `input_charset` varchar(16) DEFAULT NULL COMMENT '枚举值，字符编码 utf-8,gbk等等',
  `pay_type` char(16) DEFAULT NULL COMMENT '支付类型,aliPay：支付宝，wxPay：微信, youdianPay: 友店微信,此处开发者自定义对应in.egan.pay.demo.entity.PayType枚举值',
  `msg_type` char(8) DEFAULT NULL COMMENT '消息类型，text,xml,json',
  `create_by` char(32) DEFAULT NULL COMMENT '创建人',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`pay_id`)
) ENGINE=MyISAM AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

/*Data for the table `apy_account` */

insert  into `apy_account`(`pay_id`,`partner`,`appid`,`public_key`,`private_key`,`notify_url`,`return_url`,`seller`,`sign_type`,`input_charset`,`pay_type`,`msg_type`,`create_by`,`create_time`) values
(1,'12******01','wxa**********ba9e9','48gf0iwuhr***********r9weh9eiut9','48gf0iwuhr***********r9weh9eiut9','http://pay.egan.in/payBack2.json','同步回调地址','','MD5','utf-8','wxPay','xml','egan','2017-01-20 17:07:48'),
(2,'20889119449*****','','MIGfMA0GCSqGSIb3DQEB*********gmLCUYuLkxpLQIDAQAB','IqZg51Vx8BvyypnIfKgw=*********MIICdwIBADANBgkqhkiG9w0BAQE','http://pay.egan.in/payBack3.json','同步回调地址','egzosn@gmail.com','RSA','utf-8','aliPay','text','egan','2017-01-20 17:11:46'),



