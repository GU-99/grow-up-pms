package com.growup.pms.file.service;

import static com.growup.pms.test.fixture.user.builder.UserTestBuilder.사용자는;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.growup.pms.test.annotation.AutoKoreanDisplayName;
import com.growup.pms.user.domain.User;
import com.growup.pms.user.repository.UserRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

@AutoKoreanDisplayName
@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class ProfileImageServiceTest {

    @Mock
    UserRepository userRepository;

    @Mock
    FileStorageService fileStorageService;

    @InjectMocks
    ProfileImageService profileImageService;

    @Nested
    class 프로필_이미지_업데이트_시 {

        @Test
        void 성공한다() {
            // given
            Long 사용자_ID = 1L;
            User 사용자 = 사용자는().식별자가(사용자_ID).이다();

            byte[] 파일_내용 = "GU-99".getBytes();
            MockMultipartFile 프로필_이미지 = new MockMultipartFile("test.img", 파일_내용);

            when(userRepository.findByIdOrThrow(사용자_ID)).thenReturn(사용자);

            // when & then
            assertThatCode(() -> profileImageService.update(사용자_ID, 프로필_이미지))
                    .doesNotThrowAnyException();
        }
    }

    @Nested
    class 프로필_이미지_다운로드_시 {

        @Test
        void 성공한다() {
            // given
            String 파일_이름 = "test.png";
            byte[] 파일_내용 = "test".getBytes();

            when(fileStorageService.download(anyString())).thenReturn(파일_내용);

            // when
            byte[] 실제_결과 = profileImageService.download(파일_이름);

            // then
            assertThat(실제_결과).isEqualTo(파일_내용);
        }
    }
}
