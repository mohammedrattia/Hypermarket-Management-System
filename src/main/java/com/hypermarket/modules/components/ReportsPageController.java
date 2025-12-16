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
        String content = inputReportArea.getText().trim();
        if (content.isEmpty() || currentUser == null) return;

        Report r = currentUser.saveCustomReport(
                0,
                "Custom Marketing Report - " + java.time.LocalDateTime.now(),
                content
        );

        reportsContainer.getChildren().add(createReportCard(r));
        inputReportArea.clear();
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