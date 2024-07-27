package com.example.userService.exception;

@SuppressWarnings("preview")
public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String message) {
        super(message);
    }

    // Optionally, you can add more constructors or methods as needed
}
