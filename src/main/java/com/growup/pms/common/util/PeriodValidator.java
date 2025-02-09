package com.growup.pms.common.util;

import com.growup.pms.common.exception.code.ErrorCode;
import com.growup.pms.common.exception.exceptions.BusinessException;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PeriodValidator {

    /**
     * 프로젝트 시작 일자가 프로젝트 일정의 첫번째 시작 일자보다 앞서는지 검증
     *
     * @param projectStartDate 프로젝트 시작 일자
     * @param taskStartDate    프로젝트 일정의 첫번째 시작 일자
     * @throws BusinessException 프로젝트 시작 일자가 프로젝트 일정의 첫번째 시작 일자보다 늦는 경우
     */
    public static void validateProjectStartBeforeAllTaskStart(LocalDate projectStartDate, LocalDate taskStartDate) {
        if (projectStartDate != null && taskStartDate != null && taskStartDate.isBefore(projectStartDate)) {
            throw new BusinessException(ErrorCode.INVALID_PERIOD);
        }
    }

    /**
     * 프로젝트 종료 일자가 프로젝트 일정의 마지막 종료 일자보다 앞서지 않는지 검증
     *
     * @param projectEndDate 프로젝트 종료 일자
     * @param taskEndDate    프로젝트 일정의 마지막 종료 일자
     * @throws BusinessException 프로젝트 종료 일자가 프로젝트 일정의 마지막 종료 일자보다 빠른 경우
     */
    public static void validateProjectEndAfterAllTaskEnd(LocalDate projectEndDate, LocalDate taskEndDate) {
        if (projectEndDate != null && taskEndDate != null && taskEndDate.isAfter(projectEndDate)) {
            throw new BusinessException(ErrorCode.INVALID_PERIOD);
        }
    }
}
