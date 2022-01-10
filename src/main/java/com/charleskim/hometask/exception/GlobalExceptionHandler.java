package com.charleskim.hometask.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * Handles exceptions where the status code, headers, and body are unknown.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleUncaughtException(Exception exception) {
        System.out.printf("An unknown error occurred.", exception);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(exception.getMessage());
    } 
}
