package com.hypermarket.modules.marketing;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import com.hypermarket.data.DataStore;
import com.hypermarket.entities.Marketing;
import com.hypermarket.entities.Offer;
import com.hypermarket.entities.Product;
import com.hypermarket.entities.Offer.Status;
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
    private ComboBox<String> productCombo;
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private DatePicker endDatePicker;
    @FXML
    private Button addOfferBtn;
    @FXML
    private VBox offersContainer;

    private Product selectedProduct;

    @FXML
    public void initialize() {

        currentUser = (Marketing) Session.getInstance().getUser();

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

        selectedProduct = null;

        try {
            String name = offerNameField.getText().trim();
            double discount = Double.parseDouble(discountField.getText().trim());
            LocalDate startL = startDatePicker.getValue();
            LocalDate endL = endDatePicker.getValue();

            if (name.isEmpty() || startL == null || endL == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Missing Fields");
                alert.setHeaderText(null);
                alert.setContentText("Please fill in all fields before adding the offer.");
                alert.showAndWait();
                return;
            }

            Date start = Date.from(startL.atStartOfDay(ZoneId.systemDefault()).toInstant());
            Date end = Date.from(endL.atStartOfDay(ZoneId.systemDefault()).toInstant());

            if (end.before(start)) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Invalid Dates");
                alert.setHeaderText(null);
                alert.setContentText("End date must be after start date.");
                alert.showAndWait();
                return;
            }

            String targetVal = productCombo.getValue() != null
                    ? productCombo.getValue()
                    : null;

            if (targetVal != null) {
                String productName = productCombo.getValue();
                selectedProduct = DataStore.getDataStore().getProducts()
                        .stream()
                        .filter(p -> p.getName().equals(productName))
                        .findFirst()
                        .orElse(null);
            }

            Offer.Status status = Offer.Status.ACTIVE;

            if (selectedProduct != null) {
                boolean rejected = false;
                boolean pending = false;

                for (Offer o : DataStore.getDataStore().getOffers()) {
                    if (o.getProduct() != null &&
                            o.getProduct().getProductID() == selectedProduct.getProductID() &&
                            o.getManualStatus() == Offer.Status.ACTIVE) {

                        if (!start.before(o.getStartDate()) && !end.after(o.getEndDate())) {
                            rejected = true;
                            break;
                        }

                        if (!start.before(o.getStartDate()) && end.after(o.getEndDate())) {
                            pending = true;
                        }
                    }
                }

                if (rejected) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Offer Conflict");
                    alert.setHeaderText("Rejected");
                    alert.setContentText(
                            "Cannot add offer. This product already has an active offer in the selected period.");
                    alert.showAndWait();
                    return;
                }

                if (pending || start.after(new Date())) {
                    status = Offer.Status.PENDING;
                } else {
                    status = Offer.Status.ACTIVE;
                }

            } else {

                if (start.after(new Date())) {
                    status = Offer.Status.PENDING;
                }
            }

            Offer o = currentUser.createOffer(name, discount, start, end, selectedProduct);
            o.setManualStatus(status);

            if (selectedProduct != null) {
                selectedProduct.setOffer(o);
            }

            DataStore.getDataStore().saveAllData();

            offersContainer.getChildren().clear();
            DataStore.getDataStore().getOffers().stream()
                    .sorted((a, b) -> Integer.compare(getStatusOrder(a.getManualStatus()),
                            getStatusOrder(b.getManualStatus())))
                    .forEach(o2 -> offersContainer.getChildren().add(createOfferCard(o2)));

            offerNameField.clear();
            discountField.clear();
            startDatePicker.setValue(null);
            endDatePicker.setValue(null);
            productCombo.setValue(null);

        } catch (NumberFormatException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Discount");
            alert.setHeaderText(null);
            alert.setContentText("Please enter a valid positive number for Discount!");
            alert.showAndWait();
        }
    }

    private int getStatusOrder(Offer.Status status) {
        return switch (status) {
            case ACTIVE -> 1;
            case PENDING -> 2;
            case EXPIRED -> 3;
        };
    }

    private VBox createOfferCard(Offer o) {
        VBox card = new VBox(8);
        card.getStyleClass().add("card");

        Label name = new Label(o.getOfferName());
        name.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        Label productName = new Label("Product Name: " + o.getProduct().getName());
        Label discount = new Label("Discount: " + o.getDiscount() + "%");
        Label dates = new Label("From: " + o.getStartDate() + " To: " + o.getEndDate());

        Label statusLabel = new Label("Status: " + o.getManualStatus());
        updateStatusLabelStyle(statusLabel, o.getManualStatus());

        ComboBox<Offer.Status> statusCombo = new ComboBox<>();
        statusCombo.getItems().addAll(Offer.Status.values());
        statusCombo.setValue(o.getManualStatus());
        statusCombo.setPrefWidth(120);
        statusCombo.getStyleClass().add("status-combo");

        statusCombo.setOnAction(e -> {
            if (o.getProduct().getOffer() != null && statusCombo.getValue() == Status.ACTIVE) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Offer Conflict");
                alert.setHeaderText("Rejected");
                alert.setContentText(
                        "Cannot make offer active. This product already has an active offer in the selected period.");
                alert.showAndWait();
                statusCombo.setValue(Status.PENDING);
                return;
            }
            if (statusCombo.getValue() == Status.ACTIVE)
                o.getProduct().setOffer(o);
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
                    boolean deleted = currentUser.deleteOffer(o);
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
                productName,
                discount,
                dates,
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