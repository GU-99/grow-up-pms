package com.growup.pms.test.annotation;

import com.growup.pms.test.support.H2DatabaseCleaner;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestExecutionListeners.MergeMode;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Retention(RetentionPolicy.RUNTIME)
@TestExecutionListeners(value = {H2DatabaseCleaner.class}, mergeMode = MergeMode.MERGE_WITH_DEFAULTS)
public @interface DatabaseCleanupTest {
}
