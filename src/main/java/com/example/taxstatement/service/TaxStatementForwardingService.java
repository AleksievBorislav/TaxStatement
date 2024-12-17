package com.example.taxstatement.service;

import com.example.taxstatement.model.TaxStatement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class TaxStatementForwardingService {

    private static final Logger logger = LoggerFactory.getLogger(TaxStatementForwardingService.class);

    private final TaxStatementService taxStatementService;

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${secondary.service.url}")
    private String mockServiceURL;

    public TaxStatementForwardingService(TaxStatementService taxStatementService) {
        this.taxStatementService = taxStatementService;
    }

    public void forwardTaxStatementById(Long id) {
        Optional<TaxStatement> taxStatementOptional = taxStatementService.getTaxStatementById(id);

        if (taxStatementOptional.isPresent()) {
            TaxStatement taxStatement = taxStatementOptional.get();
            try {

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);

                HttpEntity<TaxStatement> requestEntity = new HttpEntity<>(taxStatement, headers);

                ResponseEntity<String> response = restTemplate.exchange(mockServiceURL, HttpMethod.POST, requestEntity, String.class);

                String responseBody = response.getBody();

                if (response.getStatusCode().is2xxSuccessful()) {
                    logger.info("Mock service response: {}", responseBody);
                } else {
                    logger.error("Error from mock service: {}", response.getStatusCode());
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            logger.warn("Tax Statement not found with ID: {}", id);
        }
    }
}
