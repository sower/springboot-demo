package me.demo.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

/**
 * @date 2022/09/04
 **/
@Component
@Data
@ConfigurationProperties(prefix = "person")
public class Person {

  private String name;
  private Integer age;
  private Boolean isLived;
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  @JsonFormat(pattern = "yyyy-MM-dd")
  private LocalDate birth;
  private Map<String, Object> info;
  private List<Object> hobbies;
  private Pet pet;

  @Data
  public static class Pet {

    private String name;
    private String species;
  }
}
