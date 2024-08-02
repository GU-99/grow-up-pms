package com.growup.pms.common.validation;

import static com.growup.pms.test.fixture.user.UserCreateRequestTestBuilder.가입하는_사용자는;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.growup.pms.common.validation.constraint.FieldMatch;
import com.growup.pms.test.annotation.AutoKoreanDisplayName;
import com.growup.pms.user.dto.UserCreateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@AutoKoreanDisplayName
@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class FieldMatchValidatorTest {
    FieldMatchValidator validator;

    @BeforeEach
    void setUp() {
        validator = new FieldMatchValidator();

        FieldMatch annotation = mock(FieldMatch.class);
        when(annotation.first()).thenReturn("password");
        when(annotation.second()).thenReturn("passwordConfirm");

        validator.initialize(annotation);
    }

    @Test
    void 서로_일치하면_유효성_검사가_성공한다() {
        // given
        boolean 예상하는_결과 = true;
        UserCreateRequest 사용자_생성_요청 = 가입하는_사용자는().비밀번호가("12345678").비밀번호_확인이("12345678").이다();

        // when
        boolean 실제_결과 = validator.isValid(사용자_생성_요청, null);

        // then
        assertThat(실제_결과).isEqualTo(예상하는_결과);
    }

    @Test
    void 서로_일치하지_않으면_유효성_검사가_실패한다() {
        // given
        boolean 예상하는_결과 = false;
        UserCreateRequest 사용자_생성_요청 = 가입하는_사용자는().비밀번호가("12345678").비밀번호_확인이("87654321").이다();

        // when
        boolean 실제_결과 = validator.isValid(사용자_생성_요청, null);

        // then
        assertThat(실제_결과).isEqualTo(예상하는_결과);
    }
}
