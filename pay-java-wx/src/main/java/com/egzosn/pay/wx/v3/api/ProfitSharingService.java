package com.egzosn.pay.wx.v3.api;

import java.util.Map;

import com.egzosn.pay.common.bean.PayOrder;

/**
 * 分账服务
 * @author Egan
 * <pre>
 * email egan@egzosn.com
 * date 2021/10/6
 * </pre>
 */
public interface ProfitSharingService {

    /**
     * 添加分账接收方
     * @param order  添加分账
     * @return 结果
     */
    Map<String, Object> add(PayOrder order);
    /**
     * 删除分账接收方
     * @param order  删除分账
     * @return 结果
     */
    Map<String, Object> delete(PayOrder order);
    /**
     * 解冻剩余资金
     * @param order  解冻
     * @return 结果
     */
    Map<String, Object> unfreeze(PayOrder order);

}
