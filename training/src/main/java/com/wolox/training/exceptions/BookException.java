package com.wolox.training.exceptions;

public class BookException extends Exception{

  private int code;

  public BookException(String message, Throwable cause) {
    super(message, cause);
  }

  public BookException(String message, int code) {
    super(message);
    this.code = code;
  }

  public int getCode() {
    return code;
  }

  public void setCode(int code) {
    this.code = code;
  }
}
