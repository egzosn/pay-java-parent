import com.alibaba.fastjson.JSON;
import com.egzosn.pay.common.bean.CurType;
import com.egzosn.pay.common.util.sign.encrypt.Base64;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.util.UUID;

/**
 *
 * 
 * payoneer支付测试
 * @author Actinia
 * @email hayesfu@qq.com
 * @date 2018/1/18 0018 16:49
 */
public class PayTest {

    public static void  main(String[] args) throws IOException {

        if (1==1){

            String auth = Base64.encode("egan6190:12BkDT8152Zj".getBytes());
            System.out.println(auth);
            return;
        }
//        URI uri = URI.create("https://api.sandbox.payoneer.com/v2/programs/100086190/payees/registration-link");
//                                https://api.sandbox.payoneer.com/v2/programs/100086190/payees/registration-link
        URI uri = URI.create("https://api.sandbox.payoneer.com/v2/programs/100086190/charges");
        HttpHost target = new HttpHost(uri.getHost(), uri.getPort(), uri.getScheme());
        HttpPost httpPost = new HttpPost(uri.toString());

        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(
                new AuthScope(uri.getHost(), uri.getPort()),
                new UsernamePasswordCredentials("egan6190", "12BkDT8152Zj"));

        CloseableHttpClient httpclient = HttpClients.custom()
                .setDefaultCredentialsProvider(credsProvider)
                .build();
        try {

            // Create AuthCache instance
            AuthCache authCache = new BasicAuthCache();
            // Generate BASIC scheme object and add it to the local
            // auth cache
            BasicScheme basicAuth = new BasicScheme();
            authCache.put(target, basicAuth);

            // Add AuthCache to the execution context
            HttpClientContext localContext = HttpClientContext.create();
//            localContext.setCredentialsProvider(credsProvider);
            localContext.setAuthCache(authCache);

//            BasicHttpContext localContext = new BasicHttpContext();
//            localContext.setAttribute(ClientContext.AUTH_CACHE,authCache);

//            PayoneerRequestBean bean = new PayoneerRequestBean("666");
            String referenceId = UUID.randomUUID().toString().replace("-", "");
            PayoneerRequestBean bean = new PayoneerRequestBean("8a2950f959043699015904453b330057","1.01", referenceId, CurType.USD,"egan order");
//            PayoneerRequestBean bean = JSON.parseObject("{\"amount\":\"1.00\",\"client_reference_id\":\""+ System.nanoTime()+"\",\"currency\":\"USD\",\"description\":\"aaabb\",\"payee_id\":\"asdfg13\"}", PayoneerRequestBean.class);
            System.out.println(JSON.toJSONString(bean));
            StringEntity entity = new StringEntity(JSON.toJSONString(bean), ContentType.APPLICATION_JSON);
            httpPost.setEntity(entity);


            System.out.println("Executing request " + httpPost.getRequestLine() + " to target " + target);
            for (int i = 0; i < 1; i++) {
                CloseableHttpResponse response = httpclient.execute(target, httpPost, localContext);
                try {
                    System.out.println("----------------------------------------");
                    System.out.println(response.getStatusLine());
                    System.out.println(EntityUtils.toString(response.getEntity()));
                } finally {
                    response.close();
                }
            }
        } finally {
            httpclient.close();
        }
    }

}
