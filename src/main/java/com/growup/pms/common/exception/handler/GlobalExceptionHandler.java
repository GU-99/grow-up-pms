package com.growup.pms.common.exception.handler;

import com.growup.pms.common.exception.code.ErrorCode;
import com.growup.pms.common.exception.dto.ErrorResponse;
import com.growup.pms.common.exception.exceptions.AuthenticationException;
import com.growup.pms.common.exception.exceptions.AuthorizationException;
import com.growup.pms.common.exception.exceptions.BusinessException;
import com.growup.pms.common.exception.exceptions.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final String LOG_MESSAGE_FORMAT = "[{}] ({} {}) {}";

    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<ErrorResponse> handleEntityNotFoundException(EntityNotFoundException entityNotFoundException, HttpServletRequest request) {
        logInfo(entityNotFoundException, request);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.of(entityNotFoundException.getErrorCode()));
    }

    @ExceptionHandler(AuthenticationException.class)
    protected ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException authenticationException, HttpServletRequest request) {
        logInfo(authenticationException, request);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ErrorResponse.of(authenticationException.getErrorCode()));
    }

    @ExceptionHandler(AuthorizationException.class)
    protected ResponseEntity<ErrorResponse> handleAuthorizationException(AuthorizationException authorizationException, HttpServletRequest request) {
        logInfo(authorizationException, request);
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ErrorResponse.of(authorizationException.getErrorCode()));
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handleException(Exception exception, HttpServletRequest request) {
        logError(exception, request);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR));
    }

    private void logInfo(BusinessException ex, HttpServletRequest request) {
        log.info(LOG_MESSAGE_FORMAT, "INFO", request.getMethod(), request.getRequestURI(), ex.getMessage());
    }

    private void logError(Exception ex, HttpServletRequest request) {
        log.error(LOG_MESSAGE_FORMAT, "ERROR", request.getMethod(), request.getRequestURI(), ex.getMessage(), ex);
    }
}
