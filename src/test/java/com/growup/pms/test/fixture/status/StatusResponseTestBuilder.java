package com.growup.pms.test.fixture.status;

import com.growup.pms.status.controller.dto.response.StatusResponse;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@SuppressWarnings("NonAsciiCharacters")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class StatusResponseTestBuilder {

    private Long statusId = 1L;
    private Long projectId = 1L;
    private String name = "대기중";
    private String colorCode = "#FFFFFF";
    private Short sortOrder = 0;

    public static StatusResponseTestBuilder 상태_응답은() {
        return new StatusResponseTestBuilder();
    }

    public StatusResponseTestBuilder 상태_식별자는(Long statusId) {
        this.statusId = statusId;
        return this;
    }

    public StatusResponseTestBuilder 프로젝트_식별자는(Long projectId) {
        this.projectId = projectId;
        return this;
    }

    public StatusResponseTestBuilder 이름은(String name) {
        this.name = name;
        return this;
    }

    public StatusResponseTestBuilder 색상코드는(String colorCode) {
        this.colorCode = colorCode;
        return this;
    }

    public StatusResponseTestBuilder 정렬순서는(Short sortOrder) {
        this.sortOrder = sortOrder;
        return this;
    }

    public StatusResponse 이다() {
        return StatusResponse.builder()
                .statusId(statusId)
                .projectId(projectId)
                .name(name)
                .colorCode(colorCode)
                .sortOrder(sortOrder)
                .build();
    }
}
