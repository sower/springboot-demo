package me.demo.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 公共异常
 *
 * @date 2022/09/18
 */
@Getter
@Setter
@NoArgsConstructor
public class CommonException extends RuntimeException {

  private static final long serialVersionUID = 1;

  private static final String SYS_ERROR = "SYS_ERROR";

  private String errorCode;

  private String errorMessage;

  public CommonException(String errorMessage) {
    this(SYS_ERROR, errorMessage);
  }

  public CommonException(String errorCode, String errorMessage) {
    super(errorMessage);
    this.errorCode = errorCode;
    this.errorMessage = errorMessage;
  }

  public CommonException(String errorCode, String errorMessage, Throwable cause) {
    super(errorMessage, cause);
    this.errorCode = errorCode;
    this.errorMessage = errorMessage;
  }
}
