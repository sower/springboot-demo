package me.demo;

import static org.assertj.core.api.Assertions.assertThat;

import me.demo.utils.JacksonUtils;
import org.junit.Test;

public class UtilsTest {

  @Test
  public void JacsonTest() {
    assertThat(JacksonUtils.isJsonString("{}")).isTrue();
    assertThat(JacksonUtils.isJsonString("adc")).isFalse();
  }
}
