package com.growup.pms.common.aop.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 메서드 파라미터가 프로젝트 ID를 나타냄을 표시하는 마커 애노테이션입니다.
 * 이 애노테이션은 주로 권한 검사 로직에서 사용되며,
 * {@code AuthorizationAspect}에 의해 처리됩니다.
 *
 * <p>사용 예:</p>
 * <pre>
 * {@code
 * @GetMapping("/projects/{projectId}")
 * @RequireProjectPermission(ProjectPermission.READ_PROJECT)
 * public ResponseEntity<ProjectDto> getProject(@Positive @ProjectId @PathVariable Long projectId) {
 *     // 메서드 구현
 * }
 * }
 * </pre>
 *
 * @see com.growup.pms.common.aop.aspect.AuthorizationAspect
 * @see RequireProjectPermission
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface ProjectId {
}
