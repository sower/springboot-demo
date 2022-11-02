package me.demo.aspect;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;
import org.springframework.core.annotation.AliasFor;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RateLimit {

  int NOT_LIMITED = 0;

  /**
   * qps
   */
  @AliasFor("qps") double value() default NOT_LIMITED;

  /**
   * qps
   */
  @AliasFor("value") double qps() default NOT_LIMITED;

  /**
   * 超时时长
   */
  int timeout() default 1;

  /**
   * 超时时间单位
   */
  TimeUnit timeUnit() default TimeUnit.SECONDS;
}
