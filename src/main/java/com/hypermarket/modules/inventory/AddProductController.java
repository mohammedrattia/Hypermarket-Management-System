package com.hypermarket.modules.inventory;

import com.hypermarket.entities.Product; 
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
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import javafx.event.ActionEvent;
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

            Inventory inventorySystem = new Inventory("Admin", 0, "System", "User", "null", "000", "email", "pass", 0.0);
            int newID = inventorySystem.generateNextProductId(); 

            if (this.selectedImageFile != null) {
                File folder = new File("Data/Product_Images");
                if (!folder.exists()) {
                    folder.mkdir();
                }
                File dest = new File("Data/Product_Images/image_" + newID + ".png");
                Files.copy(
                    this.selectedImageFile.toPath(), 
                    dest.toPath(), 
                    StandardCopyOption.REPLACE_EXISTING
                );
            }
            Product newProduct = new Product(newID, name, category, description, 0, price, size, threshold);
            inventorySystem.addProduct(newProduct);            
            clearAllFields();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
