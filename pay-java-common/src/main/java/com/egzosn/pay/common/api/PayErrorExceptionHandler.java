package com.egzosn.pay.common.api;


import com.egzosn.pay.common.exception.PayErrorException;

/**
 *   PayErrorExceptionHandler处理器
 *
 * @author  egan
 * @email egzosn@gmail.com
 * @date 2016-6-1 11:33:01
 */
public interface PayErrorExceptionHandler {

    /**
     * 异常统一处理器
     * @param e
     */
     void handle(PayErrorException e);

}
