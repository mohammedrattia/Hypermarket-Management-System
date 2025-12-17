package com.hypermarket.modules.components;

import com.hypermarket.entities.Order;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class BarChartController {

    @FXML
    private BarChart<String, Number> barChart;

    public void setData(List<Order> allMyOrders) {
        barChart.getData().clear();

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Revenue");

        LocalDate today = LocalDate.now();
        DateTimeFormatter dayFmt = DateTimeFormatter.ofPattern("MMM dd");

        // 1. Populate Data
        for (int i = 6; i >= 0; i--) {
            LocalDate dateToCheck = today.minusDays(i);
            double dayRevenue = 0;

            for (Order o : allMyOrders) {
                if (o.getDateTime().toLocalDate().equals(dateToCheck)) {
                    dayRevenue += o.getTotalPrice();
                }
            }
            series.getData().add(new XYChart.Data<>(dateToCheck.format(dayFmt), dayRevenue));
        }

        barChart.getData().add(series);

        applyGradient(series);
    }

    private void applyGradient(XYChart.Series<String, Number> series) {
        int totalBars = series.getData().size();

        int startR = 209, startG = 250, startB = 229;

        int endR = 16, endG = 185, endB = 129;

        for (int i = 0; i < totalBars; i++) {
            XYChart.Data<String, Number> data = series.getData().get(i);
            Node bar = data.getNode();

            if (bar != null) {
                double t = (totalBars > 1) ? (double) i / (totalBars - 1) : 1.0;
                int r = (int) (startR + (endR - startR) * t);
                int g = (int) (startG + (endG - startG) * t);
                int b = (int) (startB + (endB - startB) * t);

                String hexColor = String.format("#%02x%02x%02x", r, g, b);

                bar.setStyle("-fx-bar-fill: " + hexColor + ";");

                String normalColor = hexColor;
                bar.setOnMouseEntered(e -> bar.setStyle("-fx-bar-fill: derive(" + normalColor + ", -15%);"));
                bar.setOnMouseExited(e -> bar.setStyle("-fx-bar-fill: " + normalColor + ";"));
            }
            displayLabelForData(data);

        }

    }

    private void displayLabelForData(XYChart.Data<String, Number> data) {
        Node bar = data.getNode();

        if (bar instanceof StackPane) {
            StackPane barPane = (StackPane) bar;

            javafx.scene.text.Text dataText = new javafx.scene.text.Text(
                    String.format("$%.0f", data.getYValue().doubleValue()));
            dataText.setStyle("-fx-font-size: 10px; -fx-font-weight: bold; -fx-fill: #2a2a2a;");

            barPane.getChildren().add(dataText);

            StackPane.setAlignment(dataText, javafx.geometry.Pos.TOP_CENTER);
            dataText.setTranslateY(-15);

            barPane.setClip(null);
        }
    }
}