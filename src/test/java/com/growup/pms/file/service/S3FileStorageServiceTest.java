package com.growup.pms.file.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.growup.pms.common.exception.code.ErrorCode;
import com.growup.pms.common.exception.exceptions.BusinessException;
import com.growup.pms.test.annotation.AutoKoreanDisplayName;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.function.Consumer;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.http.AbortableInputStream;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest.Builder;
import software.amazon.awssdk.services.s3.model.S3Exception;

@AutoKoreanDisplayName
@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class S3FileStorageServiceTest {

    @Mock
    S3Client s3Client;

    @InjectMocks
    S3FileStorageService s3FileStorageService;

    static final String S3_버킷_이름 = "gu-99-bucket-name";

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(s3FileStorageService, "bucketName", S3_버킷_이름);
    }

    @Nested
    class 파일_업로드_시 {

        @Test
        void 성공한다() {
            // given
            MockMultipartFile 업로드_할_파일 = new MockMultipartFile("file", "test.txt", "text/plain", "GU-99".getBytes());

            ArgumentCaptor<Consumer<Builder>> 업로드_객체_캡터 = ArgumentCaptor.forClass(Consumer.class);
            ArgumentCaptor<RequestBody> 파일_내용_캡터 = ArgumentCaptor.forClass(RequestBody.class);

            // when
            String 업로드_된_파일명 = s3FileStorageService.upload(업로드_할_파일);

            // then
            verify(s3Client).putObject(업로드_객체_캡터.capture(), 파일_내용_캡터.capture());

            SoftAssertions.assertSoftly(softly -> {
                softly.assertThat(업로드_된_파일명).endsWith(".txt");

                Consumer<PutObjectRequest.Builder> 업로드_객체_컨슈머 = 업로드_객체_캡터.getValue();
                PutObjectRequest.Builder 업로드_요청_빌더 = PutObjectRequest.builder();
                업로드_객체_컨슈머.accept(업로드_요청_빌더);
                PutObjectRequest 업로드_요청 = 업로드_요청_빌더.build();

                softly.assertThat(업로드_요청.bucket()).isEqualTo(S3_버킷_이름);
                softly.assertThat(업로드_요청.key()).isEqualTo(업로드_된_파일명);
            });
        }

        @Test
        void 업로드된_파일명이_존재하지_않으면_예외가_발생한다() {
            // given
            MockMultipartFile 업로드_할_파일 = new MockMultipartFile("file", null, "text/plain", "GU-99".getBytes());

            // when & then
            assertThatThrownBy(() -> s3FileStorageService.upload(업로드_할_파일))
                    .isInstanceOf(BusinessException.class)
                    .hasFieldOrPropertyWithValue("errorCode", ErrorCode.MISSING_FILE_NAME);
        }

        @Test
        void 업로드_실패_시_예외가_발생한다() {
            // given
            MockMultipartFile 업로드_할_파일 = new MockMultipartFile("file", "test.txt", "text/plain", "GU-99".getBytes());

            doThrow(S3Exception.class).when(s3Client).putObject(any(Consumer.class), any(RequestBody.class));

            // when & then
            assertThatThrownBy(() -> s3FileStorageService.upload(업로드_할_파일))
                    .isInstanceOf(BusinessException.class)
                    .hasFieldOrPropertyWithValue("errorCode", ErrorCode.FILE_UPLOAD_FAILED)
                    .hasCauseInstanceOf(S3Exception.class);
        }
    }

    @Nested
    class 파일_다운로드_시 {

        @Test
        void 성공한다() {
            // given
            String 파일_이름 = "test.txt";
            byte[] 예상_파일_내용 = "GU-99".getBytes();
            ResponseInputStream<GetObjectResponse> 응답_입력_스트림 = new ResponseInputStream<>(
                    GetObjectResponse.builder().build(),
                    AbortableInputStream.create(new ByteArrayInputStream(예상_파일_내용)));

            when(s3Client.getObject(any(Consumer.class))).thenReturn(응답_입력_스트림);

            // when
            byte[] 다운로드_받은_내용 = s3FileStorageService.download(파일_이름);

            // then
            assertThat(다운로드_받은_내용).isEqualTo(예상_파일_내용);
        }

        @Test
        void S3Exception_발생_시_FileStorageException을_던진다() {
            // given
            String 파일_경로 = "file.txt";

            when(s3Client.getObject(any(Consumer.class))).thenThrow(S3Exception.class);

            // when & then
            assertThatThrownBy(() -> s3FileStorageService.download(파일_경로))
                    .isInstanceOf(BusinessException.class)
                    .hasFieldOrPropertyWithValue("errorCode", ErrorCode.FILE_DOWNLOAD_FAILED)
                    .hasCauseInstanceOf(S3Exception.class);
        }

        @Test
        void IOException_발생_시_FileStorageException을_던진다() throws IOException {
            // given
            String 파일_경로 = "file.txt";
            ResponseInputStream<GetObjectResponse> 응답_입력_스트림 = mock(ResponseInputStream.class);

            when(응답_입력_스트림.read(any(byte[].class))).thenThrow(IOException.class);
            when(s3Client.getObject(any(Consumer.class))).thenReturn(응답_입력_스트림);

            // when & then
            assertThatThrownBy(() -> s3FileStorageService.download(파일_경로))
                    .isInstanceOf(BusinessException.class)
                    .hasFieldOrPropertyWithValue("errorCode", ErrorCode.FILE_DOWNLOAD_FAILED)
                    .hasCauseInstanceOf(IOException.class);
        }
    }

    @Nested
    class 파일_제거_시 {

        @Test
        void 성공한다() {
            // given
            String 파일_경로 = "test.txt";

            // when
            s3FileStorageService.delete(파일_경로);

            // then
            verify(s3Client).deleteObject(argThat((Consumer<DeleteObjectRequest.Builder> consumer) -> {
                DeleteObjectRequest.Builder builder = DeleteObjectRequest.builder();
                consumer.accept(builder);
                DeleteObjectRequest request = builder.build();
                return request.bucket().equals(S3_버킷_이름) && request.key().equals(파일_경로);
            }));
        }

        @Test
        void S3Exception_발생_시_FileStorageException을_던진다() {
            // given
            String 파일_경로 = "test.txt";

            doThrow(S3Exception.class).when(s3Client).deleteObject(any(Consumer.class));

            // when & then
            assertThatThrownBy(() -> s3FileStorageService.delete(파일_경로))
                    .isInstanceOf(BusinessException.class)
                    .hasFieldOrPropertyWithValue("errorCode", ErrorCode.FILE_DELETE_FAILED)
                    .hasCauseInstanceOf(S3Exception.class);
        }
    }
}
