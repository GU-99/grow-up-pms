package com.growup.pms.common.aop.annotation;

import com.growup.pms.role.domain.TeamPermission;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 팀 관련 권한을 요구하는 메서드에 적용하는 애노테이션입니다.
 *
 * <p>이 애노테이션이 적용된 메서드는 실행 전에 현재 사용자가 지정된 팀 권한을
 * 가지고 있는지 검사합니다. 권한이 없는 경우 리소스에 접근 권한이 없다는 예외가 발생합니다.</p>
 *
 * <p><strong>사용 예시:</strong></p>
 * <pre>{@code
 * @PostMapping("/teams/{teamId}/projects")
 * @RequireTeamPermission(TeamPermission.CREATE_PROJECT)
 * public ResponseEntity<ProjectResponse> createProject(@TeamId @PathVariable Long teamId, ...) {
 *     // 메서드 구현
 * }
 * }</pre>
 *
 * <p>주의: 이 애노테이션을 사용할 때는 메서드 파라미터에 {@code @TeamId} 또는 {@code @ProjectId}
 * 애노테이션이 붙은 {@code Long} 타입의 ID가 반드시 존재해야 합니다. {@code @ProjectId}만 있는
 * 경우, 해당 프로젝트와 연관된 팀의 권한을 검사합니다.</p>
 *
 * @see TeamPermission
 * @see TeamId
 * @see ProjectId
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RequireTeamPermission {
    TeamPermission[] value() default {};
}
