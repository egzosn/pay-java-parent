package in.egan.pay.ali.api;

import in.egan.pay.common.api.PayConfigStorage;
import in.egan.pay.common.api.PayNotify;
import in.egan.pay.common.api.RequestExecutor;
import in.egan.pay.common.bean.result.PayError;
import in.egan.pay.common.exception.PayErrorException;
import org.apache.http.HttpHost;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 *  支付宝支付通知
 * @author  egan
 * @email egzosn@gmail.com
 * @date 2016-5-18 14:09:01
 */
public class AliPayNotify implements PayNotify {
    protected final Logger log = LoggerFactory.getLogger(AliPayNotify.class);

    protected PayConfigStorage payConfigStorage;

    protected CloseableHttpClient httpClient;

    protected HttpHost httpProxy;

    private int retrySleepMillis = 1000;

    private int maxRetryTimes = 5;

    @Override
    public String verifyResponse(String notify_id) {
        return null;
    }


    /**
     * 向支付宝端发送请求，在这里执行的策略是当发生access_token过期时才去刷新，然后重新执行请求，而不是全局定时请求
     *
     * @param executor
     * @param uri
     * @param data
     * @return
     * @throws PayErrorException
     */
    @Override
    public <T, E> T execute(RequestExecutor<T, E> executor, String uri, E data) throws PayErrorException {
        int retryTimes = 0;
        do {
            try {
                return executeInternal(executor, uri, data);
            } catch (PayErrorException e) {
                PayError error = e.getError();
                /**
                 * -1 系统繁忙, 1000ms后重试
                 */
                if (error.getErrorCode() == -1) {
                    int sleepMillis = retrySleepMillis * (1 << retryTimes);
                    try {
                        log.debug("支付宝系统繁忙，{}ms 后重试(第{}次)", sleepMillis, retryTimes + 1);
                        Thread.sleep(sleepMillis);
                    } catch (InterruptedException e1) {
                        throw new RuntimeException(e1);
                    }
                } else {
                    throw e;
                }
            }
        } while (++retryTimes < maxRetryTimes);

        throw new RuntimeException("支付宝服务端异常，超出重试次数");
    }

    protected <T, E> T executeInternal(RequestExecutor<T, E> executor, String uri, E data) throws PayErrorException {

        return null;    }
}
