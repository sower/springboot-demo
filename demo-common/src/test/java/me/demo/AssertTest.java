package me.demo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.either;
import static org.hamcrest.Matchers.emptyArray;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.hasValue;
import static org.hamcrest.Matchers.in;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.startsWith;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.hamcrest.MatcherAssert;
import org.junit.Test;

/**
 * @description
 * @date 2023/02/13
 */
public class AssertTest {

  @Test
  public void assertjDemo() {
    assertThat("".isEmpty()).isTrue();

    assertThat(Arrays.asList("1", "2", "3"))
        .startsWith("1")
        .doesNotContainNull()
        .containsSequence("2", "3")
        .isNotEmpty();
  }

  /** 核心匹配. */
  @Test
  public void hamcrestDemo() {
    // allOf: 所有条件都必须满足，相当于&&
    MatcherAssert.assertThat("myName", allOf(startsWith("my"), containsString("Name")));
    // either: 两者之一
    MatcherAssert.assertThat("myName", either(equalToIgnoringCase("my")).or(endsWith("Name")));
    // everyItem: 每个元素都需满足特定条件
    MatcherAssert.assertThat(Arrays.asList("my", "mine"), everyItem(startsWith("m")));
    // hasItems: 包含多个元素
    MatcherAssert.assertThat(Arrays.asList("my", "mine", "your"), hasItems("your", "my"));
    // is: is(equalTo(x))或is(instanceOf(clazz.class))的简写
    MatcherAssert.assertThat("myName", is("myName"));
    // anything(): 任何情况下，都匹配正确
    MatcherAssert.assertThat("myName", anything());
    MatcherAssert.assertThat("123", is(notNullValue()));

    MatcherAssert.assertThat(2, greaterThanOrEqualTo(2));

    // emptyArray: 空数组
    MatcherAssert.assertThat(new String[0], emptyArray());

    MatcherAssert.assertThat("myName", in(Arrays.asList("myName", "yourName")));
    MatcherAssert.assertThat("myName", is(in(Arrays.asList("myName", "yourName"))));

    // Map匹配
    Map<String, String> myMap = new HashMap<>();
    myMap.put("Name", "john");
    // hasEntry: key && value匹配
    MatcherAssert.assertThat(myMap, hasEntry("Name", "john"));
    // hasKey: key匹配
    MatcherAssert.assertThat(myMap, hasKey(equalTo("Name")));
    // hasValue: value匹配
    MatcherAssert.assertThat(myMap, hasValue(equalTo("john")));
  }
}
