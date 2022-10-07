package me.demo.utils;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * 命令行工具
 *
 * @since 2022/09/04
 **/
@Component
@Slf4j
public class CommandUtils {

  static ProcessBuilder processBuilder;

  static {
    processBuilder = new ProcessBuilder();
    processBuilder.redirectErrorStream(true);
  }

  public static Map<String, String> getEnvironmentVariables() {
    return processBuilder.environment();
  }

  public static void directory(String path) {
    processBuilder.directory(FileUtils.getFile(path));
  }

  public static void environment(Map<String, String> environment) {
    environment.entrySet().removeIf(pair -> ObjectUtils.isNotEmpty(pair.getValue()));
    getEnvironmentVariables().putAll(environment);
  }

  public static String execute(String... commands) {
    processBuilder.command(commands);
    log.info("Execution commands: {}", processBuilder.command());
    String result = StringUtils.EMPTY;
    try {
      Process process = processBuilder.start();
      result = IOUtils.toString(process.getInputStream(), StandardCharsets.UTF_8);
      log.info("Process result is: {}", result);
      log.info("Exit code is {}", process.waitFor());
    } catch (Exception e) {
      log.error("Execution failed", e);
    }
    return result;
  }

}
