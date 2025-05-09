package com.vecanhac.ddd.application.exception;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(EmailAlreadyExistException.class)
    public ResponseEntity<FieldErrorResponse> handEmailExist(EmailAlreadyExistException ex) {
        return ResponseEntity
                .badRequest()
                .body(new FieldErrorResponse("email", ex.getMessage()));
    }


    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<FieldErrorResponse> handleUserNotFound(UserNotFoundException ex) {
        return ResponseEntity.badRequest().body(new FieldErrorResponse("email", ex.getMessage()));
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<FieldErrorResponse> handleInvalidPassword(InvalidPasswordException ex) {
        return ResponseEntity.badRequest().body(new FieldErrorResponse("password", ex.getMessage()));
    }

    @ExceptionHandler(OtpInvalidException.class)
    public ResponseEntity<FieldErrorResponse> handleInvalidOtp(OtpInvalidException ex) {
        return ResponseEntity.badRequest().body(new FieldErrorResponse("otp", ex.getMessage()));
    }


}
