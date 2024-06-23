package com.growup.pms.common.validation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.growup.pms.common.validation.constraint.FieldMatch;
import com.growup.pms.test.annotation.AutoKoreanDisplayName;
import com.growup.pms.test.fixture.user.UserCreateRequestFixture;
import com.growup.pms.test.fixture.user.UserFixture;
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
        boolean expectedResult = true;
        UserCreateRequest request = UserCreateRequestFixture.createDefaultRequestBuilder()
                .password(UserFixture.DEFAULT_PASSWORD)
                .passwordConfirm(UserFixture.DEFAULT_PASSWORD)
                .build();

        // when
        boolean actualResult = validator.isValid(request, null);

        // then
        assertThat(actualResult).isEqualTo(expectedResult);
    }

    @Test
    void 서로_일치하지_않으면_유효성_검사가_실패한다() {
        // given
        boolean expectedResult = false;
        UserCreateRequest request = UserCreateRequestFixture.createDefaultRequestBuilder()
                .password(UserFixture.DEFAULT_PASSWORD)
                .passwordConfirm(UserFixture.INVALID_PASSWORD)
                .build();

        // when
        boolean actualResult = validator.isValid(request, null);

        // then
        assertThat(actualResult).isEqualTo(expectedResult);
    }
}
