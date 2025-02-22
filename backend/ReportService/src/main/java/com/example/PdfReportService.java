package com.example;
import com.example.ChartUtils;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class PdfReportService  {

    private final RestTemplate restTemplate;

    public PdfReportService() {
        this.restTemplate = new RestTemplate();
    }

    public byte[] generateDiagnosticsReport(String bearerToken, String groupBy) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            PdfDocument pdf = new PdfDocument(new PdfWriter(baos));
            Document document = new Document(pdf);

            document.add(new Paragraph("Driving Diagnostics Report")
                    .setFontSize(14).setBold().setMarginBottom(10).setTextAlignment(TextAlignment.CENTER));

            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
            headers.setBearerAuth(bearerToken);
            // Fetch data from the database
            List<SensorDataDTO> readings = restTemplate.exchange(
                    "http://localhost:8090/api/sensor/readings/data",
                    HttpMethod.GET,
                    new HttpEntity<>("parameters", headers),
                    new ParameterizedTypeReference<List<SensorDataDTO>>() {}
            ).getBody();

            if (readings.isEmpty()) {
                document.add(new Paragraph("No diagnostics data available."));
            } else {
                // Group data by the selected period (day/week/month)
                Map<String, Map<String, Double>> groupedData = groupDataByPeriod(readings, groupBy);

                // Add daily diagnostics table
                addSectionHeader(document, "Daily Diagnostics Table");
                Table diagnosticsTable = createDailyDiagnosticsTable(groupedData);
                document.add(diagnosticsTable);

                // Add diagnostics bar chart
                addSectionHeader(document, "Diagnostics Averages (Bar Chart)");
                byte[] barChart = ChartUtils.generateDiagnosticsBarChart("Diagnostics Averages", calculateAveragesForChart(groupedData));
                addChartToDocument(document, barChart);

                // Add behavior chart
                addSectionHeader(document, "Behavior Distribution");
                Map<String, Long> behaviorCounts = calculateBehaviorCounts(readings);
                byte[] behaviorChart = ChartUtils.generateBehaviorComparisonChart("Behavior Distribution", behaviorCounts);
                addChartToDocument(document, behaviorChart);

                // Add warnings or recommendations based on behavior data
                if (isAggressiveMajority(behaviorCounts)) {
                    document.add(new Paragraph("⚠ **Caution**: Your driving behavior indicates a high occurrence of aggressive driving.")
                            .setFontSize(12).setBold().setFontColor(ColorConstants.RED).setMarginTop(10));
                }
            }

            document.close();
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error generating diagnostics PDF", e);
        }
    }

    private Map<String, Map<String, Double>> groupDataByPeriod(List<SensorDataDTO> readings, String groupBy) {
        return readings.stream()
                .collect(Collectors.groupingBy(
                        r -> {
                            LocalDate date = r.getTimestamp().toLocalDate();
                            return groupBy.equals("week") ? date.toString() : date.withDayOfMonth(1).toString();
                        },
                        Collectors.collectingAndThen(Collectors.toList(), this::calculateAverages)
                ));
    }

    private Map<String, Double> calculateAverages(List<SensorDataDTO> readings) {
        return Map.of(
                "AccX", readings.stream().mapToDouble(SensorDataDTO::getAccX).average().orElse(0.0),
                "AccY", readings.stream().mapToDouble(SensorDataDTO::getAccY).average().orElse(0.0),
                "GyroX", readings.stream().mapToDouble(SensorDataDTO::getGyroX).average().orElse(0.0),
                "GyroY", readings.stream().mapToDouble(SensorDataDTO::getGyroY).average().orElse(0.0)
        );
    }

    private Map<String, Double> calculateAveragesForChart(Map<String, Map<String, Double>> groupedData) {
        return Map.of(
                "AccX", groupedData.values().stream().mapToDouble(m -> m.get("AccX")).average().orElse(0.0),
                "AccY", groupedData.values().stream().mapToDouble(m -> m.get("AccY")).average().orElse(0.0),
                "GyroX", groupedData.values().stream().mapToDouble(m -> m.get("GyroX")).average().orElse(0.0),
                "GyroY", groupedData.values().stream().mapToDouble(m -> m.get("GyroY")).average().orElse(0.0)
        );
    }

    private Map<String, Long> calculateBehaviorCounts(List<SensorDataDTO> readings) {
        return readings.stream()
                .collect(Collectors.groupingBy(SensorDataDTO::getPrediction, Collectors.counting()));
    }

    private Table createDailyDiagnosticsTable(Map<String, Map<String, Double>> groupedData) {
        Table table = new Table(new float[]{3, 3, 3, 3, 3});
        table.setWidth(UnitValue.createPercentValue(100));

        table.addHeaderCell(createHeaderCell("Date"));
        table.addHeaderCell(createHeaderCell("AccX"));
        table.addHeaderCell(createHeaderCell("AccY"));
        table.addHeaderCell(createHeaderCell("GyroX"));
        table.addHeaderCell(createHeaderCell("GyroY"));

        for (var entry : groupedData.entrySet()) {
            table.addCell(createDataCell(entry.getKey()));
            Map<String, Double> averages = entry.getValue();
            table.addCell(createDataCell(String.format("%.2f", averages.get("AccX"))));
            table.addCell(createDataCell(String.format("%.2f", averages.get("AccY"))));
            table.addCell(createDataCell(String.format("%.2f", averages.get("GyroX"))));
            table.addCell(createDataCell(String.format("%.2f", averages.get("GyroY"))));
        }

        return table;
    }

    private boolean isAggressiveMajority(Map<String, Long> behaviorCounts) {
        long total = behaviorCounts.values().stream().mapToLong(Long::longValue).sum();
        long aggressiveCount = behaviorCounts.getOrDefault("Aggressive", 0L);
        return ((double) aggressiveCount / total) >= 0.4;
    }

    private Cell createHeaderCell(String content) {
        return new Cell().add(new Paragraph(content).setBold().setFontColor(ColorConstants.BLACK));
    }

    private Cell createDataCell(String content) {
        return new Cell().add(new Paragraph(content));
    }

    private void addSectionHeader(Document document, String title) {
        document.add(new Paragraph(title).setFontSize(14).setBold().setMarginTop(20));
    }

    private void addChartToDocument(Document document, byte[] chartBytes) {
        if (chartBytes != null) {
            document.add(new Image(ImageDataFactory.create(chartBytes)).setAutoScale(true).setMarginBottom(20));
        }
    }
}