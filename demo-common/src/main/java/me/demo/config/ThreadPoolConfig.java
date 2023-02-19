package me.demo.config;

import java.util.concurrent.ThreadPoolExecutor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * 线程池配置
 *
 * @date 2023/02/19
 */
@EnableAsync
@Configuration
public class ThreadPoolConfig {
  private int corePoolSize = Runtime.getRuntime().availableProcessors() + 1;
  private int maxPoolSize = corePoolSize * 2;
  private static final int queueCapacity = 50;
  private static final int keepAliveSeconds = 30;

  @Bean(name = "asyncTaskExecutor")
  public ThreadPoolTaskExecutor threadPoolTaskExecutor() {
    ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
    executor.setMaxPoolSize(maxPoolSize);
    executor.setCorePoolSize(corePoolSize);
    executor.setQueueCapacity(queueCapacity);
    executor.setKeepAliveSeconds(keepAliveSeconds);
    executor.setTaskDecorator(new MdcTaskDecorator());
    executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
    return executor;
  }
}
