package rs.ac.uns.ftn.siemcentar.configuration;

import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import rs.ac.uns.ftn.siemcentar.service.OCSPService;

import javax.net.ssl.SSLContext;
import java.security.KeyStore;

@Configuration
public class HttpComponentsClientHttpRequestFactoryConfig {


    @Autowired
    @Qualifier("myContextOCSP")
    private SSLContext sslContextOCSP;


    @Autowired
    @Qualifier("myContextNoOCSP")
    private SSLContext sslContextNoOCSP;


    @Bean(name = "httFactoryOCSP")
    public HttpComponentsClientHttpRequestFactory getHttpComponentsClientHttpRequestFactoryOCSP() {

        HttpComponentsClientHttpRequestFactory requestFactory = null;
        try {
            SSLConnectionSocketFactory socketFactory =
                    new SSLConnectionSocketFactory(sslContextOCSP, NoopHostnameVerifier.INSTANCE
//                            SSLConnectionSocketFactory.getDefaultHostnameVerifier()
                    );

            CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(socketFactory)
                    .setMaxConnTotal(Integer.valueOf(5)).setMaxConnPerRoute(Integer.valueOf(5)).build();

            requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
            requestFactory.setReadTimeout(Integer.valueOf(1000000000));
            requestFactory.setConnectTimeout(Integer.valueOf(1000000000));
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            return requestFactory;
        }
    }


    @Bean(name = "httFactoryNoOCSP")
    public HttpComponentsClientHttpRequestFactory getHttpComponentsClientHttpRequestFactoryNoOCSP() {

        HttpComponentsClientHttpRequestFactory requestFactory = null;
        try {
            SSLConnectionSocketFactory socketFactory =
                    new SSLConnectionSocketFactory(sslContextNoOCSP, NoopHostnameVerifier.INSTANCE
//                            SSLConnectionSocketFactory.getDefaultHostnameVerifier()
                    );

            CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(socketFactory)
                    .setMaxConnTotal(Integer.valueOf(5)).setMaxConnPerRoute(Integer.valueOf(5)).build();

            requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
            requestFactory.setReadTimeout(Integer.valueOf(1000000000));
            requestFactory.setConnectTimeout(Integer.valueOf(1000000000));
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            return requestFactory;
        }
    }
}
