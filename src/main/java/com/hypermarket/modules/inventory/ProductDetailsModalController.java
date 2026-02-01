package com.hypermarket.modules.inventory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.Optional;
import java.util.stream.Collectors;

import com.hypermarket.data.DataStore;
import com.hypermarket.data.FileManager;
import com.hypermarket.entities.Batch;
import com.hypermarket.entities.Product;
import com.hypermarket.service.Toast;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

public class ProductDetailsModalController {

    @FXML
    private ImageView productImage;
    @FXML
    private TextField nameField;
    @FXML
    private Label idLabel;
    @FXML
    private TextField categoryField;
    @FXML
    private TextField priceField;
    @FXML
    private TextField sizeField;
    @FXML
    private TextField thresholdField;
    @FXML
    private TextArea descriptionArea;
    @FXML
    private TextField quantityField;

    @FXML
    private TableView<Batch> batchesTable;
    @FXML
    private TableColumn<Batch, Integer> batchIdCol;
    @FXML
    private TableColumn<Batch, Integer> batchQtyCol;
    @FXML
    private TableColumn<Batch, String> batchExpiryCol;
    @FXML
    private TableColumn<Batch, String> batchDeliveryCol;

    private Product product;
    private File selectedImageFile;

    @FXML
    public void initialize() {
        batchIdCol.setCellValueFactory(new PropertyValueFactory<>("batchID"));
        batchQtyCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        batchExpiryCol.setCellValueFactory(new PropertyValueFactory<>("expiryDate"));
        batchDeliveryCol.setCellValueFactory(new PropertyValueFactory<>("deliveryDate"));
    }

    public void setProduct(Product product) {
        this.product = product;
        populateFields();
        loadBatches();
    }

    private void populateFields() {
        nameField.setText(product.getName());
        idLabel.setText("ID: #" + product.getProductID());
        categoryField.setText(product.getCategory());
        priceField.setText(String.valueOf(product.getPrice()));
        sizeField.setText(product.getSize());
        thresholdField.setText(String.valueOf(product.getThreshold()));
        descriptionArea.setText(product.getDescription());
        quantityField.setText(String.valueOf(product.getQuantity()));

        loadImage();
    }

