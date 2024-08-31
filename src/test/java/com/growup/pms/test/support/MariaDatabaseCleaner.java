package com.growup.pms.test.support;

import java.util.List;
import lombok.NonNull;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

public class MariaDatabaseCleaner extends AbstractTestExecutionListener {
    private JdbcTemplate jdbcTemplate;

    @Override
    public void afterTestMethod(@NonNull TestContext testContext) throws Exception {
        jdbcTemplate = testContext.getApplicationContext().getBean(JdbcTemplate.class);
        execute();
    }

    private void execute() {
        try {
            disableReferentialIntegrity();
            truncateAllTables();
        } catch (DataAccessException ex) {
            throw new RuntimeException("Failed to clean test database: " + ex.getMessage(), ex);
        } finally {
            enableReferentialIntegrity();
        }
    }

    private void enableReferentialIntegrity() {
        jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 1");
    }

    private void disableReferentialIntegrity() {
        jdbcTemplate.execute("SET FOREIGN_KEY_CHECKS = 0");
    }

    private List<String> getAllTableNames() {
        return jdbcTemplate.queryForList("SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_TYPE = 'BASE TABLE'", String.class);
    }

    private void truncateAllTables() {
        getAllTableNames().forEach(tableName -> jdbcTemplate.execute("TRUNCATE TABLE " + tableName));
    }
}
