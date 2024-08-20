package com.growup.pms.common.exception.handler;

import com.growup.pms.common.exception.code.ErrorCode;
import com.growup.pms.common.exception.dto.ErrorResponse;
import com.growup.pms.common.exception.exceptions.AuthenticationException;
import com.growup.pms.common.exception.exceptions.AuthorizationException;
import com.growup.pms.common.exception.exceptions.BusinessException;
import com.growup.pms.common.exception.exceptions.DuplicateException;
import com.growup.pms.common.exception.exceptions.EntityNotFoundException;
import com.growup.pms.common.exception.exceptions.InvalidInputException;
import com.growup.pms.common.exception.exceptions.MessageFailureException;
import com.growup.pms.common.exception.exceptions.StorageException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final String LOG_MESSAGE_FORMAT = "[{}] ({} {}) {}";

    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<ErrorResponse> handleNotFoundException(BusinessException ex, HttpServletRequest request) {
        logInfo(ex, request);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ErrorResponse.of(ex.getErrorCode()));
    }

    @ExceptionHandler({DuplicateException.class, MessageFailureException.class, InvalidInputException.class})
    protected ResponseEntity<ErrorResponse> handleBadRequestException(BusinessException ex, HttpServletRequest request) {
        logInfo(ex, request);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.of(ex.getErrorCode()));
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class, MethodArgumentNotValidException.class})
    public ResponseEntity<ErrorResponse> handleDataFormatException(Exception ex, HttpServletRequest request) {
        logInfo(ex, request);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.of(ErrorCode.DATA_FORMAT_INVALID));
    }

    @ExceptionHandler(AuthenticationException.class)
    protected ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException ex, HttpServletRequest request) {
        logInfo(ex, request);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ErrorResponse.of(ex.getErrorCode()));
    }

    @ExceptionHandler(AuthorizationException.class)
    protected ResponseEntity<ErrorResponse> handleAuthorizationException(AuthorizationException ex, HttpServletRequest request) {
        logInfo(ex, request);
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ErrorResponse.of(ex.getErrorCode()));
    }

    @ExceptionHandler(StorageException.class)
    protected ResponseEntity<ErrorResponse> handleStorageException(StorageException ex, HttpServletRequest request) {
        logInfo(ex, request);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.of(ex.getErrorCode()));
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handleException(Exception ex, HttpServletRequest request) {
        logError(ex, request);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR));
    }

    private void logInfo(Exception ex, HttpServletRequest request) {
        log.info(LOG_MESSAGE_FORMAT, "INFO", request.getMethod(), request.getRequestURI(), ex.getMessage());
    }

    private void logError(Exception ex, HttpServletRequest request) {
        log.error(LOG_MESSAGE_FORMAT, "ERROR", request.getMethod(), request.getRequestURI(), ex.getMessage(), ex);
    }
}
