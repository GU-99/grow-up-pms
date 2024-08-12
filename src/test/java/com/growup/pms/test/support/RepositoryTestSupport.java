package com.growup.pms.test.support;

import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;

import com.growup.pms.common.config.QueryDslConfig;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import({QueryDslConfig.class})
@AutoConfigureTestDatabase(replace = Replace.NONE)
public abstract class RepositoryTestSupport {
}