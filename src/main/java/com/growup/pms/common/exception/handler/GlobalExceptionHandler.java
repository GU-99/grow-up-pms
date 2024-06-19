package com.growup.pms.common.exception.handler;

import com.growup.pms.common.exception.dto.ErrorDto;
import com.growup.pms.common.exception.exceptions.AuthenticationException;
import com.growup.pms.common.exception.exceptions.AuthorizationException;
import com.growup.pms.common.exception.exceptions.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({AuthenticationException.class})
    protected ResponseEntity<ErrorDto> handleAuthenticationException(AuthenticationException authenticationException) {
        return ResponseEntity
                .status(authenticationException.getErrorCode().getHttpStatus())
                .body(authenticationException.getErrorDto());
    }

    @ExceptionHandler({AuthorizationException.class})
    protected ResponseEntity<ErrorDto> handleAuthorizationException(AuthorizationException authorizationException) {
        return ResponseEntity
                .status(authorizationException.getErrorCode().getHttpStatus())
                .body(authorizationException.getErrorDto());
    }

    @ExceptionHandler({EntityNotFoundException.class})
    protected ResponseEntity<ErrorDto> handleEntityNotFoundException(EntityNotFoundException entityNotFoundException) {
        return ResponseEntity
                .status(entityNotFoundException.getErrorCode().getHttpStatus())
                .body(entityNotFoundException.getErrorDto());
    }

}
