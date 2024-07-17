package com.growup.pms.test;

import com.growup.pms.test.config.RestDocsConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.web.context.WebApplicationContext;

@Import(RestDocsConfig.class)
@ExtendWith(RestDocumentationExtension.class)
@Disabled("컨트롤러 슬라이스 테스트와 REST Docs 지원이 필요한 테스트 클래스를 만들 때 상속하세요. 직접 실행하지 마세요.")
public class DefaultRestDocsControllerSliceTest extends AbstractControllerSliceTest {
    @Autowired
    protected RestDocumentationResultHandler docs;

    @BeforeEach
    protected void setUpEach(WebApplicationContext context, RestDocumentationContextProvider provider) {
        this.mockMvc = configureMockMvc(context)
                .apply(MockMvcRestDocumentation.documentationConfiguration(provider))
                .alwaysDo(docs)
                .build();
    }
}
