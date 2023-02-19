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
import me.demo.service.FeignService;
import me.demo.utils.HttpsUtils;
import me.demo.utils.MessageUtils;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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

  @Resource FeignService feignService;

  @Resource ThreadPoolTaskExecutor asyncTaskExecutor;

  @LimitRequest
  @GetMapping("/index")
  public String hello(@NotEmpty String a, String b) {
    log.info("Hello {}", feignService.uuid());
    CompletableFuture.runAsync(() -> log.info("CompletableFuture {}", a), asyncTaskExecutor);
    return MessageUtils.getMessage("hello", person.toString());
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
}
