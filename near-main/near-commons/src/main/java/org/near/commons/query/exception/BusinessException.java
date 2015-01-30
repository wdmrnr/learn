package org.near.commons.query.exception;

public class BusinessException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public BusinessException(String message) {
    super(message);
  }

  public BusinessException(Throwable cause) {
    super(cause);
  }

  public BusinessException(String message, Throwable cause) {
    super(message, cause);
  }

  public BusinessException(String message, Object... args) {
    super(String.format(message, args));
  }

  public BusinessException(String message, Throwable cause, Object... args) {
    super(String.format(message, args), cause);
  }
}
