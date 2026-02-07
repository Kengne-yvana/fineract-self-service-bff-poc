package org.apache.fineract.bff.config;

import feign.Client;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;

@Configuration
public class FeignConfig {
    @Bean
    public Client feignClient() {
        return new Client.Default(getInsecureSSLSocketFactory(), null);
    }

    private static java.util.concurrent.atomic.AtomicReference<javax.net.ssl.SSLSocketFactory> sslSocketFactory = new java.util.concurrent.atomic.AtomicReference<>();

    private static javax.net.ssl.SSLSocketFactory getInsecureSSLSocketFactory() {
        try {
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() { return null; }
                public void checkClientTrusted(X509Certificate[] certs, String authType) {}
                public void checkServerTrusted(X509Certificate[] certs, String authType) {}
            }};
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            return sc.getSocketFactory();
        } catch (Exception e) { throw new RuntimeException(e); }
    }
}
