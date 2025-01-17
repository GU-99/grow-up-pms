package com.growup.pms.role.domain;

public enum ProjectPermission implements BasePermission {
    CREATE_STATUS,
    CREATE_TASK,
    UPDATE_PROJECT,
    UPDATE_STATUS,
    UPDATE_TASK,
    UPDATE_MEMBER_ROLE,
    DELETE_PROJECT,
    DELETE_STATUS,
    DELETE_TASK,
    KICK_MEMBER,
    INVITE_MEMBER;

    private static final String PERMISSION_PREFIX = "PROJECT_";

    @Override
    public String getName() {
        return name();
    }

    @Override
    public String getPrefix() {
        return PERMISSION_PREFIX;
    }
}
