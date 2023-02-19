package me.demo.config;

import java.io.IOException;
import java.util.UUID;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import me.demo.constant.Constants;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

/**
 * 设置请求traceId过滤器
 *
 * @date 2023/02/18
 */
@Slf4j
@WebFilter(filterName = "traceIdFilter", urlPatterns = "/*")
@Component
public class TraceIdFilter implements Filter {

  @Override
  public void init(FilterConfig filterConfig) {
    log.info("init {} , config: {}", this.getClass().getName(), filterConfig);
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
      throws IOException, ServletException {
    String traceId = ((HttpServletRequest) request).getHeader(Constants.TRACE_ID);
    if (StringUtils.isBlank(traceId)) {
      traceId = UUID.randomUUID().toString();
    }
    MDC.put(Constants.TRACE_ID, traceId);
    log.debug("Create new {} - {}", Constants.TRACE_ID, traceId);
    filterChain.doFilter(request, response);
  }

  @Override
  public void destroy() {
    MDC.clear();
  }
}
