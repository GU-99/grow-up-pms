package com.growup.pms.role.domain;

import lombok.Getter;

@Getter
public enum TeamRole {
    HEAD("HEAD"),
    LEADER("LEADER"),
    MATE("MATE"),
    ;

    private static final String ROLE_PREFIX = "TEAM_";

    private final String roleName;

    TeamRole(String roleName) {
        this.roleName = ROLE_PREFIX + roleName;
    }
}
