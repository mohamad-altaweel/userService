package com.example.realEstateService.exception;


public class TokenInvalid extends RuntimeException {
    
    public TokenInvalid(String message){
        super(message);
    }
}
