package com.growup.pms.test.fixture.status;

import com.growup.pms.status.controller.dto.request.StatusCreateRequest;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@SuppressWarnings("NonAsciiCharacters")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class StatusCreateRequestTestBuilder {

    private Long projectId = 1L;
    private String name = "대기중";
    private String colorCode = "FFFFFF";
    private Short sortOrder = 0;

    public static StatusCreateRequestTestBuilder 상태_생성_요청은() {
        return new StatusCreateRequestTestBuilder();
    }

    public StatusCreateRequestTestBuilder 프로젝트_식별자는(Long projectId) {
        this.projectId = projectId;
        return this;
    }

    public StatusCreateRequestTestBuilder 이름은(String name) {
        this.name = name;
        return this;
    }

    public StatusCreateRequestTestBuilder 색상코드는(String colorCode) {
        this.colorCode = colorCode;
        return this;
    }

    public StatusCreateRequestTestBuilder 정렬순서는(Short sortOrder) {
        this.sortOrder = sortOrder;
        return this;
    }

    public StatusCreateRequest 이다() {
        return StatusCreateRequest.builder()
                .projectId(projectId)
                .name(name)
                .colorCode(colorCode)
                .sortOrder(sortOrder)
                .build();
    }
}
