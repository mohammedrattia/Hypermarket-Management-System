package com.hypermarket.modules.marketing;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList; // Import needed for list copy
import java.util.Collections; // Import needed for reversing
import java.util.Date;
import java.util.List; // Import needed for list
import java.util.Optional;

import com.hypermarket.data.DataStore;
import com.hypermarket.data.FileManager;
import com.hypermarket.entities.Marketing;
import com.hypermarket.entities.Offer;
import com.hypermarket.entities.Product;
import com.hypermarket.entities.Offer.Status;
import com.hypermarket.service.Session;
import com.hypermarket.service.Toast;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
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

        // 1. Create a copy of the list so we don't affect the DataStore directly
        List<Offer> allOffers = new ArrayList<>(DataStore.getDataStore().getOffers());

        // 2. Reverse the copy.
        // This ensures that when we sort, if two offers have the same Date & Status,
        // the one added LAST (the newest one) appears FIRST.
        Collections.reverse(allOffers);

        allOffers.stream()
                .sorted((a, b) -> {
                    // Primary Sort: Status
                    int statusA = getStatusOrder(a.getManualStatus());
                    int statusB = getStatusOrder(b.getManualStatus());

                    if (statusA != statusB) {
                        return Integer.compare(statusA, statusB);
                    }

                    // Secondary Sort: Date Descending
                    if (a.getStartDate() != null && b.getStartDate() != null) {
                        return b.getStartDate().compareTo(a.getStartDate());
                    }
                    return 0;
                })
                .forEach(o -> offersContainer.getChildren().add(createOfferCard(o)));
    }

    private VBox createOfferCard(Offer o) {
        VBox card = new VBox();
        card.getStyleClass().add("report-card");
        card.setSpacing(10);

        HBox header = new HBox(10);
        header.setAlignment(Pos.CENTER_LEFT);

        Label badgeLabel = new Label("OFFER");
        badgeLabel.getStyleClass().add("report-id-badge");

        Label titleLabel = new Label(o.getOfferName());
        titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: #2d2d2d;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label discountBadge = new Label("-" + (int) o.getDiscount() + "%");
        discountBadge.getStyleClass().add("offer-discount-badge");

        header.getChildren().addAll(badgeLabel, titleLabel, spacer, discountBadge);

        VBox detailsBox = new VBox(5);

        String productNameString = "General Offer (No Product)";
        if (o.getProduct() != null) {
            productNameString = o.getProduct().getName();
        }

        HBox productRow = new HBox(5);
        Label pLabel = new Label("Product:");
        pLabel.getStyleClass().add("offer-detail-label");
        Label pValue = new Label(productNameString);
        pValue.getStyleClass().add("offer-value-text");
        productRow.getChildren().addAll(pLabel, pValue);

        HBox dateRow = new HBox(5);
        Label dLabel = new Label("Duration:");
        dLabel.getStyleClass().add("offer-detail-label");

        String startStr = "";
        String endStr = "";

        if (o.getStartDate() != null) {
            startStr = FileManager.localDateFormat.format(o.getStartDate().toInstant().atZone(ZoneId.systemDefault()));
        }
        if (o.getEndDate() != null) {
            endStr = FileManager.localDateFormat.format(o.getEndDate().toInstant().atZone(ZoneId.systemDefault()));
        }

        Label dValue = new Label(startStr + "  ➜  " + endStr);
        dValue.getStyleClass().add("offer-value-text");
        dateRow.getChildren().addAll(dLabel, dValue);

        detailsBox.getChildren().addAll(productRow, dateRow);

        HBox footer = new HBox(10);
        footer.setAlignment(Pos.CENTER_LEFT);

        ComboBox<Offer.Status> statusCombo = new ComboBox<>();
        statusCombo.getItems().addAll(Offer.Status.values());
        statusCombo.setValue(o.getManualStatus());
        statusCombo.getStyleClass().add("status-combo-box");

        statusCombo.setButtonCell(new ListCell<>() {
            @Override
            protected void updateItem(Offer.Status item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setTextFill(javafx.scene.paint.Color.BLACK);
                } else {
                    setText(item.toString());
                    switch (item) {
                        case ACTIVE -> setTextFill(javafx.scene.paint.Color.web("#2e7d32"));
                        case PENDING -> setTextFill(javafx.scene.paint.Color.web("#ef6c00"));
                        case EXPIRED -> setTextFill(javafx.scene.paint.Color.web("#c62828"));
                    }
                    setStyle("-fx-font-weight: bold; -fx-font-size: 11px;");
                }
            }
        });

        statusCombo.setOnAction(e -> {
            if (o.getProduct() != null) {
                if (o.getProduct().getOffer() != null && statusCombo.getValue() == Status.ACTIVE
                        && o.getProduct().getOffer() != o) {
                    showAlert(Alert.AlertType.WARNING, "Conflict", "Rejected",
                            "This product already has an active offer.");
                    statusCombo.setValue(Status.PENDING);
                    return;
                }

                if (statusCombo.getValue() == Status.ACTIVE) {
                    o.getProduct().setOffer(o);
                } else if (o.getProduct().getOffer() == o) {
                    o.getProduct().setOffer(null);
                }
            }

            o.setManualStatus(statusCombo.getValue());
            DataStore.getDataStore().saveAllData();
            loadOffers();
        });

        FontIcon trashIcon = new FontIcon("fas-trash-alt");
        trashIcon.getStyleClass().add("delete-icon-view");

        Button deleteBtn = new Button("", trashIcon);
        deleteBtn.getStyleClass().add("delete-icon-btn");

        deleteBtn.setOnAction(e -> {
            String confirmDeleteMsg = "Are you sure you want to delete this offer?";
            Optional<ButtonType> deleteDecision = Toast.showToast(confirmDeleteMsg,
                    Toast.NotificationType.CONFIRMATION);
            if (deleteDecision.isPresent() && deleteDecision.get() == ButtonType.YES) {
                boolean deleted = currentUser.deleteOffer(o);
                if (deleted) {
                    offersContainer.getChildren().remove(card);
                    if (o.getProduct() != null && o.getProduct().getOffer() == o) {
                        o.getProduct().setOffer(null);
                    }
                    DataStore.getDataStore().saveAllData();
                    String deleteSuccessMsg = "Offer deleted successfully.";
                    Toast.showToast(deleteSuccessMsg, Toast.NotificationType.INFORMATION);
                }
            }
        });

        Region footerSpacer = new Region();
        HBox.setHgrow(footerSpacer, Priority.ALWAYS);

        footer.getChildren().addAll(statusCombo, footerSpacer, deleteBtn);

        card.getChildren().addAll(header, detailsBox, footer);
        return card;
    }

    private void addOffer() {
        if (currentUser == null)
            return;
        selectedProduct = null;

        if (!validateInput()) {
            return;
        }

        try {
            String name = offerNameField.getText().trim();
            String discountStr = discountField.getText().trim();

            double discount = Double.parseDouble(discountStr);
            Date start = Date.from(startDatePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
            Date end = Date.from(endDatePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());

            if (productCombo.getValue() != null) {
                String pName = productCombo.getValue();
                selectedProduct = DataStore.getDataStore().getProducts().stream()
                        .filter(p -> p.getName().equals(pName)).findFirst().orElse(null);
            }

            Status status = Status.ACTIVE;
            if (start.after(new Date()))
                status = Status.PENDING;

            Offer o = currentUser.createOffer(name, discount, start, end, selectedProduct);
            o.setManualStatus(status);

            if (status == Status.ACTIVE)
                selectedProduct.setOffer(o);

            DataStore.getDataStore().saveAllData();
            loadOffers();

            offerNameField.clear();
            discountField.clear();
            startDatePicker.setValue(null);
            endDatePicker.setValue(null);
            productCombo.setValue(null);

            String addOfferSuccessMsg = "New Offer added successfully.";
            Toast.showToast(addOfferSuccessMsg, Toast.NotificationType.INFORMATION);

        } catch (NumberFormatException ex) {
            ex.printStackTrace();
        }
    }

    private boolean validateInput() {
        String errorMessage = "";
        if (offerNameField.getText().isEmpty() || discountField.getText().isEmpty()
                || startDatePicker.getValue() == null
                || endDatePicker.getValue() == null) {
            errorMessage = "Please fill in all required fields.";
        } else if (!offerNameField.getText().matches("^.*\\S.*$")) {
            errorMessage = "Invalid name.";
        } else if (!discountField.getText().matches("^\\d+(\\.\\d{1,})?$")) {
            errorMessage = "Invalid discount entered.";
        } else if (Double.parseDouble(discountField.getText()) <= 0) {
            errorMessage = "discount must be greater than 0.";
        } else if (!startDatePicker.getValue().isAfter(LocalDate.now())) {
            errorMessage = "Offer start date cannot be before today.";
        } else if (!endDatePicker.getValue().isAfter(startDatePicker.getValue())) {
            errorMessage = "Offer end date cannot be before start date.";
        } else if (productCombo.getValue() == null) {
            errorMessage = "You must select a product from the list.";
        }
        if (!errorMessage.isEmpty()) {
            Toast.showToast(errorMessage, Toast.NotificationType.ERROR);
            return false;
        }
        return true;
    }

    private int getStatusOrder(Offer.Status status) {
        return switch (status) {
            case ACTIVE -> 1;
            case PENDING -> 2;
            case EXPIRED -> 3;
        };
    }

    private void loadProductCombo() {
        productCombo.getItems().clear();
        DataStore.getDataStore().getProducts().forEach(p -> productCombo.getItems().add(p.getName()));
    }

    private void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}