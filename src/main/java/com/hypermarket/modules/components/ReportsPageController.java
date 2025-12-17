package com.hypermarket.modules.components;

import com.hypermarket.data.DataStore;
import com.hypermarket.entities.Marketing;
import com.hypermarket.entities.Report;
import com.hypermarket.service.Session;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.control.Alert;

import javafx.animation.FadeTransition;
import javafx.util.Duration;

public class ReportsPageController {

    private Marketing currentUser;

    @FXML
    private TextArea inputReportArea;
    @FXML
    private Button generateReportBtn;
    @FXML
    private VBox reportsContainer;

    @FXML
    public void initialize() { 
         // Initialize user exactly like old controller
        currentUser = (Marketing) Session.getInstance().getUser();

        generateReportBtn.setOnAction(e -> addReport());
        loadReports();
    }

    private void loadReports() {
        reportsContainer.getChildren().clear();
        for (Report r : DataStore.getDataStore().getReports()) {
            VBox card = createReportCard(r);
            reportsContainer.getChildren().add(card);
        }
    }

    private void addReport() {
        if (currentUser == null) return;
        String content = inputReportArea.getText().trim();
        if (content == null || content.trim().isEmpty()) {

        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Empty Report");
        alert.setHeaderText(null);
        alert.setContentText("You didn't write anything in the report!");

        alert.showAndWait();
        return;
    }

        Report r = currentUser.saveCustomReport(
                0,
                "Custom Marketing Report - " + java.time.LocalDateTime.now(),
                content
        );

        reportsContainer.getChildren().add(createReportCard(r));
        inputReportArea.clear();

        showToast("Report added successfully ");

    }
    private void showToast(String message) {
    Label toast = new Label(message);
    toast.setStyle(
        "-fx-background-color: #4CAF50;" +
        "-fx-text-fill: white;" +
        "-fx-padding: 10 20;" +
        "-fx-background-radius: 20;" +
        "-fx-font-weight: bold;"
    );
    toast.setOpacity(0);

    reportsContainer.getChildren().add(toast);

    FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.5), toast);
    fadeIn.setFromValue(0);
    fadeIn.setToValue(1);

    
    FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.5), toast);
    fadeOut.setFromValue(1);
    fadeOut.setToValue(0);
    fadeOut.setDelay(Duration.seconds(2));

    fadeOut.setOnFinished(e -> reportsContainer.getChildren().remove(toast));

    fadeIn.play();
    fadeOut.play();
}


    private VBox createReportCard(Report r) {
        VBox card = new VBox(5);
        card.getStyleClass().add("card");
        
        Label title = new Label(r.getReportTitle());
        title.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        Label total = new Label("Total Offers: " + r.getTotalOffers());
        Label active = new Label("Active Offers: " + r.getActiveOffers());
        Label expired = new Label("Expired Offers: " + r.getExpiredOffers());
        Label maxDisc = new Label("Max Discount: " + r.getMaxDiscount() + "%");
        Label content = new Label(r.getContent());

        card.getChildren().addAll(title, total, active, expired, maxDisc, content);
        return card;
    }
}