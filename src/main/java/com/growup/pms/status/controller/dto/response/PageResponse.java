package com.growup.pms.status.controller.dto.response;

public record PageResponse<T>(Boolean hasNext, T items) {
    public static <T> PageResponse<T> of(Boolean hasNext, T items) {
        return new PageResponse<>(hasNext, items);
    }
}
