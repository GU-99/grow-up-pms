package com.growup.pms.test.fixture.role.builder;

import com.growup.pms.role.domain.Role;
import com.growup.pms.role.domain.RoleType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.test.util.ReflectionTestUtils;

@SuppressWarnings("NonAsciiCharacters")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RoleTestBuilder {
    private Long id = 1L;
    private RoleType roleType = RoleType.TEAM;
    private String name = "TEAM_ADMIN";

    public static RoleTestBuilder 역할은() {
        return new RoleTestBuilder();
    }

    public RoleTestBuilder 식별자가(Long 식별자) {
        this.id = 식별자;
        return this;
    }

    public RoleTestBuilder 타입이(RoleType 타입) {
        this.roleType = 타입;
        return this;
    }

    public RoleTestBuilder 이름이(String 이름) {
        this.name = 이름;
        return this;
    }

    public Role 이다() {
        var role = new Role(roleType, name);
        ReflectionTestUtils.setField(role, "id", id);
        return role;
    }
}
