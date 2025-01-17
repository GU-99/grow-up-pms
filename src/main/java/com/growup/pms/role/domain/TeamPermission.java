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

    /**
     * Returns the name of the current team permission enum constant.
     *
     * @return A string representing the name of the enum constant
     */
    @Override
    public String getName() {
        return name();
    }

    /**
     * Returns the prefix for team-related permissions.
     *
     * @return A static string prefix "TEAM_" used to identify team permission constants.
     */
    @Override
    public String getPrefix() {
        return PERMISSION_PREFIX;
    }
}
