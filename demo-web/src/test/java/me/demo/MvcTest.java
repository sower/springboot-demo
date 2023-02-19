package me.demo;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import me.demo.bean.Person;
import me.demo.controller.TestController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

/**
 * @description
 * @date 2023/02/13
 */
// @SpringBootTest
// @AutoConfigureMockMvc
@WebMvcTest(TestController.class)
public class MvcTest {
  @Autowired private MockMvc mvc;

  @MockBean private Person person;

  //  @Test
  //  void testWithMockMvc(@Autowired MockMvc mvc) throws Exception {
  //    mvc.perform(MockMvcRequestBuilders.get("/index")).andExpect(status().isOk());
  ////    .andExpect(content().string("Hello World"));
  //  }
  @Test
  void testExample() throws Exception {
    mvc.perform(MockMvcRequestBuilders.get("/demo/index").accept(MediaType.TEXT_PLAIN))
        .andExpect(status().isOk());
    //        .andExpect(content().string("Honda Civic"));
  }
}
