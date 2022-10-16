package me.demo.utils;

import com.google.common.collect.ImmutableMap;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * @description
 * @date 2022/10/12
 **/
public class UrlUtils {

  private static final PathMatcher MATCHER = new AntPathMatcher();

  private static void match(PathMatcher matcher, String pattern, String reqPath) {
    boolean match = matcher.match(pattern, reqPath);
    System.out.println(pattern + "\t" + (match ? "【成功】" : "【失败】") + "\t" + reqPath);
  }

  private static void extractUriTemplateVariables(PathMatcher matcher, String pattern,
      String reqPath) {
    Map<String, String> variablesMap = matcher.extractUriTemplateVariables(pattern, reqPath);
    System.out.println(variablesMap + "\t" + pattern + "\t" + reqPath);
  }

  private static void uriComponents() {
    UriComponents uriComponents = UriComponentsBuilder.fromHttpUrl("https://www.bing.cn")
        .path("search/{content}")
        .queryParam("q", "spring")
        .uriVariables(ImmutableMap.of("content", "abc"))
        .build().encode(StandardCharsets.UTF_8);
    System.out.println(uriComponents);

  }

  public static void main(String[] args) {
    extractUriTemplateVariables(MATCHER, "**/{a}/", "https://sdas.ds/sdsa/dsadsa/");
    extractUriTemplateVariables(MATCHER, "**/{a}/", "https://sdas.ds/sdsa/dsadsa");
    extractUriTemplateVariables(MATCHER, "**/{a}/", "/dsadsa/");
  }

}
