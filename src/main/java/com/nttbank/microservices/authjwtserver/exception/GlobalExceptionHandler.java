package com.nttbank.microservices.authjwtserver.exception;

import java.util.HashMap;
import java.util.Map;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.mongodb.MongoException;

@ControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(DuplicateKeyException.class)
  public ResponseEntity<String> handleDuplicateKeyException(DuplicateKeyException ex,
      WebRequest request) {
    return new ResponseEntity<>("Duplicate key error: " + ex.getMessage(), HttpStatus.CONFLICT);
  }

  @ExceptionHandler(MongoException.class)
  public ResponseEntity<String> handleMongoException(MongoException ex, WebRequest request) {
    return new ResponseEntity<>("MongoDB error: " + ex.getMessage(),
        HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<String> handleGeneralException(Exception ex, WebRequest request) {
    return new ResponseEntity<>("An error occurred: " + ex.getMessage(),
        HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler({BadCredentialsException.class, UsernameNotFoundException.class})
  public ResponseEntity<Map<String, Object>> handleAuthenticationExceptions(Exception ex) {
    Map<String, Object> response = new HashMap<>();
    response.put("status", HttpStatus.UNAUTHORIZED.value());
    response.put("error", "Authentication failed");
    response.put("message", ex.getMessage());
    response.put("code",
        ex instanceof BadCredentialsException ? "BAD_CREDENTIALS" : "USER_NOT_FOUND");
    return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler({UsernameAlreadyExistsException.class})
  public ResponseEntity<Map<String, Object>> handleUsernameAlreadyExist(Exception ex) {
    Map<String, Object> response = new HashMap<>();
    response.put("status", HttpStatus.CONFLICT.value());
    response.put("error", "Registration failed");
    response.put("message", ex.getMessage());
    return new ResponseEntity<>(response, HttpStatus.CONFLICT);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, Object>> handleValidationExceptions(
      MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();
    for (FieldError error : ex.getBindingResult().getFieldErrors()) {
      errors.put(error.getField(), error.getDefaultMessage());
    }

    Map<String, Object> response = new HashMap<>();
    response.put("status", HttpStatus.BAD_REQUEST.value());
    response.put("error", "Validation Failed");
    response.put("errors", errors);

    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
  }

}