package me.demo.Exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @date 2022/09/18
 **/
@Getter
@Setter
@NoArgsConstructor
public class CommonException extends RuntimeException {

  private static final long serialVersionUID = 1;

  private String errorCode;

  private String errorMessage;

  public CommonException(String errorMessage) {
    this(null, errorMessage);
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
