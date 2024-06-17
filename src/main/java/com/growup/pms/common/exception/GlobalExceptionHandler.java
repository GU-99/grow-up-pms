package com.growup.pms.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({AuthenticationException.class})
    protected ResponseEntity<?> handleAuthenticationException(AuthenticationException authenticationException) {
        return new ResponseEntity<>(authenticationException, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({AuthorizationException.class})
    protected ResponseEntity<?> handleAuthorizationException(AuthorizationException authorizationException) {
        return new ResponseEntity<>(authorizationException, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({EntityNotFoundException.class})
    protected ResponseEntity<?> handleEntityNotFoundException(EntityNotFoundException entityNotFoundException) {
        return new ResponseEntity<>(entityNotFoundException, HttpStatus.BAD_REQUEST);
    }

}
