package me.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class DemoApplication {

  public static void main(String[] args) {
    try {
      SpringApplication.run(DemoApplication.class, args);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
