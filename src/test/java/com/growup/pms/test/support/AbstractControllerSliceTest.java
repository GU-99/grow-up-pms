package com.growup.pms.test.support;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.growup.pms.common.config.ObjectMapperConfig;
import com.growup.pms.common.security.jwt.JwtTokenProvider;
import com.growup.pms.test.annotation.AutoServiceMockBeans;
import com.growup.pms.test.config.TestEncryptionConfig;
import com.growup.pms.test.config.TestSecurityConfig;
import org.junit.jupiter.api.AfterEach;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Service;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;

@Import({ObjectMapperConfig.class, TestSecurityConfig.class, TestEncryptionConfig.class})
@AutoServiceMockBeans(basePackage = "com.growup.pms")
@WebMvcTest(includeFilters = @Filter(type = FilterType.ANNOTATION, classes = RestController.class))
public abstract class AbstractControllerSliceTest {
    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    private ConfigurableListableBeanFactory beanFactory;

    @MockBean
    protected JwtTokenProvider jwtTokenProvider;

    protected DefaultMockMvcBuilder configureMockMvc(WebApplicationContext context) {
        return MockMvcBuilders.webAppContextSetup(context)
                .alwaysDo(MockMvcResultHandlers.print());
    }

    @AfterEach
    protected void resetMocks() {
        String[] mockBeanNames = beanFactory.getBeanNamesForAnnotation(Service.class);
        for (String beanName : mockBeanNames) {
            Object bean = beanFactory.getBean(beanName);
            if (Mockito.mockingDetails(bean).isMock()) {
                Mockito.reset(bean);
            }
        }
    }
}
