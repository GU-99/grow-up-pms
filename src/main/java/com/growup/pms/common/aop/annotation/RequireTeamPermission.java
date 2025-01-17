package com.growup.pms.common.aop.annotation;

import com.growup.pms.role.domain.TeamPermission;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequireTeamPermission {
    TeamPermission[] value() default {};
}
