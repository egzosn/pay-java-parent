package com.egzosn.pay.common.api;

import java.util.Map;

import com.egzosn.pay.common.bean.AssistOrder;
import com.egzosn.pay.common.bean.TransferOrder;

/**
 * 转账服务
 *
 * @author Egan
 * <pre>
 *  email egan@egzosn.com
 *  date 2023/1/8
 *  </pre>
 */
public interface TransferService {

    /**
     * 转账
     *
     * @param transferOrder 转账订单
     * @return 结果
     */
    Map<String, Object> transfer(TransferOrder transferOrder);

    /**
     * 转账查询
     *
     * @param assistOrder 辅助交易订单
     * @return 对应的转账订单
     */
    Map<String, Object> transferQuery(AssistOrder assistOrder);

}
