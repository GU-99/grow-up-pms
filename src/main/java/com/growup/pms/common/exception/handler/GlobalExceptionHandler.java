package com.growup.pms.common.exception.handler;

import com.growup.pms.GrowUpPmsApplication;
import com.growup.pms.common.exception.code.ErrorCode;
import com.growup.pms.common.exception.dto.ErrorResponse;
import com.growup.pms.common.exception.exceptions.BusinessException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestCookieException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice(basePackageClasses = GrowUpPmsApplication.class)
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<ErrorResponse> handleBusinessException(BusinessException ex, HttpServletRequest request) {
        LogFormatter.info(ex, request);
        return ResponseEntity.status(ex.getErrorCode().getStatus())
                .body(ErrorResponse.of(ex.getErrorCode()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        ErrorResponse response = ErrorResponse.of(ErrorCode.INVALID_DATA_FORMAT, ex.getBindingResult());
        LogFormatter.info(ex, request);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex, HttpServletRequest request) {
        ErrorResponse response = ErrorResponse.of(ex);
        LogFormatter.info(ex, request);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingRequestCookieException.class)
    public ResponseEntity<ErrorResponse> handleMissingRequestCookieException(MissingRequestCookieException ex, HttpServletRequest request) {
        ErrorResponse response = ErrorResponse.of(ex);
        LogFormatter.info(ex, request);
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<String> handleException(Exception ex, HttpServletRequest request) {
        LogFormatter.error(ex, request);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ex.getMessage());
    }
}
