package com.wolox.training.exceptions;

public class BookAlreadyOwnedException extends Exception{

  private int code;

  public BookAlreadyOwnedException(String message, Throwable cause) {
    super(message, cause);
  }

  public BookAlreadyOwnedException(String message, int code) {
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
