package com.example;

import org.knowm.xchart.*;
import org.knowm.xchart.BitmapEncoder.BitmapFormat;

import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class ChartUtils {

    public static byte[] generateDiagnosticsChart(String title, Map<String, Double> accX, Map<String, Double> accY,
                                                  Map<String, Double> gyroX, Map<String, Double> gyroY) {
        try {
            XYChart chart = new XYChartBuilder()
                    .width(800)
                    .height(600)
                    .title(title)
                    .xAxisTitle("Period")
                    .yAxisTitle("Average Values")
                    .build();

            List<Date> xData = accX.keySet().stream()
                    .map(ChartUtils::parseDateKey) // Use the helper method to parse dates
                    .collect(Collectors.toList());

            chart.addSeries("AccX", xData, new ArrayList<>(accX.values()));
            chart.addSeries("AccY", xData, new ArrayList<>(accY.values()));
            chart.addSeries("GyroX", xData, new ArrayList<>(gyroX.values()));
            chart.addSeries("GyroY", xData, new ArrayList<>(gyroY.values()));

            chart.getStyler().setChartBackgroundColor(Color.WHITE);
            chart.getStyler().setDatePattern("MMM dd"); // Format dates
            chart.getStyler().setSeriesColors(new Color[]{Color.BLUE, Color.ORANGE, Color.GREEN, Color.RED});

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            BitmapEncoder.saveBitmap(chart, baos, BitmapFormat.PNG);
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error generating diagnostics chart", e);
        }
    }

    private static Date parseDateKey(String key) {
        try {
            if (key.contains("W")) {
                // Week-based date parsing
                DateTimeFormatter weekFormatter = DateTimeFormatter.ISO_WEEK_DATE;
                LocalDate weekDate = LocalDate.parse(key + "-1", weekFormatter); // Adds Monday of the week
                return Date.from(weekDate.atStartOfDay(java.time.ZoneId.systemDefault()).toInstant());
            } else {
                // Month-based date parsing
                DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("yyyy-MM");
                return Date.from(LocalDate.parse(key + "-01", DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                        .atStartOfDay(java.time.ZoneId.systemDefault()).toInstant());
            }
        } catch (Exception e) {
            throw new RuntimeException("Error parsing date key: " + key, e);
        }
    }

    public static byte[] generateDiagnosticsBarChart(String title, Map<String, Double> data) {
        try {
            CategoryChart chart = new CategoryChartBuilder()
                    .width(800)
                    .height(600)
                    .title(title)
                    .xAxisTitle("Attributes")
                    .yAxisTitle("Average Values")
                    .build();

            chart.addSeries("Average Diagnostics", new ArrayList<>(data.keySet()), new ArrayList<>(data.values()));
            chart.getStyler().setSeriesColors(new Color[]{Color.BLUE, Color.ORANGE, Color.GREEN, Color.RED});

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            BitmapEncoder.saveBitmap(chart, baos, BitmapEncoder.BitmapFormat.PNG);
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error generating bar chart", e);
        }
    }


    public static byte[] generateDiagnosticsPieChart(String title, Map<String, Double> diagnosticsAverages) {
        try {
            PieChart chart = new PieChartBuilder()
                    .width(800)
                    .height(600)
                    .title(title)
                    .build();

            diagnosticsAverages.forEach(chart::addSeries);

            chart.getStyler().setLegendVisible(true);
            chart.getStyler().setAnnotationDistance(0.8);
            chart.getStyler().setSeriesColors(new Color[]{Color.BLUE, Color.YELLOW, Color.GREEN, Color.RED});

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            BitmapEncoder.saveBitmap(chart, baos, BitmapEncoder.BitmapFormat.PNG);
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error generating diagnostics pie chart", e);
        }
    }

    public static byte[] generateBehaviorComparisonChart(String title, Map<String, Long> behaviorCounts) {
        try {
            // Create pie chart with fixed size
            PieChart chart = new PieChartBuilder()
                    .width(800)  // Standardized width
                    .height(600) // Standardized height
                    .title(title)
                    .build();

            // Define consistent color mapping for behavior labels
            Map<String, Color> behaviorColors = Map.of(
                    "Aggressive", new Color(237, 28, 36), // Red
                    "Normal", new Color(34, 177, 76),     // Green
                    "Slow", new Color(0, 162, 232)        // Blue
            );

            // Add series dynamically with fallback for unexpected labels
            for (Map.Entry<String, Long> entry : behaviorCounts.entrySet()) {
                chart.addSeries(entry.getKey(), entry.getValue());
            }

            // Styling options
            chart.getStyler().setLegendVisible(true);               // Show legend
            chart.getStyler().setAnnotationDistance(0.7);           // Ensure labels are readable
            chart.getStyler().setPlotContentSize(0.95);             // Maximize pie chart size
            chart.getStyler().setChartPadding(5);                   // Minimal padding
            chart.getStyler().setStartAngleInDegrees(90);           // Start at the top center
            chart.getStyler().setCircular(true);                    // Maintain circular shape
            chart.getStyler().setDrawAllAnnotations(true);          // Force all labels to render

            // Set colors dynamically
            Color[] seriesColors = behaviorCounts.keySet()
                    .stream()
                    .map(label -> behaviorColors.getOrDefault(label, Color.GRAY)) // Default to gray for unknown labels
                    .toArray(Color[]::new);
            chart.getStyler().setSeriesColors(seriesColors);

            // Export chart to byte array
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            BitmapEncoder.saveBitmap(chart, baos, BitmapEncoder.BitmapFormat.PNG);
            return baos.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error generating behavior comparison pie chart", e);
        }
    }


}








//package com.example;
//
//import org.knowm.xchart.*;
//
//
//import java.awt.*;
//import java.io.ByteArrayOutputStream;
//import java.text.SimpleDateFormat;
//import java.util.*;
//import java.util.List;
//import java.util.stream.Collectors;
//
//import static com.example.PdfUtils.categorizeFuelConsumption;import static com.example.PdfUtils.categorizeFuelConsumption;
//
//
//public class ChartUtils {
//
//
//    public static byte[] generateFuelConsumptionByMonthChart(Map<String, Double> monthlyData) {
//        try {
//            // Convert month strings to Date objects for the X-axis
//            SimpleDateFormat sdf = new SimpleDateFormat("MM-yyyy");
//            List<Date> months = new ArrayList<>();
//            List<Double> consumptions = new ArrayList<>();
//
//            for (Map.Entry<String, Double> entry : monthlyData.entrySet()) {
//                months.add(sdf.parse(entry.getKey())); // Parse the month string into Date
//                consumptions.add(entry.getValue());    // Add the corresponding consumption
//            }
//
//            // Sort the data by date
//            Collections.sort(months);
//            Collections.sort(consumptions);
//
//            // Create an XY chart
//            XYChart chart = new XYChartBuilder()
//                    .width(800)
//                    .height(600)
//                    .title("Fuel Consumption Over Months")
//                    .xAxisTitle("Month")
//                    .yAxisTitle("Fuel Consumption (Liters)")
//                    .build();
//
//            // Add series to the chart
//            chart.addSeries("Fuel Consumption", months, consumptions);
//
//            // Style the chart
//            chart.getStyler().setLegendVisible(true);
//            chart.getStyler().setChartBackgroundColor(Color.WHITE);
//            chart.getStyler().setXAxisLabelRotation(45);
//            chart.getStyler().setPlotGridLinesVisible(false);
//            chart.getStyler().setDatePattern("MMM yyyy"); // Format the date on the X-axis
//            chart.getStyler().setSeriesColors(new Color[]{new Color(250, 0, 63)});
//
//            // Export chart to byte array
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            BitmapEncoder.saveBitmap(chart, baos, BitmapEncoder.BitmapFormat.PNG);
//            return baos.toByteArray();
//        } catch (Exception e) {
//            throw new RuntimeException("Error generating chart", e);
//        }
//    }
//
//    public static byte[] generateFuelConsumptionByWeekChart(Map<String, Double> weeklyData) {
//        try {
//            // Convert week strings to Date objects for the X-axis
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-'W'ww");
//            List<Date> weeks = new ArrayList<>();
//            List<Double> consumptions = new ArrayList<>();
//
//            for (Map.Entry<String, Double> entry : weeklyData.entrySet()) {
//                weeks.add(sdf.parse(entry.getKey())); // Parse the week string into Date
//                consumptions.add(entry.getValue());  // Add the corresponding consumption
//            }
//
//            // Sort the data by date
//            List<Date> sortedWeeks = new ArrayList<>(weeks);
//            Collections.sort(sortedWeeks);
//
//            List<Double> sortedConsumptions = sortedWeeks.stream()
//                    .map(weeks::indexOf)
//                    .map(consumptions::get)
//                    .collect(Collectors.toList());
//
//            // Create an XY chart
//            XYChart chart = new XYChartBuilder()
//                    .width(800)
//                    .height(600)
//                    .title("Fuel Consumption Over Weeks")
//                    .xAxisTitle("Week")
//                    .yAxisTitle("Fuel Consumption (Liters)")
//                    .build();
//
//            // Add series to the chart
//            chart.addSeries("Fuel Consumption", sortedWeeks, sortedConsumptions);
//
//            // Style the chart
//            chart.getStyler().setLegendVisible(true);
//            chart.getStyler().setChartBackgroundColor(Color.WHITE);
//            chart.getStyler().setXAxisLabelRotation(45);
//            chart.getStyler().setPlotGridLinesVisible(false);
//            chart.getStyler().setDatePattern("yyyy-'W'ww"); // Format the date on the X-axis
//            chart.getStyler().setSeriesColors(new Color[]{new Color(250, 0, 63)}); // Red line
//
//            // Export chart to byte array
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            BitmapEncoder.saveBitmap(chart, baos, BitmapEncoder.BitmapFormat.PNG);
//            return baos.toByteArray();
//        } catch (Exception e) {
//            throw new RuntimeException("Error generating weekly fuel consumption chart", e);
//        }
//    }
//
//    public static byte[] generateFuelConsumptionComparisonChartForWeeks(Map<String, Double> weeklyData) {
//        try {
//            // Prepare data
//            List<String> weeks = new ArrayList<>(weeklyData.keySet());
//            List<Double> consumptions = new ArrayList<>(weeklyData.values());
//
//            // Create a bar chart
//            CategoryChart chart = new CategoryChartBuilder()
//                    .width(800)
//                    .height(600)
//                    .title("Weekly Fuel Consumption Comparison")
//                    .xAxisTitle("Week")
//                    .yAxisTitle("Fuel Consumption (Liters)")
//                    .build();
//
//            // Add data series
//            chart.addSeries("Fuel Consumption", weeks, consumptions);
//
//            // Style the chart
//            chart.getStyler().setLegendVisible(true);
//            chart.getStyler().setChartBackgroundColor(Color.WHITE);
//            chart.getStyler().setSeriesColors(new Color[]{new Color(254,215,221)});
//            chart.getStyler().setXAxisLabelRotation(45); // Rotate x-axis labels to 45 degrees
//
//            // Export chart to byte array
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            BitmapEncoder.saveBitmap(chart, baos, BitmapEncoder.BitmapFormat.PNG);
//            return baos.toByteArray();
//        } catch (Exception e) {
//            throw new RuntimeException("Error generating weekly fuel consumption comparison chart", e);
//        }
//    }
//
//    public static byte[] generateFuelConsumptionComparisonChartForMonths(Map<String, Double> monthlyData) {
//        try {
//            // Data for chart
//            List<String> months = new ArrayList<>(monthlyData.keySet());
//            List<Double> consumptions = new ArrayList<>(monthlyData.values());
//            List<String> categories = consumptions.stream()
//                    .map(consumption -> categorizeFuelConsumption(consumption))
//                    .collect(Collectors.toList());
//
//            // Create a bar chart
//            CategoryChart chart = new CategoryChartBuilder()
//                    .width(800)
//                    .height(600)
//                    .title("Fuel Consumption Comparison")
//                    .xAxisTitle("Month")
//                    .yAxisTitle("Fuel Consumption (Liters)")
//                    .build();
//
//            // Add data series
//            chart.addSeries("Fuel Consumption", months, consumptions);
//
//            // Style chart
//            chart.getStyler().setLegendVisible(true);
//            chart.getStyler().setChartBackgroundColor(java.awt.Color.WHITE);
//            chart.getStyler().setSeriesColors(new java.awt.Color[]{new Color(254,215,221)});
//            chart.getStyler().setXAxisLabelRotation(45);// Light red for consumption
//
//            // Convert to byte array
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            BitmapEncoder.saveBitmap(chart, baos, BitmapEncoder.BitmapFormat.PNG);
//            return baos.toByteArray();
//        } catch (Exception e) {
//            throw new RuntimeException("Error generating fuel consumption chart", e);
//        }
//    }
//
//
//
//}