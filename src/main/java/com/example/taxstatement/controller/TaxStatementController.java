package com.example.taxstatement.controller;

import com.example.taxstatement.service.TaxStatementForwardingService;
import com.example.taxstatement.service.TaxStatementService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalTime;
import java.util.Objects;

@RestController
@RequestMapping("/api/tax-statements")
public class TaxStatementController {


    private final TaxStatementService taxStatementService;
    private final TaxStatementForwardingService taxStatementForwardingService;

    @Value("${service.availability.start}")
    private String availabilityStart;

    @Value("${service.availability.end}")
    private String availabilityEnd;

    public TaxStatementController(TaxStatementService taxStatementService,
                                  TaxStatementForwardingService taxStatementForwardingService) {
        this.taxStatementService = taxStatementService;
        this.taxStatementForwardingService = taxStatementForwardingService;
    }

    private boolean isWithinServiceHours() {
        LocalTime now = LocalTime.now();
        LocalTime start = LocalTime.parse(availabilityStart);
        LocalTime end = LocalTime.parse(availabilityEnd);
        return now.isAfter(start) && now.isBefore(end);
    }

    @PostMapping("/forward-tax-statement/{id}")
    public ResponseEntity<String> forwardTaxStatement(@PathVariable Long id) {
        if (!isWithinServiceHours()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("The service is not available. This endpoint is only available between "
                            + availabilityStart + " and " + availabilityEnd + ".");
        }

        taxStatementForwardingService.forwardTaxStatementById(id);
        return ResponseEntity.status(HttpStatus.OK).body("Tax statement forwarded successfully.");
    }


    @PostMapping(value = "/upload", consumes="multipart/form-data")
    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file) {
        if (!isWithinServiceHours()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("The service is not available. This endpoint is only available between "
                            + availabilityStart + " and " + availabilityEnd + ".");
        }

        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("File is empty");
        }

        if (!Objects.equals(file.getContentType(), "application/pdf")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid file type. Please upload a PDF file.");
        }

        taxStatementService.processTaxStatementWithRetryAsync(file);
        return ResponseEntity.status(HttpStatus.OK).body("File uploaded successfully and is being processed in the background.");
    }
}
