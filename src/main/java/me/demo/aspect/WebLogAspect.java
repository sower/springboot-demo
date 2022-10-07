package me.demo.aspect;

import com.alibaba.fastjson2.JSON;
import java.util.Objects;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * AOP
 *
 * @date 2022/09/12
 **/
@Component
@Aspect
@Slf4j
public class WebLogAspect {

  @Pointcut("execution( public * me.demo.controller..*.*(..))")
  public void aopPointCut() {
  }

  // 前置通知
  @Before("aopPointCut()")
  public void doBefore(JoinPoint joinPoint) {
    ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    Optional.ofNullable(requestAttributes).map(ServletRequestAttributes::getRequest)
        .ifPresent(request -> log.info("{} sends {} request {}", request.getRemoteAddr(),
            request.getMethod(),
            ServletUriComponentsBuilder.fromContextPath(request).encode().build()));
    log.info("Ready into {} - {} - {}", joinPoint.getTarget(),
        joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
    log.info("ARGS: {}", joinPoint.getArgs());
  }

  // 后置通知
  @AfterReturning(value = "aopPointCut()")
  public void doAfterReturning(JoinPoint joinPoint) {
    log.info("Exit from {} - {}", joinPoint.getTarget(),
        joinPoint.getSignature().getName());
  }

  // 环绕通知
  @Around("aopPointCut()")
  public Object doAround(ProceedingJoinPoint proceedingJoinPoint)
      throws Throwable {

    StopWatch stopWatch = new StopWatch(proceedingJoinPoint.toShortString());
    stopWatch.start();
    Object obj = null;
    try {
      // 执行当前目标方法
      obj = proceedingJoinPoint.proceed();
      return obj;
    } finally {

      stopWatch.stop();
      log.info("{} - {} complete, it takes {}ms", proceedingJoinPoint.getTarget(),
          proceedingJoinPoint.getSignature().getName()
          , stopWatch.getLastTaskTimeMillis());
      if (Objects.nonNull(obj)) {
        log.info("return result -> {}", StringUtils.abbreviate(JSON.toJSONString(obj), 1000));
      }
    }
  }

  // 异常通知
  @AfterThrowing(value = "aopPointCut()", throwing = "e")
  public void doAfterThrowing(JoinPoint joinPoint, Throwable e) {
    log.info("{} - {} execute error {}", joinPoint.getTarget(),
        joinPoint.getSignature().getName()
        , e.getMessage());
  }

  // 最终通知
  @After("aopPointCut()")
  public void doAfter() {
    log.info("最终通知");
  }
}
