package me.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;


@SpringBootTest
class DemoApplicationTests {

//  @Autowired
//  Person person;

  @Test
  public void abc() {
    Assert.hasLength("avc", "NO");
  }

}
