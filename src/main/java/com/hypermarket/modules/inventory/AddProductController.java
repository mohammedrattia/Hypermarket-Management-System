package com.hypermarket.modules.inventory;

import com.hypermarket.entities.Product;
import com.hypermarket.data.FileManager;
import com.hypermarket.entities.Inventory;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.io.File;
import javafx.stage.FileChooser;

public class AddProductController {

    @FXML
    private TextArea descriptionField;

    @FXML
    private TextField nameField;

    @FXML
    private TextField sizeField;

    @FXML
    private TextField priceField;

    @FXML
    private TextField categoryField;

    @FXML
    private TextField thresholdField;

    @FXML
    private ImageView productImageView;

    @FXML
    private Button clearBtn;

    @FXML
    private Button AddImageButton;

    @FXML
    private Button AddProductButton;

    private File selectedImageFile;

    @FXML
    public void initialize() {
        clearBtn.setOnAction(event -> {
            clearAllFields();
        });

        AddImageButton.setOnAction(event -> {
            handleImageSelection();
        });
        if (AddProductButton != null) {
            AddProductButton.setOnAction(event -> handleAddProduct());
        }
    }

    @FXML
    void clearAllFields() {
        nameField.clear();
        sizeField.clear();
        priceField.clear();
        categoryField.clear();
        descriptionField.clear();
        thresholdField.clear();
        productImageView.setImage(null);
        nameField.requestFocus();
    }

    @FXML
    private void handleImageSelection() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Profile Image");
        FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter("Image Files", "*.png");
        fileChooser.getExtensionFilters().add(imageFilter);

        File file = fileChooser.showOpenDialog(AddImageButton.getScene().getWindow());

        if (file != null) {
            this.selectedImageFile = file;
            Image newImage = new Image(selectedImageFile.toURI().toString());
            productImageView.setImage(newImage);
        }
    }

    private void handleAddProduct() {
        if (!validateInput()) {
            return;
        }
        try {
            String name = nameField.getText();
            String description = descriptionField.getText();
            String category = categoryField.getText();
            String size = sizeField.getText();
            double price = Double.parseDouble(priceField.getText());

            int threshold = 5;
            try {
                threshold = Integer.parseInt(thresholdField.getText());
            } catch (NumberFormatException e) {
                System.out.println("Invalid Threshold, using default 5");
            }

            Inventory inventorySystem = new Inventory("Admin", 0, "System", "User", "null", "000", "email", "pass",
                    0.0);
            int newID = inventorySystem.generateNextProductId();

            if (this.selectedImageFile != null) {
                File folder = new File("data/ProductImages");
                if (!folder.exists()) {
                    folder.mkdir();
                }
                File dest = new File(FileManager.PRODUCT_IMAGE_PATH + "image_" + newID + ".png");
                FileManager.copyImage(
                        this.selectedImageFile,
                        dest);
            }
            Product newProduct = new Product(newID, name, category, description, 0, price, size, threshold);
            inventorySystem.addProduct(newProduct);

            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Operation Successful");
            alert.setHeaderText(null);
            alert.setContentText("Product " + newProduct.getName()
                    + " added successfully.");
            alert.showAndWait();

            clearAllFields();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean validateInput() {
        StringBuilder alertText = new StringBuilder();
        if (nameField.getText().trim().isEmpty()) {
            alertText.append("- Product Name is required.\n");
        }
        if (categoryField.getText().trim().isEmpty()) {
            alertText.append("- Category is required.\n");
        }
        if (sizeField.getText().trim().isEmpty()) {
            alertText.append("- Size/Unit is required.\n");
        }

        try {
            double price = Double.parseDouble(priceField.getText().trim());
            if (price < 0) {
                alertText.append("- Price cannot be negative.\n");
            }
        } catch (NumberFormatException e) {
            alertText.append("- Price must be a valid number (e.g., 10.50).\n");
        }

        String threshText = thresholdField.getText().trim();
        if (!threshText.isEmpty()) {
            try {
                int threshold = Integer.parseInt(threshText);
                if (threshold < 0) {
                    alertText.append("- Threshold cannot be negative.\n");
                }
            } catch (NumberFormatException e) {
                alertText.append("- Threshold must be a whole number.\n");
            }
        }

        if (selectedImageFile == null) {
            alertText.append("- Please select a product image.\n");
        }

        if (alertText.length() > 0) {
            makeAlert(alertText.toString());
            return false;
        }
        return true;
    }

    private void makeAlert(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Validation Error");
        alert.setHeaderText("Please correct the following fields:");
        alert.setContentText(message);
        alert.showAndWait();
    }
}
