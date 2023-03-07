package me.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = "me.demo")
@EnableCaching
@EnableFeignClients
public class DemoApplication {

  public static void main(String[] args) {
    try {
      SpringApplication.run(DemoApplication.class, args);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
