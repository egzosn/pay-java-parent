/*
SQLyog 企业版 - MySQL GUI v8.14 
MySQL - 5.5.50-MariaDB-wsrep : Database - pay
*********************************************************************
*/


/*Table structure for table `apy_account` */

DROP TABLE IF EXISTS `apy_account`;

CREATE TABLE `apy_account` (
  `pay_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '支付账号id',
  `partner` varchar(32) DEFAULT NULL COMMENT '支付合作id',
  `appid` varchar(32) DEFAULT NULL COMMENT '应用id',
  `public_key` varchar(1204) DEFAULT NULL COMMENT '支付公钥',
  `private_key` varchar(2048) DEFAULT NULL COMMENT '支付私钥',
  `notify_url` varchar(1024) DEFAULT NULL COMMENT '回调地址',
  `seller` varchar(256) DEFAULT NULL COMMENT '收款账号',
  `sign_type` varchar(16) DEFAULT NULL COMMENT '签名类型',
  `input_charset` varchar(16) DEFAULT NULL COMMENT '枚举值，字符编码 utf-8,gbk等等',
  `pay_type` tinyint(1) DEFAULT NULL COMMENT '支付类型,0：支付宝，1微信',
  `msg_type` varchar(8) DEFAULT NULL COMMENT '消息类型，text,xml',
  `create_by` char(32) DEFAULT NULL COMMENT '创建人',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`pay_id`)
) ENGINE=MyISAM AUTO_INCREMENT=0 DEFAULT CHARSET=utf8;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
