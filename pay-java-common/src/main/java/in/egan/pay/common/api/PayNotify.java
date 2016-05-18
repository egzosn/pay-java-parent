package in.egan.pay.common.api;

import in.egan.pay.common.exception.PayErrorException;

/**
 * 支付通知
 * @author  egan
 * @email egzosn@gmail.com
 * @date 2016-5-18 14:09:01
 */
public interface PayNotify {


        public String verifyResponse(String notify_id);
        public <T, E> T execute(RequestExecutor<T, E> executor, String uri, E data) throws PayErrorException;

}
