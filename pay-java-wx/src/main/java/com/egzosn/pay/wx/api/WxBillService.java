package com.egzosn.pay.wx.api;

import java.util.Date;
import java.util.Map;

/**
 * @description:账单接口
 * @author: faymanwang
 * @email: 1057438332@qq.com
 * @time: 2020/7/31 11:21
 */
public interface WxBillService {

    public Map<String, Object> downloadbill(Date billDate, String billType, String path);
}
