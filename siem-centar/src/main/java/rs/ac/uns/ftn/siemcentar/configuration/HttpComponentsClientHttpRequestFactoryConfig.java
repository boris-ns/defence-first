package rs.ac.uns.ftn.siemcentar.configuration;

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
    private SSLContext sslContext;

    @Bean(name = "httFactory")
    public HttpComponentsClientHttpRequestFactory getHttpComponentsClientHttpRequestFactory() {

        HttpComponentsClientHttpRequestFactory requestFactory = null;
        try {

            // NoopHostnameVerifier.INSTANCE da nista ne validira ---> ovo ako stavis onda klijent ne proverava
                // servera nego ga pusti...
            // izlgeda moze loadTrustMaterial sa neta da se uvuce

            // on ga prihvati i kada nema nista u keyStore-u tj kada nema sertifikat od ovog
                // neam pojma...
            // MORA STRATEGIJA DA SE NAMESTI DA VERUJE SMAO ONIMA KOJI SU U KEYSTORU
            // ili onima koje proveri...
//            SSLContext sslContext = SSLContexts.custom()
//                    .loadTrustMaterial(null, new MyTrustStrategy(keystore, ocspService))
//                    .loadKeyMaterial(keystore, keyStorePassword.toCharArray()).build();

            SSLConnectionSocketFactory socketFactory =
                    new SSLConnectionSocketFactory(sslContext,
                            SSLConnectionSocketFactory.getDefaultHostnameVerifier()
                    );

            CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(socketFactory)
                    .setMaxConnTotal(Integer.valueOf(5)).setMaxConnPerRoute(Integer.valueOf(5)).build();

            requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
            requestFactory.setReadTimeout(Integer.valueOf(10000));
            requestFactory.setConnectTimeout(Integer.valueOf(10000));
        }
        catch (Exception e){
            e.printStackTrace();
        }
        finally {
            return requestFactory;
        }
    }

}
