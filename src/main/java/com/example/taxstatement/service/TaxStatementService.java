package com.example.taxstatement.service;

import com.example.taxstatement.model.TaxStatement;
import com.example.taxstatement.repo.TaxStatementRepository;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class TaxStatementService {

    private static final Logger logger = LoggerFactory.getLogger(TaxStatementService.class);

    private static final int MAX_RETRIES = 5;
    private static final long INITIAL_BACKOFF = 1000; // 1 second
    private static final double BACKOFF_MULTIPLIER = 2.0;

    private final TaxStatementRepository taxStatementRepository;

    public TaxStatementService(TaxStatementRepository taxStatementRepository) {
        this.taxStatementRepository = taxStatementRepository;
    }

    @Async
    public void processTaxStatementWithRetryAsync(MultipartFile file) {
        try {
            int attempt = 0;
            while (attempt < MAX_RETRIES) {
                try {
                    TaxStatement taxStatement = parseTaxStatement(file);

                    taxStatementRepository.save(taxStatement);

                    logger.info("Parsed and saved Tax Statement: {}", taxStatement);

                    return;
                } catch (IOException e) {
                    attempt++;
                    if (attempt >= MAX_RETRIES) {
                        logger.warn("Reached maximum attempts on the request!");
                        return;
                    }

                    long backoffTime = (long) (INITIAL_BACKOFF * Math.pow(BACKOFF_MULTIPLIER, attempt));
                    try {
                        Thread.sleep(backoffTime);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                        throw new IOException("Retry attempt interrupted", ex);
                    }
                }
            }
        } catch (IOException e) {
            logger.error("Failed to process file: {}", e.getMessage());
        }
    }

    private TaxStatement parseTaxStatement(MultipartFile file) throws IOException {
        try (PDDocument document = PDDocument.load(file.getInputStream())) {

            PDFTextStripper pdfStripper = new PDFTextStripper();
            String text = pdfStripper.getText(document);

            String name = extractData(text, "Name: (.*)");
            String taxId = extractData(text, "Tax ID: (.*)");
            double employmentIncome = parseDouble(extractData(text, "Employment Income: (\\d+(\\.\\d+)?)"));
            double taxPaid = parseDouble(extractData(text, "Tax Paid: (\\d+(\\.\\d+)?)"));
            double taxRefundDue = parseDouble(extractData(text, "Tax Refund Due: (\\d+(\\.\\d+)?)"));

            TaxStatement taxStatement = new TaxStatement();
            taxStatement.setName(name);
            taxStatement.setTaxId(taxId);
            taxStatement.setEmploymentIncome(employmentIncome);
            taxStatement.setTaxPaid(taxPaid);
            taxStatement.setTaxRefundDue(taxRefundDue);

            return taxStatement;
        }
    }

    private String extractData(String text, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "Unknown";
    }

    private double parseDouble(String value) {
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    public Optional<TaxStatement> getTaxStatementById(Long id) {
        return taxStatementRepository.findById(id);
    }
}
