package com.growup.pms.common.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RegexConstants {
    public static final String USERNAME_PATTERN = "^\\w{2,32}$";
    public static final String PASSWORD_PATTERN = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*()_+={}|`'<>,.?/:;\\-\\\\\\[\\]\"])[A-Za-z0-9~!@#$%^&*()_+={}|`'<>,.?/:;\\-\\\\\\[\\]\"]{8,16}$";
    public static final String NICKNAME_PATTERN = "^[A-Za-z0-9가-힣]{2,20}$";

    public static final String TEAM_NAME_PATTERN = "^[가-힣a-zA-Z0-9]{1,10}$";

    public static final String DATE_TIME_PATTERN = "yyyy-MM-dd";
}
