package com.wolox.training.exceptions;

public class IllegalArgumentBookException extends Exception {

  private int code;

  public IllegalArgumentBookException(String message, int code) {
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
