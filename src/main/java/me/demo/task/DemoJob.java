package me.demo.task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.quartz.JobExecutionContext;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * @description
 * @date 2022/10/16
 **/
@Slf4j
public class DemoJob extends QuartzJobBean {

  @Override
  protected void executeInternal(@NotNull JobExecutionContext jobExecutionContext) {
    String now = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now());
    log.info("当前的时间: " + now);
  }
}
