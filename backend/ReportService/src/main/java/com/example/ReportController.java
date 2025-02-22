package com.example;

import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;


@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final PdfReportService pdfReportService;

    public ReportController(PdfReportService pdfReportService) {
        this.pdfReportService = pdfReportService;
    }

    @PostMapping("/generate")
    public ResponseEntity<byte[]> generateReport(@RequestHeader("Authorization") String bearerToken, @RequestParam(required = false, defaultValue = "week") String groupBy) {
        // Validate 'groupBy' parameter
        if (!"week".equalsIgnoreCase(groupBy) && !"month".equalsIgnoreCase(groupBy)) {
            throw new IllegalArgumentException("Invalid 'groupBy' parameter. Use 'week' or 'month'.");
        }

        // Generate PDF report using the actual data from the database
        byte[] pdfBytes = pdfReportService.generateDiagnosticsReport(bearerToken, groupBy);

        // Dynamic file name
        String fileName = "_Diagnostics_.pdf";

        // Set response headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition
                .builder("attachment")
                .filename(fileName)
                .build());

        // Return the PDF file
        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfBytes);
    }
}