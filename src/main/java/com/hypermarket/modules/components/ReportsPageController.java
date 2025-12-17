package com.hypermarket.modules.components;

import com.hypermarket.data.DataStore;
import com.hypermarket.entities.Marketing;
import com.hypermarket.entities.Report;
import com.hypermarket.service.Session;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.scene.control.Alert;

import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

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
        if (currentUser == null)
            return;
        String content = inputReportArea.getText().trim();

        if (content == null || content.trim().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Empty Report");
            alert.setHeaderText("Report Content Missing");
            alert.setContentText("You didn't write anything in the report!");

            alert.showAndWait();
            return;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String formattedDate = LocalDateTime.now().format(formatter);

        Report r = currentUser.saveCustomReport(
                0,
                "Custom Marketing Report - " + formattedDate,
                content);

        reportsContainer.getChildren().add(createReportCard(r));
        inputReportArea.clear();

        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
        successAlert.setTitle("Report Added");
        successAlert.setHeaderText("Report Added Successfully");
        successAlert.setContentText("The report has been added successfully!");
        successAlert.showAndWait();

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