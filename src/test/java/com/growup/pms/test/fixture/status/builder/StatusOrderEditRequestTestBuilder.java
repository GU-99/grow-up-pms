package com.growup.pms.test.fixture.status.builder;

import com.growup.pms.status.controller.dto.request.StatusOrderEditRequest;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@SuppressWarnings("NonAsciiCharacters")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class StatusOrderEditRequestTestBuilder {

    private Long statusId = 1L;
    private Short sortOrder = 1;

    public static StatusOrderEditRequestTestBuilder 상태_정렬순서_변경_요청은() {
        return new StatusOrderEditRequestTestBuilder();
    }

    public StatusOrderEditRequestTestBuilder 상태식별자는(Long statusId) {
        this.statusId = statusId;
        return this;
    }

    public StatusOrderEditRequestTestBuilder 정렬순서는(Short sortOrder) {
        this.sortOrder = sortOrder;
        return this;
    }

    public StatusOrderEditRequest 이다() {
        return StatusOrderEditRequest.builder()
                .statusId(statusId)
                .sortOrder(sortOrder)
                .build();
    }
}
