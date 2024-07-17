package com.growup.pms;

import jakarta.transaction.Transactional;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 통합 테스트를 위한 공통 클래스
 *
 * @author 최영환
 */
@SpringBootTest
@Transactional
public abstract class IntegrationTestSupport {
}
