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
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * AOP
 *
 * @date 2022/09/12
 */
@Component
@Aspect
@Slf4j
public class WebLogAspect {

  @Pointcut("execution( public * me.demo.controller..*.*(..))")
  public void requestLog() {}

  @Pointcut("@annotation(me.demo.annotation.LogInOutParam)")
  public void methodLog() {}

  /*前置通知*/
  @Before("requestLog() || methodLog()")
  public void doBefore(JoinPoint joinPoint) {
    ServletRequestAttributes requestAttributes =
        (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    Optional.ofNullable(requestAttributes)
        .map(ServletRequestAttributes::getRequest)
        .ifPresent(
            request ->
                log.info(
                    "Received {} - {} request {}",
                    request.getRemoteAddr(),
                    request.getMethod(),
                    ServletUriComponentsBuilder.fromContextPath(request).encode().build()));
    MethodSignature signature = (MethodSignature) joinPoint.getSignature();
    log.info(
        "Ready into {} . {} - ARGS: (name) {} - (value) {}",
        signature.getDeclaringTypeName(),
        signature.getName(),
        signature.getParameterNames(),
        joinPoint.getArgs());
  }

  // 后置通知
  @AfterReturning(value = "requestLog()")
  public void doAfterReturning(JoinPoint joinPoint) {
    log.info("Exit from {} - {}", joinPoint.getTarget(), joinPoint.getSignature().getName());
  }

  // 环绕通知
  @Around("requestLog() || methodLog()")
  public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {

    StopWatch stopWatch = new StopWatch(proceedingJoinPoint.toShortString());
    stopWatch.start();
    Object obj = null;
    try {
      // 执行当前目标方法
      obj = proceedingJoinPoint.proceed();
      return obj;
    } finally {

      stopWatch.stop();
      String methodName = proceedingJoinPoint.getSignature().getName();
      log.info(
          "{} - {} complete, it takes {}ms",
          proceedingJoinPoint.getTarget(),
          methodName,
          stopWatch.getLastTaskTimeMillis());
      if (Objects.nonNull(obj)) {
        log.info(
            "{} return result -> {}",
            methodName,
            StringUtils.abbreviate(JSON.toJSONString(obj), 1000));
      }
    }
  }

  // 最终通知
  @After("requestLog()")
  public void doAfter() {
    log.info("last notify");
  }

  // 异常通知
  @AfterThrowing(value = "requestLog()", throwing = "e")
  public void doAfterThrowing(JoinPoint joinPoint, Throwable e) {
    log.info(
        "{} - {} execute error {}",
        joinPoint.getTarget(),
        joinPoint.getSignature().getName(),
        e.getMessage());
  }
}
