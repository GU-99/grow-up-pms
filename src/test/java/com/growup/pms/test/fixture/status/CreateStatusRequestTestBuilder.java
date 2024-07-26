package com.growup.pms.test.fixture.status;

import com.growup.pms.status.controller.dto.request.CreateStatusRequest;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@SuppressWarnings("NonAsciiCharacters")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateStatusRequestTestBuilder {

    private Long projectId = 1L;
    private String name = "대기중";
    private String colorCode = "FFFFFF";
    private Short sortOrder = 0;

    public static CreateStatusRequestTestBuilder 상태_생성_요청은() {
        return new CreateStatusRequestTestBuilder();
    }

    public CreateStatusRequestTestBuilder 프로젝트_식별자는(Long projectId) {
        this.projectId = projectId;
        return this;
    }

    public CreateStatusRequestTestBuilder 이름은(String name) {
        this.name = name;
        return this;
    }

    public CreateStatusRequestTestBuilder 색상코드는(String colorCode) {
        this.colorCode = colorCode;
        return this;
    }

    public CreateStatusRequestTestBuilder 정렬순서는(Short sortOrder) {
        this.sortOrder = sortOrder;
        return this;
    }

    public CreateStatusRequest 이다() {
        return CreateStatusRequest.builder()
                .projectId(projectId)
                .name(name)
                .colorCode(colorCode)
                .sortOrder(sortOrder)
                .build();
    }
}
