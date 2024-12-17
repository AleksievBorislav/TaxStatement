package com.example.taxstatement;

import com.example.taxstatement.service.TaxStatementForwardingService;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonNullFormatVisitor;
import org.junit.jupiter.api.BeforeAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
public abstract class BaseTest {

    private static final Logger logger = LoggerFactory.getLogger(BaseTest.class);


    private static final PostgreSQLContainer<?> POSTGRESQL_CONTAINER =
            new PostgreSQLContainer<>("postgres:latest")
                    .withDatabaseName("tax_statement")
                    .withUsername("test")
                    .withPassword("test")
                    .withReuse(true);

    @BeforeAll
    static void setupContainer() {
        if (!POSTGRESQL_CONTAINER.isRunning()) {
            POSTGRESQL_CONTAINER.start();
        }
        logger.info("Testcontainer PostgreSQL started at: {}", POSTGRESQL_CONTAINER.getJdbcUrl());

        System.setProperty("spring.datasource.url", POSTGRESQL_CONTAINER.getJdbcUrl());
        System.setProperty("spring.datasource.username", POSTGRESQL_CONTAINER.getUsername());
        System.setProperty("spring.datasource.password", POSTGRESQL_CONTAINER.getPassword());
    }
}
