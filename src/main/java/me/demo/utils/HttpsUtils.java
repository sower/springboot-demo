package me.demo.utils;

import com.alibaba.fastjson.JSON;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import lombok.extern.slf4j.Slf4j;
import me.demo.config.HttpsClientConfig;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.ConnectionPool;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

/**
 * @author Ylem
 * @date 2022/09/04
 **/
@Slf4j
@Component
public class HttpsUtils {

  OkHttpClient client;
  public Map<String, String> queryParams;
  private String url;
  public static MediaType JSON_MEDIA_TYPE = MediaType.get("application/json; charset=utf-8");

  private Request.Builder request;
  private Map<String, String> headers;
  private RequestBody body;

  private final boolean isReset = true;

  @Autowired
  public HttpsUtils(HttpsClientConfig clientConfig) {
    TrustManager[] trustManagers = buildTrustManagers();
    client = new OkHttpClient().newBuilder()
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
        .sslSocketFactory(createSSLSocketFactory(trustManagers),
            (X509TrustManager) trustManagers[0])
        .hostnameVerifier((hostName, session) -> StringUtils.isNotBlank(hostName))
        //设置连接池  最大连接数量  , 持续存活的连接
        .connectionPool(new ConnectionPool(clientConfig.getConnectionTimeout(),
            clientConfig.getKeepAliveTimeout(), TimeUnit.MINUTES))
        .build();
  }


  public HttpsUtils headers(Map<String, String> headers) {
    this.headers = headers;
    return this;
  }

  public HttpsUtils url(String url) {
    this.url = url;
    return this;
  }

  public HttpsUtils queryParams(Map<String, String> params) {
    this.queryParams = params;
    return this;
  }

  public HttpsUtils body(RequestBody body) {
    this.body = body;
    return this;
  }

  public HttpsUtils jsonBody(@NotNull String json) {
    return body(RequestBody.create(json, JSON_MEDIA_TYPE));
  }

  public HttpsUtils jsonBody(Object obj) {
    if (obj == null) {
      return this;
    }
    return jsonBody(JSON.toJSONString(obj));
  }

  public HttpsUtils formBody(Map<String, String> map) {
    FormBody.Builder formBody = new FormBody.Builder(StandardCharsets.UTF_8);
    map.forEach(formBody::addEncoded);
    return body(formBody.build());
  }

  public HttpsUtils fileBody() {
    RequestBody fileBody = RequestBody.create(new File("path/attachment.png"),
        MediaType.parse("image/png"));
    body = new MultipartBody.Builder().setType(MultipartBody.FORM)
        .addFormDataPart("file", "head_img", fileBody)
        .addFormDataPart("key", "val").build();
    return this;
  }

  private void beforeRequest() {
    log.info("build request");

    // 构建url
    HttpUrl.Builder urlBuilder = HttpUrl.get(url).newBuilder();
    if (ObjectUtils.isNotEmpty(this.queryParams)) {
      this.queryParams.forEach((key, value) -> {
        if (StringUtils.isNotBlank(value)) {
          urlBuilder.addQueryParameter(key, value);
        }
      });
    }

    this.request = new Request.Builder().url(urlBuilder.build());
    if (ObjectUtils.isNotEmpty(this.headers)) {
      this.request.headers(Headers.of(headers));
    }

  }

  private void afterRequest() {
    log.info("end request");
    if (isReset) {
      headers = null;
      queryParams = null;
      body = null;
      log.info("Reset request");
    }
  }

  public Response sync(String method) throws IOException {
    Response response;
    try {
      beforeRequest();
      this.request.method(method, body);
      response = this.client.newCall(request.build()).execute();
    } finally {
      afterRequest();
    }
    return response;
  }

  public Response get() throws IOException {
    return sync(HttpMethod.GET.name());
  }

  public Response post() throws IOException {
    return sync(HttpMethod.POST.name());
  }

  public Response put() throws IOException {
    return sync(HttpMethod.PUT.name());
  }

  public Response delete() throws IOException {
    return sync(HttpMethod.DELETE.name());
  }

  private static volatile Semaphore semaphore = null;

  /**
   * 用于异步请求时，控制访问线程数，返回结果
   */
  private static Semaphore getSemaphoreInstance() {
    //只能1个线程同时访问
    synchronized (HttpsUtils.class) {
      if (semaphore == null) {
        semaphore = new Semaphore(0);
      }
    }
    return semaphore;
  }

  public String async() {
    StringBuffer buffer = new StringBuffer();
    client.newCall(request.build()).enqueue(new Callback() {

      @Override
      public void onFailure(@NotNull Call call, @NotNull IOException e) {
        buffer.append("request failed: ").append(e.getMessage());
        e.printStackTrace();
      }

      @Override
      public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
        ResponseBody body = response.body();
        if (body != null) {
          buffer.append(body.string());
        }
        getSemaphoreInstance().release();
      }
    });
    getSemaphoreInstance().release();
    return buffer.toString();
  }

  /**
   * 生成安全套接字工厂，用于https请求的证书跳过
   */
  private static SSLSocketFactory createSSLSocketFactory(TrustManager[] trustAllCerts) {
    SSLSocketFactory ssfFactory = null;
    try {
      SSLContext sc = SSLContext.getInstance("TLS");
      sc.init(null, trustAllCerts, new SecureRandom());
      ssfFactory = sc.getSocketFactory();
    } catch (Exception e) {
      log.info("Create SSLSocketFactory error", e);
    }
    return ssfFactory;
  }

  private static TrustManager[] buildTrustManagers() {
    return new TrustManager[]{
        new X509TrustManager() {
          @Override
          public void checkClientTrusted(X509Certificate[] chain, String authType) {
          }

          @Override
          public void checkServerTrusted(X509Certificate[] chain, String authType) {
          }

          @Override
          public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[]{};
          }
        }
    };
  }

  static class BasicLoggingInterceptor implements Interceptor {

    @NotNull
    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
      Request request = chain.request();
      log.info("===> Sending {} request: {}", request.method(), request.url());

      log.info("request headers: {}", request.headers());

      RequestBody requestBody = request.body();
      if (requestBody != null) {
        Buffer buffer = new Buffer();
        requestBody.writeTo(buffer);
        log.info("request body: {}", buffer.readUtf8());
      }

      Response response = chain.proceed(request);

      log.info("<=== Received response code is {} , response headers: {}", response.code(),
          response.headers());

      ResponseBody responseBody = null;
      try {
        responseBody = response.peekBody(Long.MAX_VALUE);
        log.info("response body:{}", responseBody.string());
      } catch (Exception e) {
        log.warn("response body is null : {}", e.getMessage());
      }

      return response;
    }

  }
}
