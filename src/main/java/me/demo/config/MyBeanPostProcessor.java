package me.demo.config;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.Ordered;

/**
 * 后置处理器
 *
 * @date 2022/10/23
 **/
@Slf4j
public class MyBeanPostProcessor implements BeanPostProcessor, Ordered {

  /**
   * 实例化、依赖注入完毕，在调用显示的初始化之前完成一些定制的初始化任务 注意：方法返回值不能为null 如果返回null那么在后续初始化方法将报空指针异常或者通过getBean()方法获取不到bena实例对象
   * 因为后置处理器从Spring IoC容器中取出bean实例对象没有再次放回IoC容器中
   */
  @Override
  public Object postProcessBeforeInitialization(@NotNull Object bean, @NotNull String beanName)
      throws BeansException {
    log.info("A before--实例化的bean对象:" + bean + "\t" + beanName);
    // 可以根据beanName不同执行不同的处理操作
    return bean;
  }

  /**
   * 实例化、依赖注入、初始化完毕时执行 注意：方法返回值不能为null 如果返回null那么在后续初始化方法将报空指针异常或者通过getBean()方法获取不到bena实例对象
   * 因为后置处理器从Spring IoC容器中取出bean实例对象没有再次放回IoC容器中
   */
  @Override
  public Object postProcessAfterInitialization(@NotNull Object bean, @NotNull String beanName)
      throws BeansException {
    log.info("A after...实例化的bean对象:" + bean + "\t" + beanName);
    // 可以根据beanName不同执行不同的处理操作
    return bean;
  }

  @Override
  public int getOrder() {
    return 0;
  }
}
