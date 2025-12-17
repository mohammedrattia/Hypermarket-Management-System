package com.hypermarket.modules.components;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import com.hypermarket.data.DataStore;
import com.hypermarket.entities.Marketing;
import com.hypermarket.entities.Offer;
import com.hypermarket.service.Session;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import org.kordamp.ikonli.fontawesome5.FontAwesomeSolid;
import org.kordamp.ikonli.javafx.FontIcon;

public class OffersPageController {

    private Marketing currentUser;

    @FXML
    private TextField offerNameField;
    @FXML
    private TextField discountField;
    @FXML
    private RadioButton rbAll;
    @FXML
    private RadioButton rbProduct;
    @FXML
    private ComboBox<String> productCombo;
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private DatePicker endDatePicker;
    @FXML
    private Button addOfferBtn;
    @FXML
    private VBox offersContainer;

    @FXML
    public void initialize() {

        currentUser = (Marketing) Session.getInstance().getUser();

        ToggleGroup offerGroup = new ToggleGroup();
        rbAll.setToggleGroup(offerGroup);
        rbProduct.setToggleGroup(offerGroup);
        rbAll.setSelected(true);

        addOfferBtn.setOnAction(e -> addOffer());

        loadOffers();
        loadProductCombo();
    }

    private void loadOffers() {
        offersContainer.getChildren().clear();
        for (Offer o : DataStore.getDataStore().getOffers()) {
            VBox card = createOfferCard(o);
            offersContainer.getChildren().add(card);
        }
    }

    private void addOffer() {
        if (currentUser == null)
            return;
        try {
            String name = offerNameField.getText().trim();
            double discount = Double.parseDouble(discountField.getText().trim());
            LocalDate startL = startDatePicker.getValue();
            LocalDate endL = endDatePicker.getValue();
            if (name.isEmpty() || startL == null || endL == null)
                return;

            Date start = Date.from(startL.atStartOfDay(ZoneId.systemDefault()).toInstant());
            Date end = Date.from(endL.atStartOfDay(ZoneId.systemDefault()).toInstant());

            String targetType = rbAll.isSelected() ? "ALL" : "PRODUCT";
            String targetVal = rbProduct.isSelected() && productCombo.getValue() != null
                    ? productCombo.getValue()
                    : "ALL";

            Offer o = currentUser.createOffer(name, discount, start, end, targetType, targetVal);

            offersContainer.getChildren().add(createOfferCard(o));

            offerNameField.clear();
            discountField.clear();
            startDatePicker.setValue(null);
            endDatePicker.setValue(null);
            rbAll.setSelected(true);
            rbProduct.setSelected(false);
            productCombo.setValue(null);

        } catch (NumberFormatException ex) {
              Alert alert = new Alert(Alert.AlertType.ERROR);
              alert.setTitle("Invalid Discount");
              alert.setHeaderText(null);
              alert.setContentText("Please enter a valid positive number for Discount!");
              alert.showAndWait();
              return;
}

    }

    private VBox createOfferCard(Offer o) {
        VBox card = new VBox(8);
        card.getStyleClass().add("card");

        Label name = new Label(o.getOfferName());
        name.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        Label discount = new Label("Discount: " + o.getDiscount() + "%");
        Label dates = new Label("From: " + o.getStartDate() + " To: " + o.getEndDate());

        String targetText = o.getTargetType().equalsIgnoreCase("ALL")
                ? "Target: All Products"
                : "Target: " + o.getTargetValue();

        Label target = new Label(targetText);

        Label statusLabel = new Label("Status: " + o.getManualStatus());
        updateStatusLabelStyle(statusLabel, o.getManualStatus());

        ComboBox<Offer.Status> statusCombo = new ComboBox<>();
        statusCombo.getItems().addAll(Offer.Status.values());
        statusCombo.setValue(o.getManualStatus());
        statusCombo.setPrefWidth(120);
        statusCombo.getStyleClass().add("status-combo");

        statusCombo.setOnAction(e -> {
            o.setManualStatus(statusCombo.getValue());
            statusLabel.setText("Status: " + o.getManualStatus());
            updateStatusLabelStyle(statusLabel, o.getManualStatus());
            DataStore.getDataStore().saveAllData();
        });

        Button deleteBtn = new Button();
        FontIcon trashIcon = new FontIcon(FontAwesomeSolid.TRASH);
        trashIcon.setIconSize(16);
        deleteBtn.setGraphic(trashIcon);
        deleteBtn.getStyleClass().add("delete-btn");

        deleteBtn.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Offer");
            alert.setHeaderText("Are you sure?");
            alert.setContentText("This offer will be permanently deleted.");

            ButtonType yesBtn = new ButtonType("Delete");
            ButtonType cancelBtn = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().setAll(yesBtn, cancelBtn);

            alert.showAndWait().ifPresent(response -> {
                if (response == yesBtn) {
                    boolean deleted = currentUser.deleteOffer(o.getOfferID());
                    if (deleted) {
                        offersContainer.getChildren().remove(card);
                    }
                }
            });
        });

        HBox actionsRow = new HBox(8);
        actionsRow.getChildren().addAll(statusLabel, statusCombo);
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        actionsRow.getChildren().addAll(spacer, deleteBtn);
        card.getChildren().addAll(
                name,
                discount,
                dates,
                target,
                actionsRow);

        return card;
    }

    private void updateStatusLabelStyle(Label label, Offer.Status status) {
        switch (status) {
            case ACTIVE -> label.setStyle("-fx-font-weight: bold; -fx-text-fill: green;");
            case EXPIRED -> label.setStyle("-fx-font-weight: bold; -fx-text-fill: red;");
            case PENDING -> label.setStyle("-fx-font-weight: bold; -fx-text-fill: orange;");
        }
    }

    private void loadProductCombo() {
        productCombo.getItems().clear();
        for (var p : DataStore.getDataStore().getProducts()) {
            productCombo.getItems().add(p.getName());
        }
    }
}