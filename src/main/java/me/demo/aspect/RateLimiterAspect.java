package me.demo.aspect;

import com.google.common.collect.Maps;
import com.google.common.util.concurrent.RateLimiter;
import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentMap;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

/**
 * 限流切面
 *
 * @date 2022/10/23
 **/
@Slf4j
@Aspect
@Component
public class RateLimiterAspect {

  private static final ConcurrentMap<String, RateLimiter> RATE_LIMITER_CACHE = Maps.newConcurrentMap();

  @Pointcut("@annotation(me.demo.aspect.RateLimit)")
  public void rateLimit() {
  }

  @Around("rateLimit()")
  public Object pointcut(ProceedingJoinPoint point) throws Throwable {
    MethodSignature signature = (MethodSignature) point.getSignature();
    Method method = signature.getMethod();
    // 通过 AnnotationUtils.findAnnotation 获取 RateLimiter 注解
    RateLimit rateLimit = AnnotationUtils.findAnnotation(method, RateLimit.class);
    if (rateLimit != null && rateLimit.qps() > RateLimit.NOT_LIMITED) {
      double qps = rateLimit.qps();
      String methodName = method.getName();
      if (RATE_LIMITER_CACHE.get(methodName) == null) {
        // 初始化 QPS
        RATE_LIMITER_CACHE.put(methodName, RateLimiter.create(qps));
      }
      RateLimiter rateLimiter = RATE_LIMITER_CACHE.get(methodName);
      log.debug("{}的QPS设置为: {}", methodName, rateLimiter.getRate());
      // 尝试获取令牌
      if (!rateLimiter.tryAcquire(rateLimit.timeout(), rateLimit.timeUnit())) {
        throw new RuntimeException("请求太快，慢点重试");
      }
    }
    return point.proceed();
  }
}
