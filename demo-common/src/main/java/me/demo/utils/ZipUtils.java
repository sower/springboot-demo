package me.demo.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * Zip 解压缩工具
 *
 * @since 2022/09/06
 */
@Slf4j
public class ZipUtils {

  /**
   * @param srcPath 待压缩的文件或目录
   * @param targetFile 压缩后的文件
   */
  public static void zip(String srcPath, String targetFile) throws Exception {
    File zipFile = FileUtils.getFile(targetFile);
    File srcDir = FileUtils.getFile(srcPath);
    try (ZipArchiveOutputStream stream = new ZipArchiveOutputStream(zipFile)) {
      zipRecursive(stream, srcDir, StringUtils.EMPTY);
      stream.finish();
    }
  }

  // 递归压缩目录下的文件和目录
  private static void zipRecursive(ZipArchiveOutputStream zipStream, File srcFile, String basePath)
      throws IOException {
    String currentFile = basePath + srcFile.getName();
    if (srcFile.isDirectory()) {
      File[] files = srcFile.listFiles();
      if (ArrayUtils.isNotEmpty(files)) {
        for (File file : files) {
          zipRecursive(zipStream, file, currentFile + File.separator);
        }
        return;
      }
    }
    // 空目录 或 文件直接放入
    ZipArchiveEntry entry = new ZipArchiveEntry(srcFile, currentFile);
    zipStream.putArchiveEntry(entry);
    if (srcFile.isFile()) {
      FileUtils.copyFile(srcFile, zipStream);
    }
    zipStream.closeArchiveEntry();
  }

  /**
   * @param srcFile 待解压文件
   * @param targetPath 存放位置
   */
  public static void unZip(String srcFile, String targetPath) throws Exception {
    try (InputStream fileInputStream = new FileInputStream(srcFile);
        ZipArchiveInputStream archiveInputStream = new ZipArchiveInputStream(fileInputStream)) {
      ZipArchiveEntry entry;
      while ((entry = archiveInputStream.getNextZipEntry()) != null) {
        if (!archiveInputStream.canReadEntryData(entry)) {
          log.warn("{} skip", entry.getName());
          continue;
        }
        File file = FileUtils.getFile(targetPath, entry.getName());
        if (entry.isDirectory()) {
          log.info("Create directory {}", file.getCanonicalPath());
          if (!file.isDirectory() && !file.mkdirs()) {
            throw new IOException("failed to create directory " + file);
          }
        } else {
          FileUtils.copyToFile(archiveInputStream, file);
          if (!file.setLastModified(entry.getLastModifiedDate().getTime())) {
            log.warn("set {} file properties failed.", file.getName());
          }
        }
      }
    }
  }
}
