package me.demo.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * 初始化任务
 *
 * @date 2023/02/27
 */
@Slf4j
@Component
public class InitTask implements CommandLineRunner {

  @Override
  public void run(String... args) {
    log.info("=== Init task done ===");
  }
}
