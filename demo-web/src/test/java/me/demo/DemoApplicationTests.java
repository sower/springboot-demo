package me.demo;

import me.demo.bean.Person;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

// @RunWith(SpringRunner.class)
// @ActiveProfiles("local")
@SpringBootTest(
    classes = DemoApplication.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DemoApplicationTests {

  @Autowired Person person;

  @Test
  void contextLoads() {}

  @Test
  public void testJwtProperties() {
    System.out.println(person);
  }
}
