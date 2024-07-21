package com.growup.pms.test.support;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.springframework.web.context.WebApplicationContext;

@Disabled("컨트롤러 슬라이스 테스트 지원이 필요한 테스트 클래스를 만들 때 상속하세요. 직접 실행하지 마세요.")
public class DefaultControllerSliceTest extends AbstractControllerSliceTest {

    @BeforeEach
    void setUp(WebApplicationContext context) {
        this.mockMvc = configureMockMvc(context).build();
    }
}
