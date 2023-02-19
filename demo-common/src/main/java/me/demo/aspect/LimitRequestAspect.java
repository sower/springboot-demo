package me.demo.aspect;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import me.demo.annotation.LimitRequest;
import me.demo.exception.CommonException;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
@Aspect
@Slf4j
public class LimitRequestAspect {

  private static final Map<String, Cache<String, Integer>> RECORDER = new HashMap<>();

  // 定义切点 让所有有@LimitRequest注解的方法都执行切面方法
  @Pointcut("execution( public * me.demo.controller..*.*(..)) && @annotation(limitRequest)")
  public void excuseService(LimitRequest limitRequest) {}

  @Before(value = "excuseService(limitRequest)", argNames = "limitRequest")
  public void preCheck(LimitRequest limitRequest) throws Throwable {
    log.info("Start preCheck");
    // 获得请求
    ServletRequestAttributes attributes =
        (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    HttpServletRequest request = attributes.getRequest();

    HttpServletResponse response = attributes.getResponse();

    Cache<String, Integer> ipCounter = RECORDER.get(request.getRequestURI());
    if (ipCounter == null) {
      ipCounter =
          CacheBuilder.newBuilder()
              .maximumSize(limitRequest.count())
              .expireAfterWrite(limitRequest.time(), TimeUnit.MILLISECONDS)
              .build();
    }
    int count = ipCounter.get(request.getRemoteAddr(), () -> 0);
    // 超过次数，不执行目标方法
    if (count >= limitRequest.count()) {
      response.addIntHeader("X-LIMIT-REQUEST", count);
      throw new CommonException("Request access too fast, out of limit.");
    }
    // 未超过次数，记录加一
    ipCounter.put(request.getRemoteAddr(), count + 1);
    RECORDER.put(request.getRequestURI(), ipCounter);
    log.info("cache is {}", ipCounter.asMap());
  }
}
