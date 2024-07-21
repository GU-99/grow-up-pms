package com.growup.pms.common.validation;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;

import com.growup.pms.common.validation.constraint.ImageFile;
import com.growup.pms.test.annotation.AutoKoreanDisplayName;
import com.growup.pms.user.service.UserService;
import java.io.FileInputStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

@AutoKoreanDisplayName
@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class ImageFileValidatorTest {
    ImageFileValidator validator;

    @Autowired
    UserService userService;

    @BeforeEach
    void setUp() {
        validator = new ImageFileValidator();

        ImageFile annotation = mock(ImageFile.class);
        validator.initialize(annotation);
    }


    @Test
    void JPEG_파일을_업로드하면_유효성_검사가_성공한다() throws Exception{
        // given
        boolean expectedResult = true;

        final String rootPath = "src/test/resources/images";
        final String fileName = "testImage.jpg";
        FileInputStream fileInputStream = new FileInputStream(rootPath + "/" + fileName);
        MockMultipartFile file = new MockMultipartFile(
                "test",
                fileName,
                MediaType.IMAGE_JPEG_VALUE,
                fileInputStream
        );

        // when
        boolean actualResult = validator.isValid(file, null);

        // then
        assertThat(actualResult).isEqualTo(expectedResult);
    }

    @Test
    void 이미지_파일이_아니면_유효성_검사가_실패한다() throws Exception{
        // given
        boolean expectedResult = false;

        final String rootPath = "src/test/resources/images";
        final String fileName = "testImage.jpg";
        FileInputStream fileInputStream = new FileInputStream(rootPath + "/" + fileName);
        MockMultipartFile file = new MockMultipartFile(
                "test",
                fileName,
                MediaType.APPLICATION_OCTET_STREAM_VALUE,
                fileInputStream
        );

        // when
        boolean actualResult = validator.isValid(file, null);

        // then
        assertThat(actualResult).isEqualTo(expectedResult);
    }
}
