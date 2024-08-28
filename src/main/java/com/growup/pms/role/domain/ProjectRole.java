package com.growup.pms.role.domain;

import lombok.Getter;

@Getter
public enum ProjectRole {
    ADMIN("ADMIN"),
    LEADER("LEADER"),
    ASSIGNEE("ASSIGNEE");

    private final String roleName;

    ProjectRole(String roleName) {
        this.roleName = roleName;
    }
}
