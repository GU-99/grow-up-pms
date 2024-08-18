package com.growup.pms.role.domain;

import lombok.Getter;

@Getter
public enum TeamRole {
    HEAD("HEAD"),
    LEADER("LEADER"),
    MATE("MATE");

    private final String roleName;

    TeamRole(String roleName) {
        this.roleName =  roleName;
    }

    public static TeamRole of(String roleName) {
        return TeamRole.valueOf(roleName);
    }
}
