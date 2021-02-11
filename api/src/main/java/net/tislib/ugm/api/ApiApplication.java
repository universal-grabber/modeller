package net.tislib.ugm.api;

import kong.unirest.Unirest;
import org.apache.http.ssl.SSLContexts;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import unirest.shaded.org.apache.http.conn.ssl.DefaultHostnameVerifier;
import unirest.shaded.org.apache.http.conn.ssl.NoopHostnameVerifier;
import unirest.shaded.org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import unirest.shaded.org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import unirest.shaded.org.apache.http.impl.client.CloseableHttpClient;
import unirest.shaded.org.apache.http.impl.client.HttpClientBuilder;
import unirest.shaded.org.apache.http.impl.client.HttpClients;

import javax.annotation.PostConstruct;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;

@SpringBootApplication
public class ApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class, args);
    }

    static {
        try {

            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }

            }};


            SSLContext sslcontext = SSLContext.getInstance("SSL");
            sslcontext.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sslcontext.getSocketFactory());
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, NoopHostnameVerifier.INSTANCE);

            CloseableHttpClient httpclient = HttpClients.custom()
                    .setSSLSocketFactory(sslsf)
                    .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                    .build();

            Unirest.config().httpClient(httpclient);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
