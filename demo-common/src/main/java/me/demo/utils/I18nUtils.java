package me.demo.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 * i18 message
 *
 * @date 2022/10/07
 */
@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class I18nUtils {

  /**
   * 根据消息键和参数 获取消息 委托给spring messageSource
   *
   * @param code 消息键
   * @param args 参数
   * @return 获取国际化翻译值
   */
  public static String getMessage(String code, Object... args) {
    MessageSource messageSource = SpringContextHolder.getBean(MessageSource.class);
    return messageSource.getMessage(code, args, LocaleContextHolder.getLocale());
  }
}
