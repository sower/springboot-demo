package me.demo.controller;

import com.google.common.collect.ImmutableMap;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;
import lombok.extern.slf4j.Slf4j;
import me.demo.annotation.LimitRequest;
import me.demo.bean.Person;
import me.demo.service.HttpBinService;
import me.demo.utils.BaseContextHolder;
import me.demo.utils.HttpsUtils;
import me.demo.utils.I18nUtils;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;

/**
 * 控制器
 *
 * @date 2022/09/04
 */
@RestController
@Validated
@Slf4j
public class TestController {

  @Resource Person person;

  @Resource HttpsUtils httpsUtils;

  @Resource HttpBinService binService;

  @Resource ThreadPoolTaskExecutor asyncTaskExecutor;

  @LimitRequest
  @GetMapping("/index")
  public String hello(@NotEmpty String a, String method) {

    switch (HttpMethod.resolve(method)) {
      case POST:
        binService.post(person);
        break;
      case PUT:
        binService.put(ImmutableMap.of("method", "put"), a);
        break;
      case DELETE:
        binService.delete(a);
        break;
      default:
        binService.get(a, ImmutableMap.of("method", "get"));
    }

    CompletableFuture.runAsync(this::testContext, asyncTaskExecutor);
    return I18nUtils.getMessage("hello", person.toString());
  }

  @PostMapping("/http-bin")
  public String bin(
      String method, String param, @RequestBody(required = false) Map<String, String> body)
      throws IOException {
    String url = "http://httpbin.org/" + method.toLowerCase(Locale.ROOT);
    return httpsUtils
        .url(url)
        .queryParams(ImmutableMap.of("param", param))
        .headers(ImmutableMap.of("h-date", LocalDateTime.now().toString()))
        .jsonBody(body)
        .sync(method.toUpperCase(Locale.ROOT))
        .body()
        .string();
  }

  public void testContext() {
    log.info("test - BaseContext Properties {}", BaseContextHolder.getContext().getProperties());
    if (RequestContextHolder.getRequestAttributes() != null) {
      log.info("----- request -----");
    }
  }
}
