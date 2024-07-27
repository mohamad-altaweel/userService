package com.example.realEstateService.exception;

@SuppressWarnings("preview")
public class PropertyNotFoundException extends RuntimeException {

    public PropertyNotFoundException(String message) {
        super(message);
    }

    // Optionally, you can add more constructors or methods as needed
}
