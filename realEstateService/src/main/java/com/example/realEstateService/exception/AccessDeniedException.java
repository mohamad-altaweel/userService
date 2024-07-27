package com.example.realEstateService.exception;

public class AccessDeniedException extends RuntimeException{
    
  public AccessDeniedException(String message) {
    super(message);
  }
}
