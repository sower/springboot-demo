package me.demo.controller;

import com.google.common.collect.ImmutableMap;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Map;
import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;
import lombok.extern.slf4j.Slf4j;
import me.demo.bean.Person;
import me.demo.utils.HttpsUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Ylem
 * @date 2022/09/04
 **/
@RestController
@Validated
@Slf4j
public class TestController {

  @Resource
  Person person;

  @Resource
  HttpsUtils httpsUtils;

  @GetMapping("/index")
  public String hello(@NotEmpty String a, @RequestParam String b) {
    log.info("Hello");
    return person.toString();
  }

  @PostMapping("/http-bin")
  public String hello(String method, String param,
      @RequestBody(required = false) Map<String, String> body) throws IOException {
    String url = "http://httpbin.org/" + method.toLowerCase(Locale.ROOT);
    return httpsUtils.url(url)
        .queryParams(ImmutableMap.of("param", param))
        .headers(ImmutableMap.of("h-date", LocalDateTime.now().toString()))
        .jsonBody(body)
        .sync(method.toUpperCase(Locale.ROOT))
        .body().string();
  }


}
