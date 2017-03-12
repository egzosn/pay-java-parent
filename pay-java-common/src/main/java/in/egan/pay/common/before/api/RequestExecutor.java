package in.egan.pay.common.before.api;


import in.egan.pay.common.exception.PayErrorException;
import org.apache.http.HttpHost;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.IOException;

/**
 * http请求执行器
 *
 * @param <T> 返回值类型
 * @param <E> 请求参数类型
 * @source chanjarster/weixin-java-tools
 */
@Deprecated
public interface RequestExecutor<T, E> {

    /**
     *
     * @param httpclient 传入的httpClient
     * @param httpProxy http代理对象，如果没有配置代理则为空
     * @param uri       uri
     * @param data      数据
     * @return
     * @throws java.io.IOException
     */
    public T execute(CloseableHttpClient httpclient, HttpHost httpProxy, String uri, E data) throws PayErrorException, IOException;

}
