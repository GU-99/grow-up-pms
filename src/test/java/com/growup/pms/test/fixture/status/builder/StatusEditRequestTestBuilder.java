package com.growup.pms.test.fixture.status.builder;

import com.growup.pms.status.controller.dto.request.StatusEditRequest;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.openapitools.jackson.nullable.JsonNullable;

@SuppressWarnings("NonAsciiCharacters")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class StatusEditRequestTestBuilder {

    private String name = "대기중";
    private String colorCode = "#FFFFFF";
    private Short sortOrder = 3;

    public static StatusEditRequestTestBuilder 상태_변경_요청은() {
        return new StatusEditRequestTestBuilder();
    }

    public StatusEditRequestTestBuilder 이름은(String name) {
        this.name = name;
        return this;
    }

    public StatusEditRequestTestBuilder 색상코드는(String colorCode) {
        this.colorCode = colorCode;
        return this;
    }

    public StatusEditRequestTestBuilder 정렬순서는(Short sortOrder) {
        this.sortOrder = sortOrder;
        return this;
    }

    public StatusEditRequest 이다() {
        return StatusEditRequest.builder()
                .statusName(JsonNullable.of(name))
                .colorCode(JsonNullable.of(colorCode))
                .sortOrder(JsonNullable.of(sortOrder))
                .build();
    }
}
