package com.example.userservice.exception;

import com.example.userservice.dto.ErrorResponse;
import jakarta.validation.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(Exception ex) {
        ErrorResponse error = new ErrorResponse("An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(ValidationException ex) {
        ErrorResponse error = new ErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST.value());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
