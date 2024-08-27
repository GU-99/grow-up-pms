package com.growup.pms.common.exception.dto;

import com.growup.pms.common.exception.code.ErrorCode;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.validation.BindingResult;
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

    public static ErrorResponse of(MethodArgumentTypeMismatchException ex) {
        return new ErrorResponse(ErrorCode.INVALID_DATA_FORMAT, FieldError.of(ex.getName(), ex.getErrorCode()));
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

        private static List<FieldError> of(BindingResult bindingResult) {
            List<org.springframework.validation.FieldError> fieldErrors = bindingResult.getFieldErrors();
            return fieldErrors.stream()
                    .map(error -> new FieldError(
                            error.getField(),
                            error.getDefaultMessage()))
                    .toList();
        }
    }
}
