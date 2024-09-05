package com.growup.pms.test.fixture.status.builder;

import static com.growup.pms.test.fixture.project.builder.ProjectTestBuilder.프로젝트는;

import com.growup.pms.project.domain.Project;
import com.growup.pms.status.domain.Status;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.test.util.ReflectionTestUtils;

@SuppressWarnings("NonAsciiCharacters")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class StatusTestBuilder {

    private Long id = 1L;
    private Project project = 프로젝트는().이다();
    private String name = "대기중";
    private String color = "FFFFFF";
    private Short sortOrder = 0;

    public static StatusTestBuilder 상태는() {
        return new StatusTestBuilder();
    }

    public StatusTestBuilder 식별자가(Long id) {
        this.id = id;
        return this;
    }

    public StatusTestBuilder 프로젝트가(Project project) {
        this.project = project;
        return this;
    }

    public StatusTestBuilder 이름이(String name) {
        this.name = name;
        return this;
    }

    public StatusTestBuilder 색상코드가(String color) {
        this.color = color;
        return this;
    }

    public StatusTestBuilder 정렬순서가(Short sortOrder) {
        this.sortOrder = sortOrder;
        return this;
    }

    public Status 이다() {
        var build = Status.builder()
                .project(project)
                .name(name)
                .colorCode(color)
                .sortOrder(sortOrder)
                .build();

        ReflectionTestUtils.setField(build, "id", id);
        return build;
    }
}
