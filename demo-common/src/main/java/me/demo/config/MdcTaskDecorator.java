package me.demo.config;

import java.util.Map;
import java.util.UUID;
import me.demo.constant.Constants;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.core.task.TaskDecorator;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

/**
 * MDC任务装饰器
 *
 * @date 2023/02/19
 */
public class MdcTaskDecorator implements TaskDecorator {
  @Override
  public Runnable decorate(Runnable runnable) {
    Map<String, String> map = MDC.getCopyOfContextMap();
    RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
    return () -> {
      try {
        MDC.setContextMap(map);
        RequestContextHolder.setRequestAttributes(attributes);
        String traceId = MDC.get(Constants.TRACE_ID);
        if (StringUtils.isBlank(traceId)) {
          traceId = UUID.randomUUID().toString();
          MDC.put(Constants.TRACE_ID, traceId);
        }
        runnable.run();
      } finally {
        MDC.clear();
        RequestContextHolder.resetRequestAttributes();
      }
    };
  }
}
