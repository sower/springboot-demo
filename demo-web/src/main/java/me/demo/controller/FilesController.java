package me.demo.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import me.demo.exception.CommonException;
import me.demo.utils.Result;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传与下载
 *
 * @date 2022/09/21
 */
@RestController
@Slf4j
public class FilesController {

  // @RequestParam("file") 将name=file控件得到的文件封装成CommonsMultipartFile 对象
  @PostMapping("/upload")
  public Result<?> upload(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
    String filePath = getPath(request);
    try {
      saveFile(file, filePath);
    } catch (Exception e) {
      return Result.failed(e.getMessage());
    }
    return Result.success();
  }

  @PostMapping("/multiUpload")
  public Result<?> multiUpload(
      @RequestParam("files") MultipartFile[] files, HttpServletRequest request) {
    String filePath = getPath(request);

    try {
      for (MultipartFile f : files) {
        saveFile(f, filePath);
      }
    } catch (Exception e) {
      return Result.failed(e.getMessage());
    }
    return Result.success();
  }

  private String getPath(HttpServletRequest request) {
    return request.getServletContext().getRealPath("/upload");
  }

  private void saveFile(MultipartFile file, String filePath) {
    if (file.isEmpty()) {
      throw new CommonException("001", "Empty file");
    }
    String filename = file.getOriginalFilename(); // 获取上传文件原来的名称
    File dir = FileUtils.getFile(filePath);
    if (!dir.exists() && !dir.mkdirs()) {
      log.error("Failed to create directory {}", dir.getName());
      throw new CommonException("002", "Failed to create directory");
    }

    File localFile = new File(filePath + filename);
    try {
      file.transferTo(localFile); // 把上传的文件保存至本地
      log.info("{} 上传成功", file.getOriginalFilename());
    } catch (IOException e) {
      log.info("Failed to save file {}", file.getOriginalFilename());
      throw new CommonException("003", "Failed to save file");
    }
  }

  @RequestMapping(value = "/download")
  public Result<?> downloads(HttpServletResponse response, HttpServletRequest request)
      throws Exception {
    // 下载的地址
    String path = request.getServletContext().getRealPath("/upload");
    String fileName = "image.jpg";

    // 1、设置response 响应头
    response.reset(); // 设置页面不缓存,清空buffer
    response.setCharacterEncoding("UTF-8"); // 字符编码
    response.setContentType("multipart/form-data"); // 二进制传输数据
    // 设置响应头
    response.setHeader(
        "Content-Disposition",
        "attachment;fileName=" + URLEncoder.encode(fileName, StandardCharsets.UTF_8));

    File file = new File(path, fileName);
    try (InputStream input = new FileInputStream(file);
        OutputStream out = response.getOutputStream()) {
      IOUtils.copy(input, out);
    } catch (Exception e) {
      return Result.failed("Failed to download file");
    }
    return Result.success();
  }
}
