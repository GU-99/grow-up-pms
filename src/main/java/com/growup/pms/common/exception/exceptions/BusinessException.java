package com.growup.pms.common.exception.exceptions;

import com.growup.pms.common.exception.code.ErrorCode;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
    private final ErrorCode errorCode;
    private final Map<String, String> details = new HashMap<>();

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public BusinessException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.getMessage(), cause);
        this.errorCode = errorCode;
    }

    public <T> BusinessException addDetail(String key, T data) {
        this.details.put(key, String.valueOf(data));
        return this;
    }

    public Map<String, String> getDetails() {
        return Collections.unmodifiableMap(details);
    }

    @Override
    public String toString() {
        if (details.isEmpty()) {
            return super.getMessage();
        }
        return "%s: %s".formatted(super.getMessage(), details.entrySet().stream()
                .map(e -> "%s=%s".formatted(e.getKey(), e.getValue()))
                .collect(Collectors.joining(", ")));
    }
}
