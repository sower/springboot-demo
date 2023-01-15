package me.demo.config;

import feign.Feign;
import feign.Logger;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import me.demo.utils.HttpsUtils;
import me.demo.utils.HttpsUtils.BasicLoggingInterceptor;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @description
 * @date 2023/01/15
 **/
@Configuration
@ConditionalOnClass(Feign.class)
@AutoConfigureBefore(FeignAutoConfiguration.class)
public class FeignLogConfig {

  @Bean
  Logger.Level feignLoggerLevel() {
    return Logger.Level.FULL;
  }

  @Bean
  public OkHttpClient.Builder okHttpClientBuilder(HttpsClientConfig clientConfig) {
    TrustManager[] trustManagers = HttpsUtils.buildTrustManagers();
    return new OkHttpClient().newBuilder()
        .addInterceptor(new BasicLoggingInterceptor())
//      .cache(cache) // configure cache
//      .proxy(proxy) // configure proxy
//      .certificatePinner(certificatePinner) // certificate pinning
//      .addNetworkInterceptor(interceptor) // network level interceptor
//      .authenticator(authenticator) // authenticator for requests (it supports similar use-cases as "Authorization header" earlier
        .callTimeout(clientConfig.getCallTimeout(),
            TimeUnit.SECONDS) // default timeout for complete calls
        .readTimeout(clientConfig.getReadTimeout(),
            TimeUnit.SECONDS) // default read timeout for new connections
        .writeTimeout(clientConfig.getWriteTimeout(),
            TimeUnit.SECONDS) // default write timeout for new connections
//      .dns(dns) // DNS service used to lookup IP addresses for hostnames
//      .followRedirects(true) // follow requests redirects
//      .followSslRedirects(true) // follow HTTP tp HTTPS redirects
//      .connectionPool(connectionPool) // connection pool used to recycle HTTP and HTTPS connections
//      .retryOnConnectionFailure(true) // retry or not when a connectivity problem is encountered
//      .cookieJar(cookieJar) // cookie manager
//      .dispatcher(dispatcher) // dispatcher used to set policy and execute asynchronous requests
        .sslSocketFactory(HttpsUtils.createSSLSocketFactory(trustManagers),
            (X509TrustManager) trustManagers[0])
        .hostnameVerifier((hostName, session) -> StringUtils.isNotBlank(hostName))
        //设置连接池  最大连接数量  , 持续存活的连接
        .connectionPool(new ConnectionPool(clientConfig.getConnectionTimeout(),
            clientConfig.getKeepAliveTimeout(), TimeUnit.MINUTES));
  }

}
