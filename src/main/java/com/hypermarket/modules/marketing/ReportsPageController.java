package com.hypermarket.modules.marketing;

import com.hypermarket.data.DataStore;
import com.hypermarket.entities.Marketing;
import com.hypermarket.entities.Report;
import com.hypermarket.service.Session;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.kordamp.ikonli.javafx.FontIcon;

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
        currentUser = (Marketing) Session.getInstance().getUser();

        generateReportBtn.setOnAction(e -> addReport());
        loadReports();
    }

    private void loadReports() {
        reportsContainer.getChildren().clear();
        for (Report r : DataStore.getDataStore().getReports()) {
            VBox card = createReportCard(r);
            reportsContainer.getChildren().add(0, card);
        }
    }

    private void addReport() {
        if (currentUser == null)
            return;

        String content = inputReportArea.getText().trim();

        if (content == null || content.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Empty Report", "Report Content Missing",
                    "You didn't write anything in the report!");
            return;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        String formattedDate = LocalDateTime.now().format(formatter);

        Report r = currentUser.saveCustomReport(
                0,
                "Custom Marketing Report - " + formattedDate,
                content);

        reportsContainer.getChildren().add(0, createReportCard(r));

        inputReportArea.clear();
        showAlert(Alert.AlertType.INFORMATION, "Report Added", "Success", "The report has been posted successfully!");
    }

    private VBox createReportCard(Report r) {
        VBox card = new VBox();
        card.getStyleClass().add("report-card");
        card.setSpacing(10);

        HBox header = new HBox();
        header.setSpacing(10);
        header.setAlignment(Pos.CENTER_LEFT);

        Label badgeLabel = new Label("REPORT");
        badgeLabel.getStyleClass().add("report-id-badge");

        String dateText = r.getReportTitle().replace("Custom Marketing Report - ", "");
        Label dateLabel = new Label(dateText);
        dateLabel.getStyleClass().add("report-header-date");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        header.getChildren().addAll(badgeLabel, dateLabel, spacer);

        Label contentLabel = new Label(r.getContent());
        contentLabel.setWrapText(true);
        contentLabel.getStyleClass().add("report-content-text");

        contentLabel.setMaxWidth(Double.MAX_VALUE);
        contentLabel.setMinHeight(Region.USE_PREF_SIZE);

        ScrollPane textScroll = new ScrollPane(contentLabel);
        textScroll.setFitToWidth(true);
        textScroll.getStyleClass().add("card-text-scroll");

        textScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        textScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        textScroll.prefHeightProperty().bind(Bindings.min(contentLabel.heightProperty(), 85));

        textScroll.setMinHeight(Region.USE_PREF_SIZE);

        HBox statsBox = new HBox(15);
        if (r.getActiveOffers() > 0 || r.getExpiredOffers() > 0) {
            Label activeStat = new Label("Active: " + r.getActiveOffers());
            activeStat.setStyle("-fx-text-fill: #2e7d32; -fx-font-size: 11px; -fx-font-weight: bold;");

            Label expiredStat = new Label("Expired: " + r.getExpiredOffers());
            expiredStat.setStyle("-fx-text-fill: #c62828; -fx-font-size: 11px; -fx-font-weight: bold;");

            statsBox.getChildren().addAll(activeStat, expiredStat);
        }

        FontIcon trashIcon = new FontIcon("fas-trash-alt");
        trashIcon.getStyleClass().add("delete-icon-view");

        Button deleteBtn = new Button("", trashIcon);
        deleteBtn.getStyleClass().add("delete-icon-btn");

        deleteBtn.setOnAction(e -> {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Delete Report");
            alert.setHeaderText("Delete this report?");
            alert.setContentText("Are you sure you want to delete this report?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                reportsContainer.getChildren().remove(card);
                DataStore.getDataStore().getReports().remove(r);
                DataStore.getDataStore().saveAllData();
            }
        });

        HBox bottomBar = new HBox();
        bottomBar.setAlignment(Pos.CENTER_RIGHT);

        Region bottomSpacer = new Region();
        HBox.setHgrow(bottomSpacer, Priority.ALWAYS);

        if (!statsBox.getChildren().isEmpty()) {
            bottomBar.getChildren().addAll(statsBox, bottomSpacer, deleteBtn);
        } else {
            bottomBar.getChildren().add(deleteBtn);
        }

        card.getChildren().addAll(header, textScroll, bottomBar);

        return card;
    }

    private void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}