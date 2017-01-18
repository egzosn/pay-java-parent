/*
 * Copyright 2002-2017 the original huodull or egan.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package in.egan.pay.common.api;

import in.egan.pay.common.exception.PayErrorException;
import in.egan.pay.common.util.str.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;

/**
 * @author: egan
 * @email egzosn@gmail.com
 * @date 2017/1/12 20:09
 */
public abstract class BasePayService implements PayService {

    protected PayConfigStorage payConfigStorage;

    protected CloseableHttpClient httpClient;

    protected HttpHost httpProxy;

    protected int retrySleepMillis = 1000;

    protected int maxRetryTimes = 5;

    protected <T, E> T executeInternal(RequestExecutor<T, E> executor, String uri, E data) throws PayErrorException {

        try {
            return executor.execute(getHttpClient(), httpProxy, uri, data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public HttpHost getHttpProxy() {
        return httpProxy;
    }

    public CloseableHttpClient getHttpClient() {
        return httpClient;
    }

    /**
     * 设置支付配置
     * @param payConfigStorage 支付配置
     */
    public void setPayConfigStorage(PayConfigStorage payConfigStorage) {
        this.payConfigStorage = payConfigStorage;

        String http_proxy_host = payConfigStorage.getHttpProxyHost();
        int http_proxy_port = payConfigStorage.getHttpProxyPort();
        String http_proxy_username = payConfigStorage.getHttpProxyUsername();
        String http_proxy_password = payConfigStorage.getHttpProxyPassword();

        if (StringUtils.isNotBlank(http_proxy_host)) {
            // 使用代理服务器
            if (StringUtils.isNotBlank(http_proxy_username)) {
                // 需要用户认证的代理服务器
                CredentialsProvider credsProvider = new BasicCredentialsProvider();
                credsProvider.setCredentials(
                        new AuthScope(http_proxy_host, http_proxy_port),
                        new UsernamePasswordCredentials(http_proxy_username, http_proxy_password));
                httpClient = HttpClients
                        .custom()
                        .setDefaultCredentialsProvider(credsProvider)
                        .build();
            } else {
                // 无需用户认证的代理服务器
                httpClient = HttpClients.createDefault();
            }
            httpProxy = new HttpHost(http_proxy_host, http_proxy_port);
        } else {
            httpClient = HttpClients.createDefault();
        }
    }

    @Override
    public PayConfigStorage getPayConfigStorage() {
        return payConfigStorage;
    }

    public BasePayService() {
    }


    public BasePayService(PayConfigStorage payConfigStorage) {
        setPayConfigStorage(payConfigStorage);
    }

}
