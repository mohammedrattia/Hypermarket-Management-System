package com.hypermarket.modules.components;

import org.kordamp.ikonli.javafx.FontIcon;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class KpiCardController {
    @FXML
    private Label valueLabel;
    @FXML
    private Label titleLabel;
    @FXML
    private Label trendLabel;
    @FXML
    private FontIcon trendIcon;

    public void setData(String title, String value, String trend, boolean isPositive) {
        titleLabel.setText(title);
        valueLabel.setText(value);
        trendLabel.setText(trend);

        if (isPositive) {
            trendIcon.setIconLiteral("fas-arrow-up");
            trendIcon.getStyleClass().add("kpi-trend-icon-positive");
            trendLabel.getStyleClass().add("kpi-trend-positive");
        } else {
            trendIcon.setIconLiteral("fas-arrow-down");
            trendIcon.getStyleClass().add("kpi-trend-icon-negative");
            trendLabel.getStyleClass().add("kpi-trend-negative");
        }
    }
}
