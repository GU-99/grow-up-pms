package com.growup.pms.common;

import com.growup.pms.common.exception.code.ErrorCode;
import com.growup.pms.common.exception.exceptions.BusinessException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Period {

    @Column(nullable = false)
    private LocalDate startDate;

    private LocalDate endDate;

    @Builder
    protected Period(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
        validatePeriod();
    }

    public void editStartDate(LocalDate startDate) {
        this.startDate = startDate;
        validatePeriod();
    }

    public void editEndDate(LocalDate endDate) {
        this.endDate = endDate;
        validatePeriod();
    }

    private void validatePeriod() {
        if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
            throw new BusinessException(ErrorCode.INVALID_PERIOD);
        }
    }
}
