package com.hypermarket.modules.inventory;

import com.hypermarket.entities.Product;
import com.hypermarket.service.Toast;
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
        FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter("Image Files",
                FileManager.IMAGEEXTENSIONS);
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
            String addProductSuccessMsg = "Product " + newProduct.getName() + " added successfully.";
            Toast.showToast(addProductSuccessMsg, Toast.NotificationType.INFORMATION);

            clearAllFields();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        } else if (selectedImageFile == null) {
            errorMessage = "Image not selected.";
        }
        if (!errorMessage.isEmpty()) {
            Toast.showToast(errorMessage, Toast.NotificationType.ERROR);
            return false;
        }
        return true;
    }
}
