package com.hypermarket.modules.components;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import com.hypermarket.data.DataStore;
import com.hypermarket.entities.Marketing;
import com.hypermarket.entities.Offer;
import com.hypermarket.entities.Report;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class MarketingDashboardController {

    private Marketing currentUser;

    @FXML private RadioButton reportsRadio;
    @FXML private RadioButton offersRadio;

    @FXML private VBox reportsSection;
    @FXML private VBox offersSection;

    @FXML private TextArea inputReportArea;
    @FXML private Button generateReportBtn;
    @FXML private VBox reportsContainer;

    @FXML private TextField offerNameField;
    @FXML private TextField discountField;
    @FXML private RadioButton rbAll;
    @FXML private RadioButton rbProduct;
    @FXML private ComboBox<String> productCombo;
    @FXML private DatePicker startDatePicker;
    @FXML private DatePicker endDatePicker;
    @FXML private Button addOfferBtn;
    @FXML private VBox offersContainer;

    @FXML
    public void initialize() {
        
        offersSection.setVisible(false);
        offersSection.setManaged(false);

        
        reportsSection.setVisible(true);
        reportsSection.setManaged(true);

        
        ToggleGroup sectionGroup = new ToggleGroup();
        reportsRadio.setToggleGroup(sectionGroup);
        offersRadio.setToggleGroup(sectionGroup);

        ToggleGroup offerGroup = new ToggleGroup();
        rbAll.setToggleGroup(offerGroup);
        rbProduct.setToggleGroup(offerGroup);
        rbAll.setSelected(true);

        
        reportsRadio.setOnAction(e -> toggleSections(true));
        offersRadio.setOnAction(e -> toggleSections(false));

        
        generateReportBtn.setOnAction(e -> addReport());
        addOfferBtn.setOnAction(e -> addOffer());

        
        loadReports();
    }

    private void toggleSections(boolean showReports) {
        if (showReports) {
            reportsSection.setVisible(true);
            reportsSection.setManaged(true);
            offersSection.setVisible(false);
            offersSection.setManaged(false);
            loadReports();
        } else {
            reportsSection.setVisible(false);
            reportsSection.setManaged(false);
            offersSection.setVisible(true);
            offersSection.setManaged(true);
            loadOffers();
        }
    }

    private void loadReports() {
        reportsContainer.getChildren().clear();
        for (Report r : DataStore.getDataStore().getReports()) {
            VBox card = createReportCard(r);
            reportsContainer.getChildren().add(card);
        }
    }

    private void loadOffers() {
        offersContainer.getChildren().clear();
        for (Offer o : DataStore.getDataStore().getOffers()) {
            VBox card = createOfferCard(o);
            offersContainer.getChildren().add(card);
        }
    }

    private VBox createReportCard(Report r) {
        VBox card = new VBox(5);
        card.getStyleClass().add("card");
        Label title = new Label(r.getReportTitle());
        title.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        Label content = new Label(r.getContent());
        card.getChildren().addAll(title, content);
        return card;
    }

    private VBox createOfferCard(Offer o) {
        VBox card = new VBox(5);
        card.getStyleClass().add("card");
        Label name = new Label(o.getOfferName());
        name.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        Label discount = new Label("Discount: " + o.getDiscount() + "%");
        Label dates = new Label("From: " + o.getStartDate() + " To: " + o.getEndDate());
        String targetText = o.getTargetType().equals("ALL") 
            ? "Target: All Products" 
            : "Target: " + o.getTargetValue(); // هنا نعرض الـ targetValue إذا كان المنتج محدد
    
        Label target = new Label(targetText);
        card.getChildren().addAll(name, discount, dates, target);
        return card;
    }

    private void addReport() {
        String content = inputReportArea.getText().trim();
        if (content.isEmpty() || currentUser == null) return;

        Report r = currentUser.saveCustomReport(0,
                "Custom Marketing Report - " + java.time.LocalDateTime.now(),
                0,0,0,0,
                new Date(),
                content);

        DataStore.getDataStore().getReports().add(r);
        DataStore.getDataStore().saveAllData();

        reportsContainer.getChildren().add(createReportCard(r));
        inputReportArea.clear();
    }

    private void addOffer() {
        if (currentUser == null) return;
        try {
            String name = offerNameField.getText().trim();
            double discount = Double.parseDouble(discountField.getText().trim());
            LocalDate startL = startDatePicker.getValue();
            LocalDate endL = endDatePicker.getValue();
            if (name.isEmpty() || startL == null || endL == null) return;

            Date start = Date.from(startL.atStartOfDay(ZoneId.systemDefault()).toInstant());
            Date end = Date.from(endL.atStartOfDay(ZoneId.systemDefault()).toInstant());

            String targetType = rbAll.isSelected() ? "ALL" : "PRODUCT";
            String targetVal = rbProduct.isSelected() && productCombo.getValue() != null
                    ? productCombo.getValue() : "ALL";

            Offer o = currentUser.createOffer(name, discount, start, end, targetType, targetVal);

            DataStore.getDataStore().getOffers().add(o);
            DataStore.getDataStore().saveAllData();

            offersContainer.getChildren().add(createOfferCard(o));

            // Clear inputs
            offerNameField.clear();
            discountField.clear();
            startDatePicker.setValue(null);
            endDatePicker.setValue(null);
            rbAll.setSelected(true);
            rbProduct.setSelected(false);
            productCombo.setValue(null);

        } catch (NumberFormatException ex) {
            System.err.println("Invalid discount input");
        }
    }

    public void setCurrentUser(Marketing user) {
        this.currentUser = user;
    }

    

}
