package com.growup.pms.test.support;

import com.growup.pms.test.config.RestDocsConfig;
import org.junit.jupiter.api.BeforeEach;
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
public abstract class ControllerSliceTestSupport extends AbstractControllerSliceTest {
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
