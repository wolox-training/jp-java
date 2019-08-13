package com.wolox.training.exceptions;

public class UserNotFoundException extends Exception{

  private int code;

  public UserNotFoundException(String message, int code) {
    super(message);
    this.code = code;
  }
}
