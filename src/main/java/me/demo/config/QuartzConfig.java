package me.demo.config;

import me.demo.task.DemoJob;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @description
 * @date 2022/10/16
 **/
@Configuration
public class QuartzConfig {

  @Bean
  public JobDetail jobDetail1() {
    return JobBuilder.newJob(DemoJob.class)
        .withIdentity("demo 01")
        .withDescription("demo job test") // 任务描述
        .storeDurably() // 每次任务执行后进行存储
        .build();
  }

  @Bean
  public Trigger trigger1() {
    // 简单的调度计划的构造器
    SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
        .withIntervalInSeconds(5) // 频率
        .repeatForever(); // 次数

    return TriggerBuilder.newTrigger()
        .forJob(jobDetail1()) // 绑定工作任务
        .withIdentity("demo 01 Trigger")
        .withSchedule(scheduleBuilder)
        .build();
  }
}
