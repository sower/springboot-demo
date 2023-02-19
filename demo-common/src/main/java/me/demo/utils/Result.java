package me.demo.utils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

/**
 * @date 2022/09/18
 */
@Data
public class Result<T> {

  static final String MSG_SUCCESS = "success";
  static final String MSG_FAILED = "failed";

  static final int CODE_SUCCESS = 1;
  static final int CODE_FAILED = 0;

  /** 结果码 */
  private int code;
  //  结果消息
  private String msg;
  // 数据体
  private T data;

  public static <T> Result<T> success(T data) {
    return new Result<>(CODE_SUCCESS, MessageUtils.getMessage(MSG_SUCCESS), data);
  }

  public static <T> Result<T> success() {
    return Result.success(null);
  }

  public static <T> Result<T> failed(String msg) {
    return new Result<>(CODE_FAILED, msg, null);
  }

  public static <T> Result<T> failed() {
    return Result.failed(MessageUtils.getMessage(MSG_FAILED));
  }

  @JsonIgnore
  public boolean isSuccess() {
    return this.code == 1;
  }

  private Result(int code, String msg, T data) {
    this.code = code;
    this.msg = msg;
    this.data = data;
  }
}
