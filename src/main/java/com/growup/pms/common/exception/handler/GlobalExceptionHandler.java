package com.growup.pms.common.exception.handler;

import com.growup.pms.common.exception.code.ErrorCode;
import com.growup.pms.common.exception.dto.ErrorResponse;
import com.growup.pms.common.exception.exceptions.AuthenticationException;
import com.growup.pms.common.exception.exceptions.AuthorizationException;
import com.growup.pms.common.exception.exceptions.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({EntityNotFoundException.class})
    protected ResponseEntity<ErrorResponse> handleEntityNotFoundException(EntityNotFoundException entityNotFoundException) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(entityNotFoundException.getErrorResponse());
    }

    @ExceptionHandler({AuthenticationException.class})
    protected ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException authenticationException) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(authenticationException.getErrorResponse());
    }

    @ExceptionHandler({AuthorizationException.class})
    protected ResponseEntity<ErrorResponse> handleAuthorizationException(AuthorizationException authorizationException) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(authorizationException.getErrorResponse());
    }

    /**`
     * Exception handler for handling general exceptions.
     *
     * @param  exception   the exception to handle
     * @return             the response entity with the error response
     */
    @ExceptionHandler({Exception.class})
    protected ResponseEntity<ErrorResponse> handleException(Exception exception) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR));
    }

}
