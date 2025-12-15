package com.hypermarket.modules.inventory;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.time.LocalDate;
import java.util.Optional;
import java.util.stream.Collectors;

import com.hypermarket.data.DataStore;
import com.hypermarket.data.FileManager;
import com.hypermarket.entities.Batch;
import com.hypermarket.entities.Product;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
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

public class ProductDetailsModalController {

    @FXML private ImageView productImage;
    @FXML private TextField nameField;
    @FXML private Label idLabel;
    @FXML private TextField categoryField;
    @FXML private TextField priceField;
    @FXML private TextField sizeField;
    @FXML private TextField thresholdField;
    @FXML private TextArea descriptionArea;
    @FXML private TextField quantityField;

    @FXML private TableView<Batch> batchesTable;
    @FXML private TableColumn<Batch, Integer> batchIdCol;
    @FXML private TableColumn<Batch, Integer> batchQtyCol;
    @FXML private TableColumn<Batch, String> batchExpiryCol;
    @FXML private TableColumn<Batch, String> batchDeliveryCol;

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
        quantityField.setText(String.valueOf(product.getTotalQuantity()));

        loadImage();
    }

    private void loadImage() {
        try {
            String imageName = product.getImageName();
            if (imageName != null && !imageName.equals("null") && !imageName.isEmpty()) {
                File imageFile = new File("data/ProductImages/" + imageName);
                if (imageFile.exists()) {
                    productImage.setImage(new Image(imageFile.toURI().toURL().toString()));
                }
            } else {
                 // Load default or leave as is (placeholder in FXML)
            }
        } catch (MalformedURLException e) {
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
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );
        File file = fileChooser.showOpenDialog(nameField.getScene().getWindow());
        if (file != null) {
            selectedImageFile = file;
            try {
                productImage.setImage(new Image(file.toURI().toURL().toString()));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void handleAddBatch(ActionEvent event) {
        Dialog<Batch> dialog = new Dialog<>();
        dialog.setTitle("Add New Batch");
        dialog.setHeaderText("Enter batch details");

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

        // Add styling to dialog
        try {
            dialog.getDialogPane().getScene().getStylesheets().add(getClass().getResource("/com/hypermarket/css/ProductDetailsModal.css").toExternalForm());
            Node addButton = dialog.getDialogPane().lookupButton(addButtonType);
            addButton.getStyleClass().add("btn-success");
            
            Node cancelButton = dialog.getDialogPane().lookupButton(ButtonType.CANCEL);
            if (cancelButton != null) {
                cancelButton.getStyleClass().add("btn-secondary");
            }
            
            // Set initial disabled state
            addButton.setDisable(true);
            
            // Re-assign addButton for listener use
            final Node finalAddButton = addButton; 
             quantity.textProperty().addListener((observable, oldValue, newValue) -> {
                finalAddButton.setDisable(newValue.trim().isEmpty());
            });
        } catch (Exception e) {
            System.err.println("Could not load CSS for dialog");
            e.printStackTrace();
             // Fallback if CSS fails
             Node addButton = dialog.getDialogPane().lookupButton(addButtonType);
             addButton.setDisable(true);
             quantity.textProperty().addListener((observable, oldValue, newValue) -> {
                addButton.setDisable(newValue.trim().isEmpty());
            });
        }

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                try {
                    int qty = Integer.parseInt(quantity.getText());
                    LocalDate del = deliveryDate.getValue();
                    LocalDate exp = expiryDate.getValue();
                    
                    // Generate ID
                    int newId = generateBatchId();
                    
                    // batchID;productID;quantity;deliveryDate;expiryDate
                    String record = newId + FileManager.DELIMETER +
                                    product.getProductID() + FileManager.DELIMETER +
                                    qty + FileManager.DELIMETER +
                                    del.format(FileManager.dateFormat) + FileManager.DELIMETER +
                                    exp.format(FileManager.dateFormat);
                    
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
            product.setQuantity(product.getQuantity() + batch.getQuantity()); // Update product quantity
            DataStore.getDataStore().saveAllData();
            loadBatches();
            quantityField.setText(String.valueOf(product.getTotalQuantity()));
        });
    }

    private int generateBatchId() {
        ObservableList<Batch> batches = DataStore.getDataStore().getBatches();
        int maxId = 0;
        for(Batch b : batches) {
            if(b.getBatchID() > maxId) maxId = b.getBatchID();
        }
        return maxId + 1;
    }

    @FXML
    private void handleUpdate(ActionEvent event) {
        try {
            product.setName(nameField.getText());
            product.setCategory(categoryField.getText());
            product.setPrice(Double.parseDouble(priceField.getText()));
            product.setSize(sizeField.getText());
            product.setThreshold(Integer.parseInt(thresholdField.getText()));
            product.setDescription(descriptionArea.getText());

            if (selectedImageFile != null) {
                String ext = getFileExtension(selectedImageFile);
                String newImageName = "image_" + product.getProductID() + ext;
                File destDir = new File("data/ProductImages/");
                if(!destDir.exists()) destDir.mkdirs();
                
                File destFile = new File(destDir, newImageName);
                FileManager.copyImage(selectedImageFile, destFile);
                
                product.setImageName(newImageName);
            }

            DataStore.getDataStore().saveAllData();
            
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Product updated successfully!");
            alert.showAndWait();
            
            closeModal();

        } catch (NumberFormatException e) {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Invalid Input");
            alert.setContentText("Please check your number fields.");
            alert.showAndWait();
        } catch (IOException e) {
             Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Image Upload Failed");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
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
