package com.growup.pms.common.exception.dto;

import com.growup.pms.common.exception.code.ErrorCode;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.method.ParameterValidationResult;
import org.springframework.web.bind.MissingRequestCookieException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorResponse {
    private String code;
    private String message;
    private List<FieldError> errors;

    private ErrorResponse(ErrorCode errorCode) {
        this(errorCode, new ArrayList<>());
    }

    private ErrorResponse(ErrorCode errorCode, List<FieldError> errors) {
        this.code = errorCode.getCode();
        this.message = errorCode.getMessage();
        this.errors = errors;
    }

    public static ErrorResponse of(ErrorCode errorCode) {
        return new ErrorResponse(errorCode);
    }

    public static ErrorResponse of(ErrorCode errorCode, List<FieldError> fieldErrors) {
        return new ErrorResponse(errorCode, fieldErrors);
    }

    public static ErrorResponse of(ErrorCode errorCode, BindingResult bindingResult) {
        return new ErrorResponse(errorCode, FieldError.of(bindingResult));
    }

    public static ErrorResponse of(List<ParameterValidationResult> parameterValidationResults) {
        return new ErrorResponse(ErrorCode.INVALID_DATA_FORMAT, FieldError.of(parameterValidationResults));
    }

    public static ErrorResponse of(MethodArgumentTypeMismatchException ex) {
        return new ErrorResponse(ErrorCode.INVALID_DATA_FORMAT, FieldError.of(ex.getName(), ex.getErrorCode()));
    }

    public static ErrorResponse of(MissingRequestCookieException ex) {
        return new ErrorResponse(ErrorCode.REFRESH_TOKEN_NOT_FOUND);
    }

    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class FieldError {
        private String field;
        private String message;

        private FieldError(String field, String message) {
            this.field = field;
            this.message = message;
        }

        public static List<FieldError> of(String field, String message) {
            List<FieldError> fieldErrors = new ArrayList<>();
            fieldErrors.add(new FieldError(field, message));
            return fieldErrors;
        }

        public static List<FieldError> of(BindingResult bindingResult) {
            List<org.springframework.validation.FieldError> fieldErrors = bindingResult.getFieldErrors();
            return fieldErrors.stream()
                    .map(error -> new FieldError(
                            error.getField(),
                            error.getDefaultMessage()))
                    .toList();
        }

        public static List<FieldError> of(List<ParameterValidationResult> parameterValidationResults) {
            return parameterValidationResults.stream()
                    .flatMap(result -> result.getResolvableErrors().stream()
                        .map(error -> {
                            String param = (error instanceof ObjectError objectError
                                    ? objectError.getObjectName() :
                                    ((MessageSourceResolvable) Objects.requireNonNull(error.getArguments())[0]).getDefaultMessage());
                            return new FieldError(param, error.getDefaultMessage());
                        }))
                    .toList();
        }
    }
}
