package com.wolox.training.exceptions;

public class UserNotFoundException extends Exception{

  private int code;

  public UserNotFoundException(String message, int code) {
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
