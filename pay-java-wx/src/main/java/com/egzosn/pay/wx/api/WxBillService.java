package com.egzosn.pay.wx.api;

import java.util.Date;
import java.util.Map;

import com.egzosn.pay.common.bean.BillType;

/**
 * 账单接口
 *
 * @author faymanwang
 * email: 1057438332@qq.com
 * time: 2020/7/31 11:21
 */
public interface WxBillService {

    @Deprecated
    Map<String, Object> downloadbill(Date billDate, String billType, String path);

}
