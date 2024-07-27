package com.example.userService.exception;


public class TokenInvalid extends RuntimeException {
    
    public TokenInvalid(String message){
        super(message);
    }
}