    private void loadImage() {
        try {
            String imageName = product.getImageName();
            if (imageName != null && !imageName.equals("null") && !imageName.isEmpty()) {
                File dir = new File(FileManager.PRODUCT_IMAGE_PATH);
                File imageFile = new File(dir, imageName);

                if (!imageFile.exists()) {
                    File png = new File(dir, imageName + ".png");
                    File jpg = new File(dir, imageName + ".jpg");
                    File jpeg = new File(dir, imageName + ".jpeg");

                    if (png.exists())
                        imageFile = png;
                    else if (jpg.exists())
                        imageFile = jpg;
                    else if (jpeg.exists())
                        imageFile = jpeg;
                }

                if (imageFile.exists()) {
                    try (InputStream stream = new FileInputStream(imageFile)) {
                        productImage.setImage(new Image(stream));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadBatches() {
        ObservableList<Batch> allBatches = DataStore.getDataStore().getBatches();
        ObservableList<Batch> productBatches = allBatches.stream()
                .filter(b -> b.getProduct().getProductID() == product.getProductID())
                .collect(Collectors.toCollection(FXCollections::observableArrayList));
        batchesTable.setItems(productBatches);
    }

    @FXML
    private void handleUploadImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Product Image");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", FileManager.IMAGEEXTENSIONS));
        File file = fileChooser.showOpenDialog(nameField.getScene().getWindow());

        if (file != null) {
            selectedImageFile = file;
            try (InputStream stream = new FileInputStream(file)) {
                productImage.setImage(new Image(stream));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleAddBatch(ActionEvent event) {
        Dialog<Batch> dialog = new Dialog<>();
        dialog.setTitle("Enter new batch details");
        Window dialogScene = dialog.getDialogPane().getScene().getWindow();
        Stage dialogStage = (Stage) dialogScene;
        dialogStage.initStyle(StageStyle.UNIFIED);
        dialogStage.getIcons().addAll(new Image(Toast.class.getResource(Toast.blankIconPath).toExternalForm()));

        ButtonType addButtonType = new ButtonType("Add", javafx.scene.control.ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField quantity = new TextField();
        quantity.setPromptText("Quantity");
        DatePicker deliveryDate = new DatePicker(LocalDate.now());
        DatePicker expiryDate = new DatePicker(LocalDate.now().plusMonths(1));

        grid.add(new Label("Quantity:"), 0, 0);
        grid.add(quantity, 1, 0);
        grid.add(new Label("Delivery Date:"), 0, 1);
        grid.add(deliveryDate, 1, 1);
        grid.add(new Label("Expiry Date:"), 0, 2);
        grid.add(expiryDate, 1, 2);

        try {
            dialog.getDialogPane().getScene().getStylesheets()
                    .add(getClass().getResource("/com/hypermarket/css/ProductDetailsModal.css").toExternalForm());
            Node addButton = dialog.getDialogPane().lookupButton(addButtonType);
            addButton.getStyleClass().add("btn-success");

            Node cancelButton = dialog.getDialogPane().lookupButton(ButtonType.CANCEL);
            if (cancelButton != null) {
                cancelButton.getStyleClass().add("btn-secondary");
            }

            addButton.setDisable(true);

            final Node finalAddButton = addButton;
            quantity.textProperty().addListener((observable, oldValue, newValue) -> {
                finalAddButton.setDisable(newValue.trim().isEmpty());
            });
        } catch (Exception e) {
            System.err.println("Could not load CSS for dialog");
            e.printStackTrace();
            Node addButton = dialog.getDialogPane().lookupButton(addButtonType);
            addButton.setDisable(true);
            quantity.textProperty().addListener((observable, oldValue, newValue) -> {
                addButton.setDisable(newValue.trim().isEmpty());
            });
        }

        dialog.getDialogPane().setContent(grid);

        Button addButton = (Button) dialog.getDialogPane().lookupButton(addButtonType);

        addButton.addEventFilter(ActionEvent.ACTION, e -> {
            if (!validateInput(quantity, deliveryDate, expiryDate)) {
                e.consume();
            }
        });
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                try {
                    int qty = Integer.parseInt(quantity.getText());
                    LocalDate del = deliveryDate.getValue();
                    LocalDate exp = expiryDate.getValue();

                    int newId = generateBatchId();

                    String record = newId + FileManager.DELIMETER +
                            product.getProductID() + FileManager.DELIMETER +
                            qty + FileManager.DELIMETER +
                            del.format(FileManager.localDateFormat) + FileManager.DELIMETER +
                            exp.format(FileManager.localDateFormat);

                    return new Batch(record);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
            return null;
        });

        Optional<Batch> result = dialog.showAndWait();

        result.ifPresent(batch -> {
            DataStore.getDataStore().getBatches().add(batch);
            DataStore.getDataStore().saveAllData();
            loadBatches();
            quantityField.setText(String.valueOf(product.getQuantity()));
            String addBatchSuccessMsg = "New batch for product " + product.getName() + " added successfully.";
            Toast.showToast(addBatchSuccessMsg, Toast.NotificationType.INFORMATION);
        });
    }

    private boolean validateInput(TextField quantity, DatePicker deliveryDate, DatePicker expiryDate) {
        String errorMessage = "";
        if (quantity.getText().isEmpty() || deliveryDate.getValue() == null || expiryDate.getValue() == null) {
            errorMessage = "Please fill in all required fields.";
        } else if (!quantity.getText().matches("^\\d{1,}$")) {
            errorMessage = "Invalid quantity entered.";
        } else if (Integer.parseInt(quantity.getText()) <= 0) {
            errorMessage = "Quantity must be greater than 0.";
        } else if (!expiryDate.getValue().isAfter(deliveryDate.getValue())) {
            errorMessage = "Expiry date cannot be before delivery date.";
        } else if (!expiryDate.getValue().isAfter(LocalDate.now())) {
            errorMessage = "Expiry date cannot be before today.";
        }
        if (!errorMessage.isEmpty()) {
            Toast.showToast(errorMessage, Toast.NotificationType.ERROR);
            return false;
        }
        return true;
    }

    private boolean validateInput() {
        String errorMessage = "";
        if (nameField.getText().isEmpty() || categoryField.getText().isEmpty()
                || priceField.getText().isEmpty() || thresholdField.getText().isEmpty()) {
            errorMessage = "Please fill in all required fields.";
        } else if (!nameField.getText().matches("^\\D{1,}$")) {
            errorMessage = "Invalid product name.";
        } else if (!categoryField.getText().matches("^\\D{1,}$")) {
            errorMessage = "Invalid category name.";
        } else if (!priceField.getText().matches("^\\d+(\\.\\d{1,})?$")) {
            errorMessage = "Invalid price entered.";
        } else if (!thresholdField.getText().matches("^\\d{1,}$")) {
            errorMessage = "Invalid threshold entered.";
        } else if (Double.parseDouble(priceField.getText()) <= 0) {
            errorMessage = "Price must be greater than 0.";
        } else if (Integer.parseInt(thresholdField.getText()) < 0) {
            errorMessage = "Threshold must be greater than or equal to 0.";
        }
        if (!errorMessage.isEmpty()) {
            Toast.showToast(errorMessage, Toast.NotificationType.ERROR);
            return false;
        }
        return true;
    }

    private int generateBatchId() {
        ObservableList<Batch> batches = DataStore.getDataStore().getBatches();
        int maxId = 0;
        for (Batch b : batches) {
            if (b.getBatchID() > maxId)
                maxId = b.getBatchID();
        }
        return maxId + 1;
    }

    @FXML
    private void handleUpdate(ActionEvent event) {
        try {
            if (!validateInput()) {
                return;
            }
            product.setName(nameField.getText());
            product.setCategory(categoryField.getText());
            product.setPrice(Double.parseDouble(priceField.getText()));
            product.setSize(sizeField.getText());
            product.setThreshold(Integer.parseInt(thresholdField.getText()));
            product.setDescription(descriptionArea.getText());

            if (selectedImageFile != null) {
                String ext = getFileExtension(selectedImageFile);
                String newImageName = "image_" + product.getProductID() + ext;
                File destDir = new File(FileManager.PRODUCT_IMAGE_PATH);
                if (!destDir.exists())
                    destDir.mkdirs();

                File destFile = new File(destDir, newImageName);
                FileManager.copyImage(selectedImageFile, destFile);

                product.setImageName(newImageName);
            }

            DataStore.getDataStore().saveAllData();

            closeModal();
            String updateProductSuccessMsg = "Product updated successfully!";
            Toast.showToast(updateProductSuccessMsg, Toast.NotificationType.INFORMATION);
        } catch (IOException e) {
            String uploadImageErrorMsg = "Image Upload Failed";
            Toast.showToast(uploadImageErrorMsg, Toast.NotificationType.ERROR);
        }
    }

    @FXML
    private void handleDelete(ActionEvent event) {
        String confirmDeleteMsg = "Are you sure you want to delete " + product.getName() + "?";
        Optional<ButtonType> deleteDecision = Toast.showToast(confirmDeleteMsg, Toast.NotificationType.CONFIRMATION);
        if (deleteDecision.get() == ButtonType.YES) {
            try {
                if (productImage.getImage() != null) {
                    productImage.setImage(null);
                }
                System.gc();

                File imageFile = resolveImageFile(product.getImageName());

                if (imageFile != null && imageFile.exists()) {
                    boolean deleted = imageFile.delete();
                    if (!deleted) {
                        System.err
                                .println("Failed to delete image file (System Lock?): " + imageFile.getAbsolutePath());

                        imageFile.deleteOnExit();
                    } else {
                        System.out.println("Image deleted successfully.");
                    }
                }
            } catch (Exception e) {
                System.out.println("Error attempting to delete image file.");
                e.printStackTrace();
            }

            DataStore.getDataStore().getBatches()
                    .removeIf(b -> b.getProduct().getProductID() == product.getProductID());
            DataStore.getDataStore().getProducts().remove(product);
            DataStore.getDataStore().saveAllData();
            closeModal();
            String deleteSuccessMsg = "Product " + product.getName() + " deleted successfully.";
            Toast.showToast(deleteSuccessMsg, Toast.NotificationType.INFORMATION);
        }
    }

    private File resolveImageFile(String imageName) {
        if (imageName == null || imageName.equals("null") || imageName.isEmpty()) {
            return null;
        }

        File dir = new File(FileManager.PRODUCT_IMAGE_PATH);
        File imageFile = new File(dir, imageName);

        if (!imageFile.exists()) {
            File png = new File(dir, imageName + ".png");
            File jpg = new File(dir, imageName + ".jpg");
            File jpeg = new File(dir, imageName + ".jpeg");

            if (png.exists())
                return png;
            if (jpg.exists())
                return jpg;
            if (jpeg.exists())
                return jpeg;
        }

        return imageFile;
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        closeModal();
    }

    private void closeModal() {
        Stage stage = (Stage) nameField.getScene().getWindow();
        stage.close();
    }

    private String getFileExtension(File file) {
        String name = file.getName();
        int lastIndexOf = name.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return "";
        }
        return name.substring(lastIndexOf);
    }
}
