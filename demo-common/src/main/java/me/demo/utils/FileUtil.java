package me.demo.utils;

import com.alibaba.fastjson.JSON;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

/**
 * 文件处理工具
 *
 * @date 2022/10/23
 */
@Slf4j
public class FileUtil {

  public static File initFileDirectory(String fileName) {
    File file = FileUtils.getFile(fileName);
    if (!file.exists() && !file.mkdirs()) {
      throw new RuntimeException("Failed to mkdir file");
    }
    return file;
  }

  @SneakyThrows
  public static void buildFile(String fileName, Object fileContent) {
    File file = initFileDirectory(fileName);
    if (fileContent instanceof byte[]) {
      FileUtils.writeByteArrayToFile(file, (byte[]) fileContent);
    } else if (fileContent instanceof InputStream) {
      FileUtils.copyToFile((InputStream) fileContent, file);
    } else if (fileContent instanceof URL) {
      FileUtils.copyURLToFile((URL) fileContent, file);
    } else if (fileContent instanceof String) {
      FileUtils.writeStringToFile(file, (String) fileContent, StandardCharsets.UTF_8);
    } else {
      FileUtils.writeStringToFile(file, JSON.toJSONString(fileContent), StandardCharsets.UTF_8);
    }
    log.info(
        "Build file {} successfully, size is {}",
        fileName,
        FileUtils.byteCountToDisplaySize(file.length()));
  }

  public String concat(String... element) {
    String path = String.join(File.separator, element);
    return FilenameUtils.normalize(path);
  }
}
