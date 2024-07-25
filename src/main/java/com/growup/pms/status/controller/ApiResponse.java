package com.growup.pms.status.controller;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.FOUND;
import static org.springframework.http.HttpStatus.OK;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@NoArgsConstructor
public class ApiResponse<T> {

    private int code;
    private HttpStatus status;
    private String message;
    private T data;


    public ApiResponse(HttpStatus status, String message, T data) {
        this.code = status.value();
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public static <T> ApiResponse<T> of(HttpStatus status, String message, T data) {
        return new ApiResponse<>(status, message, data);
    }

    public static <T> ApiResponse<T> ok(T data) {
        return of(OK, "SUCCESS", data);
    }

    public static <T> ApiResponse<T> created(T data) {
        return of(CREATED, "CREATED", data);
    }

    public static <T> ApiResponse<T> found(T data) {
        return of(FOUND, "FOUND", data);
    }
}
