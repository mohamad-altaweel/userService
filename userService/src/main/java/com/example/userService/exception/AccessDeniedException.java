package com.example.userService.exception;

public class AccessDeniedException extends RuntimeException{
    
  public AccessDeniedException(String message) {
    super(message);
  }
}
