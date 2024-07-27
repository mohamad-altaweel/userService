package com.example.realEstateService.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.example.realEstateService.exception.PropertyNotFoundException;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.validation.ObjectError;
import java.util.List;
import java.util.stream.Collectors;


@SuppressWarnings("preview")
@ControllerAdvice
public class PropertyControllerAdvice {

    @ExceptionHandler(PropertyNotFoundException.class)
    public ResponseEntity<Object> handleUserNotFoundException(PropertyNotFoundException exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<List<String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
    List<String> errors = ex.getBindingResult()
                             .getAllErrors()
                             .stream()
                             .map(ObjectError::getDefaultMessage)
                             .collect(Collectors.toList());
    return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
}

    // Other exception handlers can be added here
}
