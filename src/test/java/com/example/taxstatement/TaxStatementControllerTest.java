package com.example.taxstatement;

import com.example.taxstatement.model.TaxStatement;
import com.example.taxstatement.repo.TaxStatementRepository;
import com.example.taxstatement.service.TaxStatementForwardingService;
import com.example.taxstatement.service.TaxStatementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;

import org.springframework.web.multipart.MultipartFile;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;


@SpringBootTest
@ActiveProfiles("test")
public class TaxStatementControllerTest extends BaseTest{

    @Autowired
    private TaxStatementService taxStatementService;

    @Autowired
    private TaxStatementForwardingService taxStatementForwardingService;

    @Autowired
    private TaxStatementRepository taxStatementRepository;

    private MultipartFile pdfFile;

    @Value("${test.file.name}")
    private String fileName;

    @Value("${spring.datasource.url}")
    private String datasourceUrl;


    private static final Logger logger = LoggerFactory.getLogger(TaxStatementControllerTest.class);


    @BeforeEach
    public void setup() throws IOException, URISyntaxException {

        logger.info("Datasource URL in test: {}", datasourceUrl);
        ClassLoader classLoader = getClass().getClassLoader();
        byte[] fileContent = Files.readAllBytes(Paths.get(classLoader.getResource(fileName).toURI()));

        pdfFile = new MockMultipartFile("file", fileName, "application/pdf", fileContent);
    }


    @Test
    @Order(1)
    public void testFileParseAndPersistence() throws InterruptedException {

    taxStatementService.processTaxStatementWithRetryAsync(pdfFile);
        await()
                .atMost(5, TimeUnit.SECONDS)
                .pollInterval(1, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    TaxStatement statement = taxStatementRepository.findAll().get(0);
                    assertEquals("John Doe ", statement.getName());
                    assertEquals("123456789 ", statement.getTaxId());
                    assertEquals(10000.00, statement.getTaxPaid());
                    assertEquals(50000.00, statement.getEmploymentIncome());
                    assertEquals(2000.00, statement.getTaxRefundDue());
                });
    }

}
