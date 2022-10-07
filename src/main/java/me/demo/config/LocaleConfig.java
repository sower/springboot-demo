package me.demo.config;

import java.util.Locale;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

@Configuration
public class LocaleConfig {

  /**
   * 默认解析器 其中locale表示默认语言
   */
  @Bean
  public LocaleResolver localeResolver() {
    SessionLocaleResolver localeResolver = new SessionLocaleResolver();
    localeResolver.setDefaultLocale(Locale.CHINA);
    return localeResolver;
  }
}

//  @Override
//  public Locale resolveLocale(HttpServletRequest request) {
//    //获取默认的区域信息解析器
//    Locale locale = Locale.getDefault();
//
//    for (Cookie cookie : request.getCookies()) {
//      if (cookie.getName().equals("lang")) {
//        String[] s = cookie.getValue().split("_");
//        locale = new Locale(s[0], s[1]);
//        break;
//      }
//    }
//    return locale;
//  }

