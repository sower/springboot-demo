package me.demo.utils;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class SpringContextHolder implements ApplicationContextAware, DisposableBean {

  private static ApplicationContext applicationContext = null;


  @Override
  public void setApplicationContext(@NotNull ApplicationContext applicationContext)
      throws BeansException {
    SpringContextHolder.applicationContext = applicationContext;
  }

  @Override
  public void destroy() {
    SpringContextHolder.clearHolder();
  }

  /**
   * 取得存储在静态变量中的ApplicationContext.
   */
  public static ApplicationContext getApplicationContext() {
    assertContextInjected();
    return applicationContext;
  }

  /**
   * 从静态变量applicationContext中取得Bean, 自动转型为所赋值对象的类型.
   */
  public static Object getBean(String name) {
    assertContextInjected();
    return applicationContext.getBean(name);
  }

  /**
   * 从静态变量applicationContext中取得Bean, 自动转型为所赋值对象的类型.
   */
  public static <T> T getBean(Class<T> requiredType) {
    assertContextInjected();
    return applicationContext.getBean(requiredType);
  }

  /**
   * 获取SpringBoot 配置信息
   *
   * @param property     属性key
   * @param defaultValue 默认值
   * @param requiredType 返回类型
   * @return /
   */
  public static <T> T getProperties(String property, T defaultValue, Class<T> requiredType) {
    T result = defaultValue;
    try {
      result = getBean(Environment.class).getProperty(property, requiredType);
    } catch (Exception ignored) {
    }
    return result;
  }

  /**
   * 获取SpringBoot 配置信息
   *
   * @param property 属性key
   * @return /
   */
  public static String getProperties(String property) {
    return getProperties(property, null, String.class);
  }

  /**
   * 获取SpringBoot 配置信息
   *
   * @param property     属性key
   * @param requiredType 返回类型
   * @return /
   */
  public static <T> T getProperties(String property, Class<T> requiredType) {
    return getProperties(property, null, requiredType);
  }

  /**
   * 检查ApplicationContext不为空.
   */
  private static void assertContextInjected() {
    if (applicationContext == null) {
      throw new IllegalStateException("applicaitonContext属性未注入");
    }
  }

  /**
   * 清除SpringContextHolder中的ApplicationContext为Null.
   */
  public static void clearHolder() {
    log.debug("清除SpringContextHolder中的ApplicationContext:"
        + applicationContext);
    applicationContext = null;
  }

}
