package com.growup.pms.status.controller.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PageResponse<T> {

    private Boolean hasNext;
    private T items;

    public PageResponse(Boolean hasNext, T items) {
        this.hasNext = hasNext;
        this.items = items;
    }

    public static <T> PageResponse<T> of(Boolean hasNext, T items) {
        return new PageResponse<>(hasNext, items);
    }
}
