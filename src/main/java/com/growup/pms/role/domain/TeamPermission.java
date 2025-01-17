package com.growup.pms.role.domain;

public enum TeamPermission implements BasePermission {
    CREATE_PROJECT,
    READ_PROJECT,
    UPDATE_TEAM,
    UPDATE_MEMBER_ROLE,
    DELETE_TEAM,
    KICK_MEMBER,
    INVITE_MEMBER;

    private static final String PERMISSION_PREFIX = "TEAM_";

    @Override
    public String getName() {
        return name();
    }

    @Override
    public String getPrefix() {
        return PERMISSION_PREFIX;
    }
}
